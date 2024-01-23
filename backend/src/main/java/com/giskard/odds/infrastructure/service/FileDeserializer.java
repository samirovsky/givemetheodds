package com.giskard.odds.infrastructure.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileDeserializer {

  private final ObjectMapper objectMapper;

  public FileDeserializer(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public <T> T deserializeFile(MultipartFile file, Class<T> clazz) throws IOException {
    // Validate the file
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("The file is empty or null.");
    }

    // Deserialize the file content to the specified class
    return objectMapper.readValue(file.getInputStream(), clazz);
  }
}
