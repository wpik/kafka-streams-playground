package com.github.wpik.playground.kafkastreams.requeststatus.api;

import com.github.wpik.playground.kafkastreams.requeststatus.domain.Request;
import com.github.wpik.playground.kafkastreams.requeststatus.service.PublishRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "${api.version.prefix.v1}/request")
@RequiredArgsConstructor
@Slf4j
public class RequestController {

    private final PublishRequestService publishRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWithId receiveRequest(@Valid @RequestBody Object payload) {
        log.debug("Received payload: {}", payload);

        var request = Request.createForPayload(payload);
        publishRequestService.publish(request);

        return ResponseWithId.builder().id(request.getId()).build();
    }
}
