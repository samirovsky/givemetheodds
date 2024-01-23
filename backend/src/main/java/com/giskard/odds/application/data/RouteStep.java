package com.giskard.odds.application.data;

import java.util.Objects;

public record RouteStep(String planet, int day) {
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RouteStep routeStep = (RouteStep) o;
    return day == routeStep.day && Objects.equals(planet, routeStep.planet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(planet, day);
  }
}
