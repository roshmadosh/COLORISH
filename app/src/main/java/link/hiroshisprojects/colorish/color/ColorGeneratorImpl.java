package link.hiroshisprojects.colorish.color;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import link.hiroshisprojects.colorish.kmeans.RgbKMeans;
import link.hiroshisprojects.colorish.kmeans.KMeansException;

@Component
public class ColorGeneratorImpl implements ColorGenerator {

	private static final int MAX_CLUSTER_COUNT = 8;

	@Override
	public List<Color> generateColors(MultipartFile file) throws IOException {

		BufferedImage buf = ImageIO.read(file.getInputStream());

		BufferedImage resized = resizeImage(150, 150, buf);

		List<Color> pixels = generatePixels(resized);

		RgbKMeans rgbKMeans = new RgbKMeans();

		rgbKMeans.fit(pixels, MAX_CLUSTER_COUNT, RgbKMeans.CentroidDistance.LOW, 1);

		try {

			rgbKMeans.initializeClusters();

			return rgbKMeans.getLargestCentroids();

		} catch (KMeansException kme) {

			kme.printStackTrace();

			return Collections.emptyList();

		}


	}

	private BufferedImage resizeImage(int width, int height, BufferedImage img) {

		Image temp = img.getScaledInstance(width, height, Image.SCALE_FAST);

		BufferedImage dimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics2D g2d = dimg.createGraphics();

		g2d.drawImage(temp, 0, 0, null);

		g2d.dispose();

		return dimg;

	}


	private List<Color> generatePixels(BufferedImage img) {

		List<Color> pixels = new ArrayList<>();

		for (int y = 0; y < img.getHeight(); y++) {

			for (int x = 0; x < img.getWidth(); x++) {

				int pixel = img.getRGB(x, y);

				pixels.add(new Color(pixel, true));

			}

		} return pixels;

	}

}
