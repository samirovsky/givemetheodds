package com.giskard.odds.service;

import com.giskard.odds.api.exception.CannotCalculateOddsCheckedException;
import com.giskard.odds.api.exception.CannotCalculateOddsException;
import com.giskard.odds.api.exception.CannotInitializeRoutesException;
import com.giskard.odds.api.exception.InvalidInputException;
import com.giskard.odds.api.exception.InvalidRouteException;
import com.giskard.odds.application.data.BountyHunter;
import com.giskard.odds.application.data.Empire;
import com.giskard.odds.application.data.MillenniumFalconConfig;
import com.giskard.odds.application.data.RouteStep;
import com.giskard.odds.core.route.Route;
import com.giskard.odds.core.route.RouteRepository;
import com.giskard.odds.infrastructure.service.FileDeserializer;
import com.giskard.odds.infrastructure.service.Graph;
import jakarta.annotation.PostConstruct;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class JourneyOddsService {

  private final RouteRepository routeRepository;

  private final MillenniumFalconConfig config;

  private final FileDeserializer fileDeserializer;

  private Graph galaxyGraph = new Graph();

  public JourneyOddsService(
      RouteRepository routeRepository,
      MillenniumFalconConfig millenniumFalconConfig,
      FileDeserializer fileDeserializer) {
    this.routeRepository = routeRepository;
    this.config = millenniumFalconConfig;
    this.fileDeserializer = fileDeserializer;
  }

  public double calculateOdds(MultipartFile file) {
    try {
      return calculateOdds(fileDeserializer.deserializeFile(file, Empire.class));
    } catch (Exception e) {
      throw new InvalidInputException("Invalid empire file", e);
    }
  }

  public double calculateOdds(MultipartFile fileMillenniumFalconConfig, MultipartFile fileEmpire)
      throws CannotCalculateOddsCheckedException {
    Empire empire;
    MillenniumFalconConfig millenniumFalconConfig;
    try {
      empire = fileDeserializer.deserializeFile(fileEmpire, Empire.class);
      log.info(empire.toString());
      millenniumFalconConfig =
          fileDeserializer.deserializeFile(
              fileMillenniumFalconConfig, MillenniumFalconConfig.class);
      log.info(millenniumFalconConfig.toString());
      log.info(millenniumFalconConfig.departure());
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new InvalidInputException("Invalid empire file", e);
    }
    return calculateOdds(millenniumFalconConfig, empire);
  }

  public double calculateOdds(Empire empire) {
    try {
      return calculateOdds(config, empire);
    } catch (CannotCalculateOddsCheckedException e) {
      throw new CannotCalculateOddsException(e);
    }
  }

  public double calculateOdds(MillenniumFalconConfig millenniumFalconConfig, Empire empire)
      throws CannotCalculateOddsCheckedException {
    try {
      // Generate all possible routes considering autonomy and refueling needs
      Set<List<RouteStep>> possibleRoutes = generatePossibleRoutes(millenniumFalconConfig, empire);

      // Minimize the risk of encountering bounty hunters
      List<RouteStep> optimalRoute = findOptimalRoute(possibleRoutes, empire.bountyHunters());

      // Calculate the overall odds of success for the optimal route
      return calculateSuccessOdds(optimalRoute, empire.bountyHunters());
    } catch (Exception e) {
      throw new CannotCalculateOddsCheckedException("Cannot calculate the odds", e);
    }
  }

  public List<RouteStep> findOptimalRoute(
      Set<List<RouteStep>> routes, List<BountyHunter> bountyHunters) {
    return routes.stream()
        .min(
            Comparator.comparingDouble(
                route -> {
                  try {
                    return assessRouteRisk(route, bountyHunters);
                  } catch (InvalidRouteException e) {
                    log.warn("Cannot asses risk for route {}", route);
                  }
                  return 0;
                }))
        .orElse(null);
  }

  public double calculateSuccessOdds(List<RouteStep> routeSteps, List<BountyHunter> bountyHunters)
      throws InvalidRouteException {
    if (routeSteps == null) return 0.0;
    double risk = assessRouteRisk(routeSteps, bountyHunters);
    return (1 - risk) * 100; // Convert risk to success probability
  }

  public Set<List<RouteStep>> generatePossibleRoutes(MillenniumFalconConfig config, Empire empire) {
    Set<List<RouteStep>> feasibleRoutes = new HashSet<>();
    List<RouteStep> currentPath = new ArrayList<>();
    if (Objects.isNull(config)) {
      throw new CannotCalculateOddsException("Config is null");
    }
    log.info("config is" + config);
    currentPath.add(new RouteStep(config.departure(), 0));
    exploreRoutes(
        galaxyGraph,
        empire,
        config.departure(),
        config.arrival(),
        config.autonomy(),
        currentPath,
        feasibleRoutes,
        0);
    return feasibleRoutes;
  }

  private Graph createGalaxyGraph(List<Route> routes) {
    if (Objects.isNull(routes) || routes.isEmpty()) {
      throw new CannotInitializeRoutesException("No valid routes found in database");
    }
    if (routes.stream()
        .anyMatch(
            route ->
                Objects.isNull(route.getDestination())
                    || Objects.isNull(route.getOrigin())
                    || route.getTravelTime() < 0)) {
      throw new CannotInitializeRoutesException("At least one Invalid route was found in database");
    }
    Graph galaxyGraph = new Graph();
    for (Route route : routes) {
      galaxyGraph.addRoute(route.getOrigin(), route.getDestination(), route.getTravelTime());
    }
    return galaxyGraph;
  }

  private void exploreRoutes(
      Graph galaxyGraph,
      Empire empire,
      String currentPlanet,
      String destination,
      int remainingAutonomy,
      List<RouteStep> currentPath,
      Set<List<RouteStep>> feasibleRoutes,
      int currentDay) {
    if (currentPlanet.equals(destination)) {
      feasibleRoutes.add(new ArrayList<>(currentPath)); // Add the current path to feasible routes
      return;
    }

    for (Graph.Edge edge : galaxyGraph.getEdges(currentPlanet)) {
      if (!currentPath.stream().anyMatch(step -> step.planet().equals(edge.getDestination()))
          && currentDay + edge.getTravelTime() <= empire.countdown()) {
        // Check if the route step intersects with bounty hunters
        boolean intersectsBountyHunters =
            isBountyHunterPresent(
                new RouteStep(edge.getDestination(), currentDay + edge.getTravelTime()),
                empire.bountyHunters());

        if (intersectsBountyHunters && remainingAutonomy >= edge.getTravelTime()) {
          // Add waiting step to avoid bounty hunters
          currentPath.add(new RouteStep(currentPlanet, currentDay + 1));
          exploreRoutes(
              galaxyGraph,
              empire,
              currentPlanet,
              destination,
              remainingAutonomy,
              currentPath,
              feasibleRoutes,
              currentDay + 1);
          currentPath.remove(currentPath.size() - 1); // Backtrack
        }

        if (remainingAutonomy >= edge.getTravelTime()) {
          // Continue exploring this route
          currentPath.add(new RouteStep(edge.getDestination(), currentDay + edge.getTravelTime()));
          exploreRoutes(
              galaxyGraph,
              empire,
              edge.getDestination(),
              destination,
              remainingAutonomy - edge.getTravelTime(),
              currentPath,
              feasibleRoutes,
              currentDay + edge.getTravelTime());
          currentPath.remove(currentPath.size() - 1); // Backtrack
        } else {
          // Consider refueling
          currentPath.add(new RouteStep(currentPlanet, currentDay + 1)); // Wait for a day to refuel
          exploreRoutes(
              galaxyGraph,
              empire,
              currentPlanet,
              destination,
              config.autonomy(),
              currentPath,
              feasibleRoutes,
              currentDay + 1);
          currentPath.remove(currentPath.size() - 1); // Backtrack
        }
      }
    }
  }

  public double assessRouteRisk(List<RouteStep> routeSteps, List<BountyHunter> bountyHunters)
      throws InvalidRouteException {
    double risk = 0.0;
    double probabilityOfCapture = 0.1; // 10% chance of being captured at each encounter
    double probabilityOfNotBeingCaptured = 0.9;
    if (Objects.isNull(routeSteps)) {
      throw new InvalidRouteException("Invalid route steps");
    }
    for (RouteStep step : routeSteps) {
      if (isBountyHunterPresent(step, bountyHunters)) {
        risk += probabilityOfCapture;
        probabilityOfCapture *= probabilityOfNotBeingCaptured; // Decrease for the next step
      }
    }

    return risk;
  }

  private boolean isBountyHunterPresent(RouteStep step, List<BountyHunter> bountyHunters) {
    return Objects.nonNull(bountyHunters)
        && bountyHunters.stream()
            .anyMatch(
                hunter -> hunter.planet().equals(step.planet()) && hunter.day() == step.day());
  }

  @PostConstruct
  private void initializeRoutesMap() {
    List<Route> routes = routeRepository.findAllRoutes();
    galaxyGraph = createGalaxyGraph(routes);
  }
}
