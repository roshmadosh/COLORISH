package link.hiroshisprojects.colorish;

import java.awt.Color;

public class MainColor extends Color {
	private static final double MAX_DISTANCE = 10.00;

	public MainColor(Color color) {
		super(color.getRGB());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MainColor other = (MainColor) obj;
		if (distance(other) > MAX_DISTANCE)
			return false;
		return true;
	}

	private double distance(MainColor other) {
		return euclideanDistance(other);
	}

	private double euclideanDistance(MainColor other) {
		double radicand = Math.pow(getRed() - other.getRed(), 2) + Math.pow(getGreen() - other.getGreen(), 2) + Math.pow(getBlue() - other.getBlue(), 2); 
		return Math.sqrt(radicand); 
	}

}
