package tw.tony.com.service;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import tw.tony.com.model.ResultSet;

@Service
public class FileService {

	private static Logger log = LoggerFactory.getLogger(FileService.class);// slf4j

	@Value("${chat-file.upload-path}")
	private String UPLOAD_PATH;

	public ResultSet upload(MultipartFile file) {
		ResultSet resultSet = new ResultSet();
		String filename = file.getOriginalFilename();
		String filePath = UPLOAD_PATH;
		File targetFile = new File(filePath);
		if (!targetFile.exists()) {
			targetFile.mkdir();
		}
		File file1 = new File(filePath + filename);
		try {
			if (file.isEmpty()) {
				log.error("文件上傳失敗,文件為空");
				resultSet.fail("文件上傳失敗,文件為空");
				return resultSet;
			}
			file.transferTo(file1);
			log.info("上傳文件成功");
			return resultSet;
		} catch (Exception e) {
			log.error("文件上傳失敗");
			log.error(e.toString(), e);
			resultSet.fail("文件上傳失敗");
		}
		return resultSet;
	}

}
