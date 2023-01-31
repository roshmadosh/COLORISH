package link.hiroshisprojects.colorish;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import link.hiroshisprojects.colorish.RgbKMeans.CentroidDistance;

public class KMeansTest {

	private RgbKMeans kMeans;

	private final List<Color> defaultPixels = new ArrayList<>();

	private final int defaultK = 1;

	private final CentroidDistance defaultCentroidDistance = CentroidDistance.EXACT;

	private final int defaultMaxIters = 1;

	@BeforeEach
	public void init() {
		
		kMeans = new RgbKMeans();
		
	}

	@Test
	public void initalizeClustersBeforeFit_throwsException() {

		RgbKMeans kMeans = new RgbKMeans();

		KMeansException thrown = assertThrows(KMeansException.class, 
			() -> kMeans.initializeClusters());

	}

	@Test
	public void givenClustersOfVaryingSize_findKLargestCentroids_returnsExpected() {

		int K = 2;

		Map<Color, List<Color>> testClusters = new HashMap<>();

		testClusters.put(Color.BLUE, Collections.emptyList());

		testClusters.put(Color.RED, new ArrayList<>(Arrays.asList(Color.RED, Color.PINK)));

		testClusters.put(Color.GREEN, new ArrayList<>(Arrays.asList(Color.GREEN)));

		Set<Color> expected = new HashSet<>(Set.of(Color.RED, Color.GREEN));

		Set<Color> actual = kMeans.findKLargestCentroids(testClusters, K);
 
		assertEquals(expected, actual);

	}

	@Test
	public void givenK_initializeClusters_yieldsCentroidsWithKMostCommonPixels() throws KMeansException {

		int K = 2;

		List<Color> pixels = new ArrayList<>(Arrays.asList(Color.GREEN, Color.CYAN, Color.YELLOW));
		pixels.add(Color.CYAN);
		pixels.add(Color.CYAN);
		pixels.add(Color.YELLOW);

		kMeans.fit(pixels, K, CentroidDistance.EXACT, defaultMaxIters);

		kMeans.initializeClusters();

		Set<Color> expectedCentroids = new HashSet<>(Set.of(Color.CYAN, Color.YELLOW));

		Set<Color> actualCentroids = kMeans.getCentroids();

		assertEquals(expectedCentroids, actualCentroids);	

	}

	@Test
	public void givenMostlyRedPixels_findMostCommonPixels_returnsRedPixel() throws KMeansException {
		
		List<Color> pixels = new ArrayList<>();

		pixels.add(Color.BLUE);
		pixels.add(Color.RED);
		pixels.add(Color.RED);
		pixels.add(Color.BLUE);
		pixels.add(Color.RED);

		Color pixel = kMeans.findMostCommonPixel(pixels);

		assertEquals(Color.RED, pixel);
	}

	@Test
	public void givenMostlyRedPixels_generateInitialClusters_returnsRedCluster() throws KMeansException {

		List<Color> pixels = new ArrayList<>();

		pixels.add(Color.BLUE);
		pixels.add(Color.RED);
		pixels.add(Color.RED);
		pixels.add(Color.BLUE);
		pixels.add(Color.RED);

		kMeans.fit(pixels, defaultK, defaultCentroidDistance, defaultMaxIters);

		Map<Color, List<Color>> expectedClusters = new HashMap<>();

		expectedClusters.put(Color.RED, new ArrayList<>());

		Map<Color, List<Color>> actualClusters = kMeans.generateInitialClusters(pixels); 

		assertEquals(expectedClusters, actualClusters);

	}

	@Test
	public void givenNonEmptyClusterValues_resetValues_returnsEmptyClusterValues() {

		Map<Color, List<Color>> initClusters = new HashMap<>();

		initClusters.put(Color.RED, new ArrayList<>(Arrays.asList(Color.RED, Color.PINK)));

		initClusters.put(Color.BLUE, new ArrayList<>(Arrays.asList(Color.BLUE, Color.CYAN)));

		Map<Color, List<Color>> expectedClusters = new HashMap<>();

		expectedClusters.put(Color.RED, Collections.emptyList());

		expectedClusters.put(Color.BLUE, Collections.emptyList());

		Map<Color, List<Color>> actualClusters = kMeans.resetValues(initClusters);

		assertEquals(expectedClusters, actualClusters);

	}

	@Test
	public void givenExactCentroidDistanceAndNoExactCentroidMatch_whenFindNearestCentroid_returnsPixel() throws KMeansException {

		Color pixel = Color.PINK;

		CentroidDistance centroidDistance = CentroidDistance.EXACT;

		Set<Color> centroids = new HashSet<>();

		centroids.add(Color.RED);

		centroids.add(Color.BLUE);

		Color actualPixel = kMeans.findNearestCentroid(centroids, pixel, centroidDistance.distance);

		assertEquals(pixel, actualPixel);
		
	}

	@Test
	public void givenLowCentroidDistance_whenFindNearestCentroid_returnCorrectCentroids() throws KMeansException {

		Color almostRed = new Color(192, 64, 64); 

		Color notRedOrBlue = Color.GREEN; 

		Color almostBlue = new Color(64, 64, 192);

		CentroidDistance centroidDistance = CentroidDistance.LOW;

		Set<Color> centroids = new HashSet<>();

		centroids.add(Color.RED);

		centroids.add(Color.BLUE);

		Color actualAlmostRed = kMeans.findNearestCentroid(centroids, almostRed, centroidDistance.distance);
		
		Color actualAlmostBlue = kMeans.findNearestCentroid(centroids, almostBlue, centroidDistance.distance);
		
		Color actualNeither = kMeans.findNearestCentroid(centroids, notRedOrBlue, centroidDistance.distance);

		assertEquals(Color.RED, actualAlmostRed);

		assertEquals(Color.BLUE, actualAlmostBlue);

		assertEquals(Color.GREEN, actualNeither);
		
	}

	@Test
	public void givenExactCentroidDistance_whenInitializeClusters_thenCentroidsContainAllUniqueColorsInPixels() throws KMeansException {

		List<Color> pixels = new ArrayList<>(Arrays.asList(Color.RED, new Color(255, 1, 0), new Color(255, 0, 1)));

		CentroidDistance centroidDistance = CentroidDistance.EXACT;

		int K = pixels.size();

		kMeans.fit(pixels, K, centroidDistance, defaultMaxIters);
		
		kMeans.initializeClusters();
	
		Set<Color> actualCentroids = kMeans.getCentroids();

		assertEquals(new HashSet<>(pixels), actualCentroids);	
		
	}

	@Test
	public void givenNPairsOfSimilarColors_initializeClusters_formsNClusters() throws KMeansException {

		List<Color> pixels = new ArrayList<>(Arrays.asList(Color.RED, Color.GREEN, Color.BLUE));
		pixels.add(Color.RED);
		pixels.add(Color.GREEN);
		pixels.add(Color.BLUE);
		pixels.add(new Color(250, 0, 0)); 
		pixels.add(new Color(0, 250, 0));
		pixels.add(new Color(0, 0, 250));

		CentroidDistance centroidDistance = CentroidDistance.LOW;

		int K = pixels.size();

		kMeans.fit(pixels, K, centroidDistance, defaultMaxIters);
		
		kMeans.initializeClusters();

		Set<Color> expected = new HashSet<>(Set.of(Color.RED, Color.GREEN, Color.BLUE));
	
		Set<Color> actual = kMeans.getCentroids();

		System.out.println(kMeans.getClusters());

		assertEquals(expected, actual);	
		
	}


	// @Test
	// public void givenMaxCentroidDistance_whenInitializeClusters_thenSingleCentroid() throws KMeansException {

	// 	List<Color> pixels = new ArrayList<>(Arrays.asList(Color.RED, Color.BLUE, Color.GREEN));

	// 	CentroidDistance centroidDistance = CentroidDistance.MAX;

	// 	int K = pixels.size();

	// 	kMeans.fit(pixels, K, centroidDistance, defaultMaxIters);
		
	// 	kMeans.initializeClusters();
	
	// 	Set<Color> actualCentroids = kMeans.getCentroids();

	// 	assertEquals(1, actualCentroids.size());	
		
	// }
	
}
