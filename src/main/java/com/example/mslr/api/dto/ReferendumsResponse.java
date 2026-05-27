package com.example.mslr.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ReferendumsResponse(
        @JsonProperty("Referendums") List<ReferendumDto> referendums
) {}
