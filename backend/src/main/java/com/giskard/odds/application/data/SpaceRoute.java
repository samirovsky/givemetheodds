package com.giskard.odds.application.data;

import java.util.List;

public record SpaceRoute(
    String origin, String destination, int travelTime, List<RouteStep> steps) {}
