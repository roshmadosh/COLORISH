package link.hiroshisprojects.colorish.color;

import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class ColorService {

	public List<Color> extractColors(List<MultipartFile> files) throws IOException { 

		for (MultipartFile file: files) {

			BufferedImage buf = ImageIO.read(file.getInputStream());
			
		}

		return Collections.emptyList();
	}
}
