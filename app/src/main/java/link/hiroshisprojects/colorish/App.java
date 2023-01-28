package link.hiroshisprojects.colorish;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import javax.swing.JFrame;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Dimension;

public class App {

    public static void main(String[] args) throws IOException {
		String imagePath = App.class.getClassLoader().getResource("react.png").getPath();
		System.out.println(imagePath);
		File file = new File(imagePath);
		BufferedImage buf = ImageIO.read(file);
		BufferedImage resized = resizeBufferedImage(buf, 50, 50);

		ImageIO.write(resized, "png", new File(imagePath));

		List<Map.Entry<Color, Integer>> sortedEntries = rgbValueCounts(resized);
		List<Map.Entry<Color, Integer>> subset = sortedEntries.stream().limit(10).collect(Collectors.toList()); 
		
		for (Map.Entry<Color, Integer> entry : subset) {
			System.out.println("COLOR: " + entry.getKey() + " COUNT " + entry.getValue());
		}
	
	}


	static BufferedImage resizeBufferedImage(BufferedImage img, int width, int height) {
		Image temp = img.getScaledInstance(width, height, Image.SCALE_FAST);
		BufferedImage dimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(temp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}

	static List<Map.Entry<Color, Integer>> rgbValueCounts(BufferedImage img) {
		Map<Color, Integer> valueCounts = new HashMap<>();
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				int pixel = img.getRGB(x, y);
				Color color = new Color(pixel, true);


				if (valueCounts.containsKey(color)) {
					int newCount = valueCounts.get(color) + 1;
					valueCounts.put(color, newCount);
				} else {
					valueCounts.put(color, 1);
				}
			}
		}

		return sortMapByValueCount(valueCounts);

	}


	static <K, V extends Integer> List<Map.Entry<K, V>> sortMapByValueCount(Map<K, V> map) {
		List<Map.Entry<K, V>> arrayList = new ArrayList<>();
		for (Map.Entry<K, V> entry : map.entrySet()) {
			arrayList.add(entry);
		}
		Comparator<Map.Entry<K, V>> byValue = (entry1, entry2) -> {
			if (entry1.getValue() == entry2.getValue()) {
				return entry1.getKey().toString().compareTo(entry2.getKey().toString());
			} 
			return Integer.compare(entry2.getValue(), entry1.getValue());
		}; 

		
		Collections.sort(arrayList, byValue);
		return arrayList;
	} 

	static void displayJFrame(Color color) { 
		  JFrame frame = new JFrame("Tutorialspoint");
		  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  frame.setBackground(color);
		  frame.setPreferredSize(new Dimension(400, 300));
		  frame.pack();
		  frame.setLocationRelativeTo(null);
		  frame.setVisible(true);
   }


    
}
