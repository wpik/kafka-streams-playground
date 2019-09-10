package com.github.wpik.playground.kafkastreams.requeststatus.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@JsonDeserialize(builder = ResponseWithId.ResponseWithIdBuilder.class)
@Value
@Builder
public class ResponseWithId {
    @NonNull
    private String id;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ResponseWithIdBuilder {
    }
}
