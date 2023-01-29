package link.hiroshisprojects.colorish;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.awt.Color;
public class RgbKMeans {

	private static final Random RANDOM = new Random();

	private final Map<Color, List<Color>> clusters;

	private List<Color> centroids;

	private List<Color> pixels;
	
	private double distance;

	private int K;

	private int maxIters;

	public RgbKMeans() {
		this.clusters = new HashMap<>();
		this.centroids = new ArrayList<>();
	}

	 
	public void fit(List<Color> pixels, int K, double distance, int maxIters) {
		this.pixels = pixels;
		this.K = K;
		this.maxIters = maxIters;
	}

	public Map<Color, List<Color>> generateClusters() throws KMeansException {
		initializeCentroids();
		System.out.println("CENTROIDS:");
		System.out.println(centroids);
		// for (int i = 0; i < maxIters - 1; i++) {
		// 	for(Color pixel : pixels) {
		// 		Color centroid = findNearestCentroid(pixel);
		// 		assignPixelToCluster(pixel, centroid);
		// 	}

		// 	relocateCentroids();
		// }
		
		return clusters;
	}

	/* Set centroids, randomly */
	private void initializeCentroids() throws KMeansException {

		/* Push randomly generated colors into centroids list */
		for (int i = 0; i < K; i++) {
			int red = Math.round((RANDOM.nextFloat() * 255));
			int green = Math.round((RANDOM.nextFloat() * 255));
			int blue = Math.round((RANDOM.nextFloat() * 255));
			
			centroids.add(new Color(red, green, blue));
		}
	}

	private Color findNearestCentroid(Color pixel) {
		return null;
	}

	private void assignPixelToCluster(Color pixel, Color centroid) {

	}

	private Color relocateCentroid(Color centroid) {
		return null;
	}

	private void relocateCentroids() {
		
	} 
}

class KMeansException extends Exception {}
