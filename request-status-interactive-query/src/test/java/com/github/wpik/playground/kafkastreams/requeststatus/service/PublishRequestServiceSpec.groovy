package com.github.wpik.playground.kafkastreams.requeststatus.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.wpik.playground.kafkastreams.requeststatus.Application
import com.github.wpik.playground.kafkastreams.requeststatus.config.Topics
import com.github.wpik.playground.kafkastreams.requeststatus.domain.Request
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.stream.test.binder.MessageCollector
import org.springframework.messaging.Message
import spock.lang.Specification

import java.util.concurrent.TimeUnit

@SpringBootTest(classes = Application)
class PublishRequestServiceSpec extends Specification {
    @Autowired
    private Topics topics

    @Autowired
    private MessageCollector collector

    @Autowired
    private PublishRequestService publishRequestService

    @Autowired
    private ObjectMapper objectMapper

    def "Published requests are sent to specific kafka topic"() {
        given:
        def request = Request.createForPayload('{"Im":"bear"}')

        publishRequestService.publish(request)

        def messages = collector.forChannel(topics.request())

        expect:
        Message<?> message = messages.poll(1, TimeUnit.SECONDS)

        Request published = objectMapper.readValue(message.payload, Request)
        published.id == request.id
        published.payload == request.payload
    }
}
