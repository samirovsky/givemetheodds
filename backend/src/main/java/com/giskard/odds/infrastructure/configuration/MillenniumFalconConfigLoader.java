package com.giskard.odds.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giskard.odds.application.data.MillenniumFalconConfig;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class MillenniumFalconConfigLoader {

  @Value("${millenniumFalcon.configFile}")
  private Resource configFile;

  @Bean
  public MillenniumFalconConfig millenniumFalconConfig() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(configFile.getInputStream(), MillenniumFalconConfig.class);
  }
}
