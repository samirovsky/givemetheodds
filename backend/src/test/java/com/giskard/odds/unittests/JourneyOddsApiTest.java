package com.giskard.odds.unittests;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.giskard.odds.api.JourneyOddsApi;
import com.giskard.odds.service.JourneyOddsService;
import java.nio.file.Files;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

public class JourneyOddsApiTest {

  private MockMvc mockMvc;

  @Mock private JourneyOddsService journeyOddsService;

  @InjectMocks private JourneyOddsApi oddsController;

  @BeforeEach
  public void setup() {
    openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(oddsController).build();
  }

  @ParameterizedTest
  @CsvSource({"7,0.0", "8,81.0", "9,90.0", "10,100.0"})
  public void testCalculateOdds(String countdown, String expectedOdds) throws Exception {

    // Given
    // Convert input data
    double expectedOddsValue = Double.parseDouble(expectedOdds);
    int countdownValue = Integer.parseInt(countdown);
    // Configure mock service
    when(journeyOddsService.calculateOdds(any(MultipartFile.class))).thenReturn(expectedOddsValue);
    String originalFileName = "empire%s.json".formatted(countdown);
    ClassPathResource resource = new ClassPathResource("empires/%s".formatted(originalFileName));
    byte[] fileContent = Files.readAllBytes(resource.getFile().toPath());
    MockMultipartFile mockFile =
        new MockMultipartFile("empireData", originalFileName, "application/json", fileContent);

    // When
    var resultsActions =
        mockMvc.perform(MockMvcRequestBuilders.multipart("/journey/odds/calculate").file(mockFile));

    // Then
    resultsActions.andExpect(status().isOk()).andExpect(content().string(expectedOdds));
    verify(journeyOddsService).calculateOdds(mockFile);
  }
}
