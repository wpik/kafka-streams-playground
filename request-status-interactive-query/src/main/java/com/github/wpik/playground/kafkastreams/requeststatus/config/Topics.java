package com.github.wpik.playground.kafkastreams.requeststatus.config;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface Topics {
    String REQUEST = "request";

    @Output(REQUEST)
    MessageChannel request();
}
