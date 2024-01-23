package com.giskard.odds.api;

import com.giskard.odds.service.JourneyOddsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/journey/odds")
public class JourneyOddsApi {

  private final JourneyOddsService journeyOddsService;

  @Autowired
  public JourneyOddsApi(JourneyOddsService journeyOddsService) {
    this.journeyOddsService = journeyOddsService;
  }

  @PostMapping("/calculate")
  public ResponseEntity<Double> calculateOdds(
      @RequestParam("empireData") MultipartFile empireData) {
    double odds = journeyOddsService.calculateOdds(empireData);
    return ResponseEntity.ok(odds);
  }
}
