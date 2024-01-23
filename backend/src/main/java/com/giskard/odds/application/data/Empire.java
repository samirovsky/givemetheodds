package com.giskard.odds.application.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record Empire(
    @JsonProperty("bounty_hunters") List<BountyHunter> bountyHunters, int countdown) {}
