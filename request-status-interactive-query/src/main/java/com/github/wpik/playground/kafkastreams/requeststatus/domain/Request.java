package com.github.wpik.playground.kafkastreams.requeststatus.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Request {
    private String id;
    private Object payload;

    public static Request createForPayload(Object payload) {
        return new Request(UUID.randomUUID().toString(), payload);
    }
}
