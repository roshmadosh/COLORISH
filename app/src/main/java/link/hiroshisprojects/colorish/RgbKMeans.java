package link.hiroshisprojects.colorish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.awt.Color;

/**
 *	K-Means cluster algorithm implementation for pixel RGB values. Call fit() before generateClusters().
 * */
public class RgbKMeans {

	public static final int LOW_DIFF = 32;
	public static final int MID_DIFF = 64;
	public static final int MAX_DIFF = 96;
	enum CentroidDistance {

		EXACT(0),
		LOW(Math.sqrt(Math.pow(LOW_DIFF, 2) * 3)),
		MID(Math.sqrt(Math.pow(MID_DIFF, 2) * 3)),
		MAX(Math.sqrt(Math.pow(MAX_DIFF, 2) * 3));

		final double distance;

		CentroidDistance(double distance) {
			this.distance = distance;
		}
	}

	// private static final Random RANDOM = new Random();

	private Map<Color, List<Color>> clusters;

	private List<Color> pixels;
	
	private double maxCentroidDistance;

	private int K;

	private int maxIters;

	private boolean isFit;

	public RgbKMeans() {

		this.clusters = new HashMap<>();
		
		this.isFit = false;
		
	}

	 
	public void fit(List<Color> pixels, int K, CentroidDistance maxCentroidDistance, int maxIters) {

		this.pixels = pixels;

		this.K = K;

		this.maxCentroidDistance = maxCentroidDistance.distance;

		this.maxIters = maxIters;

		this.isFit = true;

	}

	public void initializeClusters() throws KMeansException {

		if (!isFit) 
			
			throw new KMeansException();

		Map<Color, List<Color>> tempClusters = generateInitialClusters(new ArrayList<>(this.pixels));

		for (int i = 0; i < this.maxIters; i++) {

			tempClusters = resetValues(tempClusters);

			for(Color pixel : this.pixels) {

				Set<Color> centroids = tempClusters.keySet();

				Color centroid = findNearestCentroid(centroids, pixel, this.maxCentroidDistance);

				tempClusters = addPixelToCluster(tempClusters, centroid, pixel);

			}

			tempClusters = relocateCentroids(tempClusters);

		}

		Set<Color> kLargestCentroids = findKLargestCentroids(tempClusters, K);

		
		this.clusters = filterDownToKClusters(tempClusters, kLargestCentroids);

	}

	protected Map<Color, List<Color>> generateInitialClusters(List<Color> pixels) throws KMeansException {

		Map<Color, List<Color>> initialClusters = new HashMap<>();

		Color initialCentroid = findMostCommonPixel(pixels);

		initialClusters.put(initialCentroid, new ArrayList<>());

		return initialClusters;

	}


	protected Color findNearestCentroid(Set<Color> centroids, Color pixel, double maxCentroidDistance) throws KMeansException {

		double min = Double.MAX_VALUE;

		Optional<Color> nearestCentroid = Optional.empty();

		for (Color centroid : centroids) {

			double distance = calculateDistanceFromCentroid(centroid, pixel);

			if (distance < min) {

				nearestCentroid = Optional.of(centroid);

				min = distance;

			}
			
		} return min > maxCentroidDistance ? pixel : nearestCentroid.orElseThrow(KMeansException::new);
		

	}

	/* Euclidean distance */
	protected double calculateDistanceFromCentroid(Color centroid, Color pixel) {

		double reds = Math.pow(centroid.getRed() - pixel.getRed(), 2);	
		double greens = Math.pow(centroid.getGreen() - pixel.getGreen(), 2);	
		double blues = Math.pow(centroid.getBlue() - pixel.getBlue(), 2);	

		return Math.sqrt(reds + greens + blues);
		
	}

	/* Add pixel to existing cluster, or create a new one and then add it */
	protected Map<Color, List<Color>> addPixelToCluster(Map<Color, List<Color>> clusters, Color centroid, Color pixel) {

		Map<Color, List<Color>> updatedClusters = new HashMap<>(clusters);	

		updatedClusters.computeIfAbsent(centroid, key -> new ArrayList<Color>()).add(pixel);

		return updatedClusters;

	}

	/* Reset values for each centroid */
	protected Map<Color, List<Color>> resetValues(Map<Color, List<Color>> clusters) {

		Map<Color, List<Color>> newClusters = new HashMap<>();

		Set<Color> centroids = clusters.keySet();

		for (Color centroid : centroids) {
			
			newClusters.put(centroid, new ArrayList<>());

		} return newClusters;
		
	}


	/* Uses polling method to relocate centroid. */
	private Map<Color, List<Color>> relocateCentroids(Map<Color, List<Color>> tempClusters) throws KMeansException {

		Map<Color, List<Color>> updatedClusters = new HashMap<>();

		for (Color centroid : tempClusters.keySet()) {

			List<Color> clusterValues = new HashMap<>(tempClusters).remove(centroid);

			Color newCentroid = findMostCommonPixel(clusterValues);


			updatedClusters.put(newCentroid, clusterValues);
			
		} return updatedClusters;

	} 


	protected Color findMostCommonPixel(List<Color> pixels) throws KMeansException {

		Map<Color, Integer> valueCounts = new HashMap<>();	

		for (Color pixel : pixels) 
			
			valueCounts.compute(pixel, (key, count) -> (count == null) ? 1 : count + 1);

		Optional<Color> mostCommonPixel = Optional.empty();

		int highestCount = 0;

		for (Map.Entry<Color, Integer> valueCount : valueCounts.entrySet()) {

			Color currentPixel = valueCount.getKey();

			int currentCount = valueCount.getValue();

			if (currentCount > highestCount) {

				mostCommonPixel = Optional.of(currentPixel);

				highestCount = currentCount;

			}

		} return mostCommonPixel.orElseThrow(KMeansException::new);

	}

	/* Helper function for determining which clusters to filter out */
	protected Set<Color> findKLargestCentroids(Map<Color, List<Color>> tempClusters, int K) {

		List<Map.Entry<Color, List<Color>>> clusterList = new ArrayList<>();


		/* Iterate through each key-value pair, add to list if among the K largest */
		for (Map.Entry<Color, List<Color>> cluster : tempClusters.entrySet()) {
			
			clusterList.add(cluster);

			if (clusterList.size() > K) {
				
				clusterList.sort((cluster1, cluster2) -> Integer.compare(cluster2.getValue().size(), cluster1.getValue().size()));

				clusterList.remove(clusterList.size() - 1);

			}

		}

		Set<Color> kLargestCentroids = clusterList.stream()

			.map(entry -> entry.getKey())

			.collect(Collectors.toSet());

		return kLargestCentroids;

	}

	protected Map<Color, List<Color>> filterDownToKClusters(Map<Color, List<Color>> tempClusters, Set<Color> kLargestCentroids) {

		Map<Color, List<Color>> updatedClusters = new HashMap<>();

		for (Map.Entry<Color, List<Color>> cluster : tempClusters.entrySet()) {

			Color centroid = cluster.getKey();

			List<Color> clusterValues = cluster.getValue();

			if (kLargestCentroids.contains(centroid)) {

				updatedClusters.put(centroid, clusterValues);
				
			}

		} return updatedClusters;

	}


	public Map<Color, List<Color>> getClusters() {
		return clusters;
	}


	public Set<Color> getCentroids() {
		return this.clusters.keySet();
	}


	public List<Color> getPixels() {
		return pixels;
	}


	public int getK() {
		return K;
	}


	public int getMaxIters() {
		return maxIters;
	}


	public boolean isFit() {
		return isFit;
	}

}


class KMeansException extends Exception {}
