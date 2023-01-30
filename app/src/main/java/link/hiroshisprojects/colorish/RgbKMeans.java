package link.hiroshisprojects.colorish;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;


import java.awt.Color;

/**
 *	K-Means cluster algorithm implementation for pixel RGB values. Call fit() before generateClusters().
 * */
public class RgbKMeans {

	private static final Random RANDOM = new Random();

	private Map<Color, List<Color>> clusters;

	private List<Color> centroids;

	private List<Color> pixels;
	
	private double distance;

	private int K;

	private int maxIters;

	private boolean isFit;

	public RgbKMeans() {

		this.clusters = new HashMap<>();
		
		this.centroids = new ArrayList<>();
		
		this.isFit = false;
		
	}

	 
	public void fit(List<Color> pixels, int K, double distance, int maxIters) {

		this.pixels = pixels;

		this.K = K;

		this.maxIters = maxIters;

		this.isFit = true;

	}

	public Map<Color, List<Color>> generateClusters() throws KMeansException {

		if (!isFit) 
			
			throw new KMeansException();

		centroids = initializeCentroids();

		for (int i = 0; i < maxIters; i++) {

			resetClusters();

			for(Color pixel : pixels) {

				Color centroid = findNearestCentroid(pixel);

				updateClusters(centroid, pixel);

			}

			centroids =	relocateCentroids();
		}
		
		return clusters;

	}

	/* Set centroids, randomly */
	protected List<Color> initializeCentroids() throws KMeansException {

		List<Color> initialCentroids = new ArrayList<>();

		/* Push randomly generated colors into centroids list */
		for (int i = 0; i < K; i++) {

			int red = Math.round((RANDOM.nextFloat() * 255));
			int green = Math.round((RANDOM.nextFloat() * 255));
			int blue = Math.round((RANDOM.nextFloat() * 255));
			
			initialCentroids.add(new Color(red, green, blue));

		} return initialCentroids;
	}

	protected Color findNearestCentroid(Color pixel) throws KMeansException {

		double min = Double.MAX_VALUE;

		Optional<Color> nearestCentroid = Optional.empty();

		for (Color centroid : centroids) {

			double distance = calculateDistanceFromCentroid(centroid, pixel);

			if (distance < min) {

				nearestCentroid = Optional.of(centroid);

				min = distance;

			}
			
		} return nearestCentroid.orElseThrow(KMeansException::new);

	}


	protected double calculateDistanceFromCentroid(Color centroid, Color pixel) {

		/* Euclidean distance */
		double reds = Math.pow(centroid.getRed() - pixel.getRed(), 2);	
		double greens = Math.pow(centroid.getGreen() - pixel.getGreen(), 2);	
		double blues = Math.pow(centroid.getBlue() - pixel.getBlue(), 2);	

		return Math.sqrt(reds + greens + blues);
		
	}

	/* Assignment made a side-effect so that I don't have to pass clusters as an argment */
	protected void updateClusters(Color centroid, Color pixel) {
		
		clusters.get(centroid).add(pixel);

	}

	protected void resetClusters() {

		Map<Color, List<Color>> newClusters = new HashMap<>();

		for (Color centroid : centroids) {
			
			newClusters.put(centroid, new ArrayList<>());

		}
		
		setClusters(newClusters);
		
	}


	/* Uses polling method to relocate centroid. */
	private List<Color> relocateCentroids() throws KMeansException {

		List<Color> updatedCentroids = new ArrayList<>();

		for (Map.Entry<Color, List<Color>> cluster : clusters.entrySet()) {

			Color centroid = cluster.getKey();

			List<Color> pixels = cluster.getValue();

			if (pixels.isEmpty()) {

				updatedCentroids.add(centroid);

				continue;

			}

			Color newCentroid = findMostCommonPixel(pixels);

			updatedCentroids.add(newCentroid);

		}
		
		return updatedCentroids;
	} 

	/* Like a valueCounts in Python */
	private Color findMostCommonPixel(List<Color> pixels) throws KMeansException {

		Map<Color, Integer> valueCounts = new HashMap<>();	

		for (Color pixel : pixels) 
			
			valueCounts.compute(pixel, (key, count) -> (count == null) ? 1 : count + 1);

		Optional<Color> mostCommonPixel = Optional.empty();

		int highestCount = 0;

		for (Map.Entry<Color, Integer> valueCount : valueCounts.entrySet()) {

			Color currentPixel = valueCount.getKey();

			int currentCount = valueCount.getValue();

			if (mostCommonPixel.isEmpty()) {
				
				mostCommonPixel = Optional.of(currentPixel);

				highestCount = 1;

				continue;

			}		

			if (currentCount > highestCount) {

				mostCommonPixel = Optional.of(currentPixel);

				highestCount = currentCount;

			}

		} return mostCommonPixel.orElseThrow(KMeansException::new);

	}


	public Map<Color, List<Color>> getClusters() {
		return clusters;
	}


	public List<Color> getCentroids() {
		return centroids;
	}


	public List<Color> getPixels() {
		return pixels;
	}


	public double getDistance() {
		return distance;
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


	public void setCentroids(List<Color> centroids) {

		this.centroids = centroids;

	}
	public void setClusters(Map<Color, List<Color>> clusters) {

		this.clusters = clusters;

	}

	public void setPixels(List<Color> pixels) {
		this.pixels = pixels;
	}


	public void setDistance(double distance) {
		this.distance = distance;
	}


	public void setK(int k) {
		K = k;
	}


	public void setMaxIters(int maxIters) {
		this.maxIters = maxIters;
	}

}


class KMeansException extends Exception {}
