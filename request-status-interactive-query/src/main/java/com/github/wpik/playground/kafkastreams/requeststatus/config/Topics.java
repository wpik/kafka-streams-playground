package com.github.wpik.playground.kafkastreams.requeststatus.config;

import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface Topics {
    String REQUEST = "request";
    String STATUS = "status";

    @Output(REQUEST)
    MessageChannel request();

    @Input(STATUS)
    KTable<String, String> status();
}
