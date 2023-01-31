package link.hiroshisprojects.colorish.color;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.Color;
import java.io.IOException;

@RestController
@RequestMapping("/v1/api/color")
public class ColorController {

	@Autowired
	ColorService colorService;
	
	@GetMapping
	public String smokeTest() {

		return "Color controller";

	}

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE,
								MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<List<Color>> extractColors(@RequestParam("image") List<MultipartFile> files) {
		try {

			return ResponseEntity.ok(colorService.extractColors(files));

		} catch (IOException ioe) {

			ioe.printStackTrace();

			return ResponseEntity.internalServerError().build();
			
		}

	}

}
