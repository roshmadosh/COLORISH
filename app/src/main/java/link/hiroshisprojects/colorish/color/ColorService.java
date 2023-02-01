package link.hiroshisprojects.colorish.color;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.awt.Color;
import java.io.IOException;

@Service
public class ColorService {

	@Autowired
	ColorGenerator colorGenerator;

	public List<List<ColorDTO>> extractColors(List<MultipartFile> files) throws IOException { 

		List<List<ColorDTO>> colors = new ArrayList<>();

		for (MultipartFile file: files) {

			List<Color> fileColors = colorGenerator.generateColors(file);

			List<ColorDTO> fileColorDtos = fileColors.stream()

				.map(color -> new ColorDTO(color))

				.collect(Collectors.toList());	

			colors.add(fileColorDtos);

		}

		return colors; 
	}
}
