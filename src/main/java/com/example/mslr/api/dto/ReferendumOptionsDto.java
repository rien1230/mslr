package com.example.mslr.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record ReferendumOptionsDto(
        @JsonProperty("options") List<Map<String, String>> options
) {}
