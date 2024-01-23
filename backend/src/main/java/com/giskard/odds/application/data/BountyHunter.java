package com.giskard.odds.application.data;

import java.util.Objects;

public record BountyHunter(String planet, int day) {
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BountyHunter that = (BountyHunter) o;
    return day == that.day && Objects.equals(planet, that.planet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(planet, day);
  }
}
