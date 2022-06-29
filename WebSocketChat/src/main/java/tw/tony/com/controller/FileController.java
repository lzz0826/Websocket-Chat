package tw.tony.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import tw.tony.com.model.ResultSet;
import tw.tony.com.service.FileService;

@Controller
@RequestMapping("/api/file")
public class FileController {
	
	
	@Autowired
	FileService fileService;
	
	
	@RequestMapping("/upload")
	@ResponseBody
	public ResultSet uploadFile(@RequestParam(value = "file") MultipartFile file) {
		return fileService.upload(file);
		
		
	}

}
