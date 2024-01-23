package com.giskard.odds.integrationtests;

import static com.giskard.odds.tools.MultipartFileTools.getExtMultipartFile;
import static com.giskard.odds.tools.MultipartFileTools.getMultipartFile;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.giskard.odds.application.data.BountyHunter;
import com.giskard.odds.application.data.Empire;
import com.giskard.odds.application.data.MillenniumFalconConfig;
import com.giskard.odds.service.JourneyOddsService;
import java.io.IOException;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JourneyServiceIntegrationTest {

  @Autowired private JourneyOddsService journeyOddsService;

  @Autowired private MillenniumFalconConfig config;

  @ParameterizedTest
  @CsvSource({"7,0.0", "8,81.0", "9,90.0", "10,100.0"})
  public void testCalculateOdds_ExamplesScenario(String countdown, String expectedOdds) {

    // Given
    // Convert input data
    double expectedOddsValue = Double.parseDouble(expectedOdds);
    int countdownValue = Integer.parseInt(countdown);
    // Example test scenario
    Empire empire =
        new Empire(
            Arrays.asList(
                new BountyHunter("Hoth", 6),
                new BountyHunter("Hoth", 7),
                new BountyHunter("Hoth", 8)),
            countdownValue);

    // When
    double odds = assertDoesNotThrow(() -> journeyOddsService.calculateOdds(config, empire));

    // Then
    Assertions.assertEquals(expectedOddsValue, odds); // Check if the odds are as expected
  }

  @ParameterizedTest
  @CsvSource({"7,0.0", "8,81.0", "9,90.0", "10,100.0"})
  public void testCalculateOddsUsingFile_examplesScenario(String countdown, String expectedOdds)
      throws IOException {

    // Given
    double expectedOddsValue = Double.parseDouble(expectedOdds);
    var empireFile =
        getMultipartFile("empires", "empire%s.json".formatted(countdown), "empireData");
    var configFile = getMultipartFile(".", "millennium-falcon.json", "configData");

    // When
    double odds =
        assertDoesNotThrow(() -> journeyOddsService.calculateOdds(configFile, empireFile));

    // Then
    Assertions.assertEquals(expectedOddsValue, odds); // Check if the odds are as expected
  }

  @ParameterizedTest
  @CsvSource({"7,0.0", "8,81.0", "9,90.0", "10,100.0"})
  public void testCalculateOddsUsingExtFile_examplesScenario(String countdown, String expectedOdds)
      throws IOException {

    // Given
    double expectedOddsValue = Double.parseDouble(expectedOdds);
    int countdownValue = Integer.parseInt(countdown);
    var empireFile =
        getExtMultipartFile(
            "/Users/samirtabib/Projects/GiveMeTheOdds/cli/examples/example%s/empire.json"
                .formatted(countdownValue - 6),
            "empireData");
    var configFile =
        getExtMultipartFile(
            "/Users/samirtabib/Projects/GiveMeTheOdds/cli/examples/millennium-falcon.json",
            "configData");

    // When
    double odds =
        assertDoesNotThrow(() -> journeyOddsService.calculateOdds(configFile, empireFile));

    // Then
    Assertions.assertEquals(expectedOddsValue, odds); // Check if the odds are as expected
  }
}
