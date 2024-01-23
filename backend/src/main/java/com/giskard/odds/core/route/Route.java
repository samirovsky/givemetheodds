package com.giskard.odds.core.route;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@Table(name = "routes")
@IdClass(RouteKey.class)
@NoArgsConstructor
@AllArgsConstructor
public class Route {

  @Id
  @Column(nullable = false)
  private String origin;

  @Id
  @Column(nullable = false)
  private String destination;

  @Column(name = "travel_time", nullable = false)
  private Integer travelTime;
}
