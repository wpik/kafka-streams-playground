package com.github.wpik.playground.kafkastreams.requeststatus.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.wpik.playground.kafkastreams.requeststatus.domain.Request
import com.github.wpik.playground.kafkastreams.requeststatus.service.PublishRequestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [RequestController])
class RequestControllerSpec extends Specification {
    @Autowired
    private MockMvc mockMvc

    @Autowired
    private PublishRequestService publishRequestService

    @Autowired
    private ObjectMapper objectMapper

    def "Request is published using service and id is returned"() {
        given:
        def payload = '{"yes":1,"no":"why"}'

        when:
        def results = mockMvc.perform(post('/api/v1/request').contentType(MediaType.APPLICATION_JSON).content(payload))

        then:
        results
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

        def responseObject = objectMapper.readValue(results.andReturn().response.contentAsString, ResponseWithId)
        UUID.fromString(responseObject.id) //valid UUID

        and:
        1 * publishRequestService.publish({
            verifyAll(it, Request) {
                it.id != null
                UUID.fromString(it.id) //valid UUID
                it.payload == ['yes': 1, 'no': 'why']
            }
        })
    }

    def "When non-JSON payload is received, 400 is returned"() {
        when:
        def results = mockMvc.perform(post('/api/v1/request').contentType(MediaType.APPLICATION_JSON).content(payload))

        then:
        results.andExpect(status().isBadRequest())

        where:
        payload     || _
        '{"yes"'    || _
        'Im alive!' || _
    }

    @TestConfiguration
    static class Config {
        DetachedMockFactory factory = new DetachedMockFactory()

        @Bean
        PublishRequestService publishRequestService() {
            return factory.Mock(PublishRequestService)
        }
    }
}
