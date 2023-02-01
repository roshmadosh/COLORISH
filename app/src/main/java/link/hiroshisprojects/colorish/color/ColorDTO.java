package link.hiroshisprojects.colorish.color;

import java.awt.Color;

/**
 *
 *	java.awt.Color has lots of class attributes that I won't need, using a DTO to limit to RGB values.
 *
 * */
public class ColorDTO {

	private final int red;

	private final int green;
	
	private final int blue;

	public ColorDTO(Color color) {

		this.red = color.getRed();

		this.green = color.getGreen();

		this.blue = color.getBlue();

	}

	public int getRed() {

		return red;

	}

	public int getGreen() {

		return green;

	}

	public int getBlue() {

		return blue;
		
	}

	@Override
	public String toString() {

		return "ColorDTO [red=" + red + ", green=" + green + ", blue=" + blue + "]";

	}

}
