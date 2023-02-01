package link.hiroshisprojects.colorish.color;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import java.awt.Color;
import java.io.IOException;

public interface ColorGenerator {

	List<Color> generateColors(MultipartFile file) throws IOException;

}
