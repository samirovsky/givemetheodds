package com.giskard.odds.tools;

import java.io.IOException;
import java.nio.file.Files;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileTools {

  public static MultipartFile getMultipartFile(String filePath, String multipartFileName)
      throws IOException {
    var file = ResourceUtils.getFile("classpath:%s".formatted(filePath));
    var fileContent = Files.readAllBytes(file.toPath());
    var originalFileName = file.getName();
    return new MockMultipartFile(
        multipartFileName, originalFileName, "application/json", fileContent);
  }
}
