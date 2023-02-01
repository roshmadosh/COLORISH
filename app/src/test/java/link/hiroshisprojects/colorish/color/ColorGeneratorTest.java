package link.hiroshisprojects.colorish.color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.io.IOException;


public class ColorGeneratorTest {

	private Resource redImage;

	private ColorGenerator colorGenerator;
	
	@BeforeEach()
	public void init() {

		redImage = new ClassPathResource("solidRed.png");

		colorGenerator = new ColorGeneratorImpl();
		
	}
	
	@Test
	public void givenSolidRedImage_whenGenerateColors_thenListOfColorRed() throws IOException {

		MultipartFile multipart = new MockMultipartFile("test", redImage.getInputStream()); 

		List<Color> colors = colorGenerator.generateColors(multipart);	

		List<Color> expected = new ArrayList<>(Arrays.asList(Color.RED));
		
		assertEquals(expected, colors);

	}

}
