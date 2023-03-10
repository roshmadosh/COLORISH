package link.hiroshisprojects.colorish.kmeans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Color;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import link.hiroshisprojects.colorish.kmeans.RgbKMeans.CentroidDistance;

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

	@DisplayName("Invoking initializeClusters() before invoking fit() throws an exception.")
	@Test
	public void initalizeClusters_throwsException() {

		RgbKMeans kMeans = new RgbKMeans();

		KMeansException thrown = assertThrows(KMeansException.class, 
			() -> kMeans.initializeClusters());

	}

	@DisplayName("findLargestCentroids() returns correct number of centroids and in decreasing size order")
	@Test
	public void findKLargestCentroids_order() {

		int K = 2;

		Map<Color, List<Color>> testClusters = new HashMap<>();

		testClusters.put(Color.BLUE, Collections.emptyList());

		testClusters.put(Color.RED, new ArrayList<>(Arrays.asList(Color.RED, Color.PINK)));

		testClusters.put(Color.GREEN, new ArrayList<>(Arrays.asList(Color.GREEN)));

		List<Color> expected = new ArrayList<>(Arrays.asList(Color.RED, Color.GREEN));

		List<Color> actual = kMeans.findLargestCentroids(testClusters, K);
 
		assertEquals(expected, actual);

	}

	@DisplayName("initalizeClusters() returns correct number of clusters and in decreasing size order")
	@Test
	public void initializeClusters_order() throws KMeansException {

		int K = 2;

		List<Color> pixels = new ArrayList<>(Arrays.asList(Color.GREEN, Color.CYAN, Color.YELLOW));
		pixels.add(Color.CYAN);
		pixels.add(Color.CYAN);
		pixels.add(Color.YELLOW);

		kMeans.fit(pixels, K, CentroidDistance.EXACT, defaultMaxIters);

		kMeans.initializeClusters();

		List<Color> expectedCentroids = new ArrayList<>(Arrays.asList(Color.CYAN, Color.YELLOW));

		List<Color> actualCentroids = kMeans.getLargestCentroids();

		assertEquals(expectedCentroids, actualCentroids);

	}

	@DisplayName("findMostCommonPixel() returns a red pixel when given mostly red pixels.")
	@Test
	public void findMostCommonPixel_() throws KMeansException {
		
		List<Color> pixels = new ArrayList<>();

		pixels.add(Color.BLUE);
		pixels.add(Color.RED);
		pixels.add(Color.RED);
		pixels.add(Color.BLUE);
		pixels.add(Color.RED);

		Color pixel = kMeans.findMostCommonPixel(pixels);

		assertEquals(Color.RED, pixel);
	}

	@DisplayName("generateInitialClusters() returns a red cluster when given mostly red pixels.")
	@Test
	public void generateInitialClusters_() throws KMeansException {

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

	@DisplayName("resetValues() resets cluster values to empty lists.")
	@Test
	public void resetValues_() {

		Map<Color, List<Color>> initClusters = new HashMap<>();

		initClusters.put(Color.RED, new ArrayList<>(Arrays.asList(Color.RED, Color.PINK)));

		initClusters.put(Color.BLUE, new ArrayList<>(Arrays.asList(Color.BLUE, Color.CYAN)));

		Map<Color, List<Color>> expectedClusters = new HashMap<>();

		expectedClusters.put(Color.RED, Collections.emptyList());

		expectedClusters.put(Color.BLUE, Collections.emptyList());

		Map<Color, List<Color>> actualClusters = kMeans.resetValues(initClusters);

		assertEquals(expectedClusters, actualClusters);

	}

	@DisplayName("findNearestCentroid() returns the pixel itself when pixel != centroid for any centroid and CentroidDistance.EXACT.")
	@Test
	public void findNearestCentroid_nonMatchWithExact() throws KMeansException {

		Color pixel = Color.PINK;

		CentroidDistance centroidDistance = CentroidDistance.EXACT;

		Set<Color> centroids = new HashSet<>();

		centroids.add(Color.RED);

		centroids.add(Color.BLUE);

		Color actualPixel = kMeans.findNearestCentroid(centroids, pixel, centroidDistance.distance);

		assertEquals(pixel, actualPixel);
		
	}

	@DisplayName("findNearestCentroid() returns expected centroid with CentroidDistance.LOW.")
	@Test
	public void findNearestCentroid_similarWithLow() throws KMeansException {

		Color almostRed = new Color(255 - RgbKMeans.LOW_DIFF, RgbKMeans.LOW_DIFF, RgbKMeans.LOW_DIFF);

		Color notRedOrBlue = Color.GREEN; 

		Color almostBlue = new Color(RgbKMeans.LOW_DIFF, RgbKMeans.LOW_DIFF, 255 - RgbKMeans.LOW_DIFF);

		CentroidDistance centroidDistance = CentroidDistance.LOW;

		Set<Color> centroids = new HashSet<>();

		centroids.add(Color.RED);

		centroids.add(Color.BLUE);

		Color actualAlmostRed = kMeans.findNearestCentroid(centroids, almostRed, centroidDistance.distance);
		
		Color actualAlmostBlue = kMeans.findNearestCentroid(centroids, almostBlue, centroidDistance.distance);
		
		Color actualNeither = kMeans.findNearestCentroid(centroids, notRedOrBlue, centroidDistance.distance);

		assertAll(

			() -> assertEquals(Color.RED, actualAlmostRed), 

			() -> assertEquals(Color.BLUE, actualAlmostBlue), 

			() -> assertEquals(notRedOrBlue, actualNeither) 

		);

	}

	@DisplayName("initializeClusters() returns all unique pixel colors as centroids when CentroidDistance.EXACT.")
	@Test
	public void initializeClusters_exact() throws KMeansException {

		List<Color> pixels = new ArrayList<>(Arrays.asList(Color.RED, new Color(255, 1, 0), new Color(255, 0, 1)));

		CentroidDistance centroidDistance = CentroidDistance.EXACT;

		int K = pixels.size();

		kMeans.fit(pixels, K, centroidDistance, defaultMaxIters);
		
		kMeans.initializeClusters();
	
		List<Color> actualCentroids = kMeans.getLargestCentroids();

		assertThat(pixels).hasSameElementsAs(actualCentroids);
		
	}

	@DisplayName("initializeClusters() forms N clusters when given N-many pairs of similar pixels.")
	@Test
	public void initializeClusters_NPairs() throws KMeansException {

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

		List<Color> expected = new ArrayList<>(Arrays.asList(Color.RED, Color.GREEN, Color.BLUE));

		List<Color> actual = kMeans.getLargestCentroids();

		assertThat(expected).hasSameElementsAs(actual);	

	}
	
}
