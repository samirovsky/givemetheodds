package com.giskard.odds.core.route;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RouteKey implements Serializable {
  private String origin;
  private String destination;
}
