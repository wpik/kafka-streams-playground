package com.github.wpik.playground.kafkastreams.requeststatus.config;

import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(Topics.class)
public class StreamsConfig {
    @StreamListener
    void initTable(@Input(Topics.STATUS) KTable<String, String> statusTable) {
        //this is needed by spring-cloud-stream to init KTable
    }
}
