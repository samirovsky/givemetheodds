package com.giskard.odds.unittests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.giskard.odds.api.exception.InvalidRouteException;
import com.giskard.odds.application.data.BountyHunter;
import com.giskard.odds.application.data.MillenniumFalconConfig;
import com.giskard.odds.application.data.RouteStep;
import com.giskard.odds.core.route.RouteRepository;
import com.giskard.odds.infrastructure.service.FileDeserializer;
import com.giskard.odds.service.JourneyOddsService;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class JourneyOddsServiceTest {

  @Mock private RouteRepository routeRepository;

  @Mock private MillenniumFalconConfig config;

  @Mock private FileDeserializer fileDeserializer;

  @InjectMocks private JourneyOddsService journeyOddsService;

  @BeforeEach
  void setUp() {
    journeyOddsService = new JourneyOddsService(routeRepository, config, fileDeserializer);
  }

  @Test
  void testFindOptimalRoute() {
    // Given
    List<BountyHunter> mockBountyHunters =
        List.of(
            new BountyHunter("Hoth", 6), new BountyHunter("Hoth", 7), new BountyHunter("Hoth", 8));
    List<RouteStep> route1 =
        Arrays.asList(
            new RouteStep("Tatooine", 0),
            new RouteStep("Dagobah", 6),
            new RouteStep("Dagobah", 7),
            new RouteStep("Hoth", 8),
            new RouteStep("Endor", 9));
    List<RouteStep> route2 =
        Arrays.asList(
            new RouteStep("Tatooine", 0),
            new RouteStep("Hoth", 6),
            new RouteStep("Hoth", 7),
            new RouteStep("Endor", 8));
    Set<List<RouteStep>> mockRoutes = Set.of(route1, route2);

    // When
    List<RouteStep> optimalRoute =
        journeyOddsService.findOptimalRoute(mockRoutes, mockBountyHunters);

    // Then
    assertEquals(route1, optimalRoute, "Route1 should be selected as it has the lowest risk.");
  }

  @Test
  void testAssessRouteRisk() {
    // Given
    List<BountyHunter> mockBountyHunters =
        List.of(
            new BountyHunter("Hoth", 6), new BountyHunter("Hoth", 7), new BountyHunter("Hoth", 8));
    List<RouteStep> route1 =
        Arrays.asList(
            new RouteStep("Tatooine", 0),
            new RouteStep("Dagobah", 6),
            new RouteStep("Dagobah", 7),
            new RouteStep("Hoth", 8),
            new RouteStep("Endor", 9));
    List<RouteStep> route2 =
        Arrays.asList(
            new RouteStep("Tatooine", 0),
            new RouteStep("Hoth", 6),
            new RouteStep("Hoth", 7),
            new RouteStep("Endor", 8));

    // When
    var risk1 =
        assertDoesNotThrow(() -> journeyOddsService.assessRouteRisk(route1, mockBountyHunters));
    var risk2 =
        assertDoesNotThrow(() -> journeyOddsService.assessRouteRisk(route2, mockBountyHunters));

    // Then
    assertEquals(0.1, risk1);
    assertEquals(0.19, risk2);
  }

  @Test
  void testAssessRouteRisk_invalidRoute() {
    // Given
    List<BountyHunter> mockBountyHunters =
        List.of(
            new BountyHunter("Hoth", 6), new BountyHunter("Hoth", 7), new BountyHunter("Hoth", 8));
    List<RouteStep> route1 = null;

    // When & Then
    assertThrows(
        InvalidRouteException.class,
        (() -> journeyOddsService.assessRouteRisk(route1, mockBountyHunters)));
  }
}
