package com.giskard.odds.application.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MillenniumFalconConfig(
    int autonomy, String departure, String arrival, @JsonProperty("routes_db") String routesDb) {}
