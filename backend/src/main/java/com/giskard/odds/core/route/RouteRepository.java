package com.giskard.odds.core.route;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RouteRepository extends CrudRepository<Route, Long> {
  @Query(value = "SELECT * FROM routes", nativeQuery = true)
  List<Route> findAllRoutes();
}
