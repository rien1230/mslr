package com.example.mslr.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReferendumDto(
        @JsonProperty("referendum_id") String referendumId,
        @JsonProperty("status") String status,
        @JsonProperty("referendum_title") String referendumTitle,
        @JsonProperty("referendum_desc") String referendumDesc,
        @JsonProperty("referendum_options") ReferendumOptionsDto referendumOptions
) {}
