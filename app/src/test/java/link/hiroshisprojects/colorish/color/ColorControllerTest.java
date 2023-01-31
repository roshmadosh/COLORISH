package link.hiroshisprojects.colorish.color;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import java.awt.Color;

@SpringBootTest
@AutoConfigureMockMvc
public class ColorControllerTest {

	
	@Autowired
	MockMvc mockMvc;


	@Test
	public void GET_smokeTest_returnsString() throws Exception {

		this.mockMvc.perform(get(ColorConstants.API_ENDPOINT)).andDo(print()).andExpect(status().isOk());	

	} 


	@Test
	public void POST_givenRedImage_extractColors_returnsListWithColorRed() throws Exception {

		Resource resource = new ClassPathResource("solidRed.png");

		File file = new File(resource.getURI());
		
		FileInputStream input = new FileInputStream(file);

		MockMultipartFile multipart = new MockMultipartFile(ColorConstants.FORM_REQUEST_PARAM, 
			file.getName(), "multipart/form-data", 
			input
		);

		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
				.multipart(ColorConstants.API_ENDPOINT)
				.file(multipart))		
				.andExpect(status().isOk())
				.andReturn();
				
		String content = result.getResponse().getContentAsString();

		assertEquals(String.format("[%s]", Color.RED), content);

	}
}
