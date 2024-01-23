package com.giskard.odds.infrastructure.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

public class Graph {
  private Map<String, List<Edge>> adjList = new HashMap<>();

  public void addRoute(String origin, String destination, int travelTime) {
    adjList.putIfAbsent(origin, new ArrayList<>());
    adjList.putIfAbsent(destination, new ArrayList<>());
    adjList.get(origin).add(new Edge(destination, travelTime));
    adjList.get(destination).add(new Edge(origin, travelTime)); // Since routes are bidirectional
  }

  public boolean isEmpty() {
    return adjList.isEmpty();
  }

  public List<Edge> getEdges(String planet) {
    return adjList.getOrDefault(planet, Collections.emptyList());
  }

  private void findPathsRecursive(
      String current, String end, int remainingDays, List<Edge> path, List<List<Edge>> paths) {
    if (current.equals(end)) {
      paths.add(new ArrayList<>(path));
      return;
    }
    if (remainingDays <= 0) return;

    for (Edge edge : adjList.getOrDefault(current, Collections.emptyList())) {
      if (!path.contains(edge)) { // Prevents revisiting the same edge
        path.add(edge);
        findPathsRecursive(edge.destination, end, remainingDays - edge.travelTime, path, paths);
        path.remove(path.size() - 1);
      }
    }
  }

  // Edge class represents a route between two planets

  @Getter
  public static class Edge {
    String destination;
    int travelTime;

    Edge(String destination, int travelTime) {
      this.destination = destination;
      this.travelTime = travelTime;
    }
  }
}
