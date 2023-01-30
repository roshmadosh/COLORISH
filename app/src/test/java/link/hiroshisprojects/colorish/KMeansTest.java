package link.hiroshisprojects.colorish;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class KMeansTest {

	private RgbKMeans kMeans;

	@BeforeEach
	public void init() {
		
		kMeans = new RgbKMeans();
		
		List<Color> pixels = new ArrayList<>();

		pixels.add(Color.RED);
		pixels.add(Color.GREEN);
		pixels.add(Color.BLUE);

		int K = 3;
		double distance = 0.0;
		int maxIters = 2;
		
		kMeans.fit(pixels, K, distance, maxIters);

	}

	@Test
	public void generateClusterBeforeFit_throwsException() {

		RgbKMeans kMeans = new RgbKMeans();

		KMeansException thrown = assertThrows(KMeansException.class, 
			() -> kMeans.generateClusters());

	}

	@Test
	public void initializeClusters_returnsKClusters() throws KMeansException {

		List<Color> centroids = kMeans.initializeCentroids();

		int actual = centroids.size();

		assertEquals(kMeans.getK(), actual);
	}

	@Test
	public void givenRedPixelAndRedCentroid_whenCalculateDistanceFromCentroid_thenZeroDistance() throws KMeansException {
			
		Color pixel = Color.RED;

		Color centroid = Color.RED;

		double actualDistance = kMeans.calculateDistanceFromCentroid(centroid, pixel);

		assertEquals(0.0, actualDistance);

	} 

	@Test
	public void givenRedPixelAndBlueCentroid_whenCalculateDistanceFromCentroid_thenNonZeroDistance() throws KMeansException {
			
		Color pixel = Color.RED;

		Color centroid = Color.BLUE;

		double actualDistance = kMeans.calculateDistanceFromCentroid(centroid, pixel);

		assertTrue(actualDistance > 0.0);	
		
	}

	@Test
	public void givenRedGreenCentroidAndGreenPixel_whenFindNearestCentroid_thenGreenCentroid() throws KMeansException {
		
		List<Color> centroids = new ArrayList<>();

		centroids.add(Color.RED);

		centroids.add(Color.GREEN);

		Color pixel = Color.RED;

		kMeans.setCentroids(centroids);

		Color actualCentroid = kMeans.findNearestCentroid(pixel);

		assertEquals(Color.RED, actualCentroid);

	}


	@Test
	public void givenExistingCluster_whenUpdateClusters_thenClustersUpdated() {

		/* ARRANGE */
		Color centroid = Color.WHITE;

		Color pixel = Color.WHITE;

		Map<Color, List<Color>> initialClusters = new HashMap<>();

		List<Color> initialClusterPixels = new ArrayList<>(Arrays.asList(Color.GRAY, Color.LIGHT_GRAY));
		
		Map<Color, List<Color>> expectedClusters = new HashMap<>();

		List<Color> expectedClusterPixels = new ArrayList<>(initialClusterPixels);

		expectedClusterPixels.add(pixel);

		expectedClusters.put(centroid, expectedClusterPixels);

		initialClusters.put(centroid, new ArrayList<>(initialClusterPixels)); 

		kMeans.setClusters(initialClusters);

		/* ACT */
		kMeans.updateClusters(centroid, pixel);

		 
		/* ASSERT */
		assertEquals(expectedClusters, kMeans.getClusters());	

	}

	@Test
	public void whenGenerateClusters_thenKManyClusters() throws KMeansException {

		Map<Color, List<Color>> clusters = kMeans.generateClusters();

		int actualClusterCount = clusters.entrySet().size();	

		System.out.println(clusters);

		assertEquals(kMeans.getK(), actualClusterCount);

	}

}
