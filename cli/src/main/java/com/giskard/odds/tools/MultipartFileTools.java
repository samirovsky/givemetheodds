package com.giskard.odds.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileTools {

  public static MultipartFile getExtMultipartFile(String filePath, String multipartFileName)
      throws IOException {
    Path path = Path.of(filePath);
    String originalFileName = path.getFileName().toString();
    String contentType = "application/json";

    byte[] content = Files.readAllBytes(path);
    return new MockMultipartFile(multipartFileName, originalFileName, contentType, content);
  }
}
