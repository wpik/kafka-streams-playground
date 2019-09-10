package com.github.wpik.playground.kafkastreams.requeststatus.service;

import com.github.wpik.playground.kafkastreams.requeststatus.config.Headers;
import com.github.wpik.playground.kafkastreams.requeststatus.config.Topics;
import com.github.wpik.playground.kafkastreams.requeststatus.domain.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublishRequestService {
    private final Topics topics;

    public void publish(Request request) {
        var message = MessageBuilder
                .withPayload(request)
                .setHeader(Headers.REQUEST_ID, request.getId())
                .build();

        topics.request().send(message);
    }
}
