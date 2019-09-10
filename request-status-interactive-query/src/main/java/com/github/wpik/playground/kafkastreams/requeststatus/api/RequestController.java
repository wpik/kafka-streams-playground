package com.github.wpik.playground.kafkastreams.requeststatus.api;

import com.github.wpik.playground.kafkastreams.requeststatus.domain.Request;
import com.github.wpik.playground.kafkastreams.requeststatus.service.PublishRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.state.HostInfo;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UncheckedIOException;

@RestController
@RequestMapping(path = "${api.version.prefix.v1}/request")
@RequiredArgsConstructor
@Slf4j
public class RequestController {

    private final PublishRequestService publishRequestService;

    private final InteractiveQueryService interactiveQueryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWithId receiveRequest(@Valid @RequestBody Object payload) {
        log.debug("Received payload: {}", payload);

        var request = Request.createForPayload(payload);
        publishRequestService.publish(request);

        return ResponseWithId.builder().id(request.getId()).build();
    }

    @GetMapping("/{id}")
    Object getStatus(@PathVariable("id") String id, HttpServletResponse response) {
        HostInfo hostInfo = interactiveQueryService.getHostInfo("status-store", id, Serdes.String().serializer());

        if (interactiveQueryService.getCurrentHostInfo().equals(hostInfo)) {
            log.info("This host");

            var store = interactiveQueryService.getQueryableStore("status-store", QueryableStoreTypes.keyValueStore());
            return store.get(id);

        } else {
            log.info("Another host");

            try {
                response.sendRedirect("http://" + hostInfo.host() + ":" + hostInfo.port() + "/api/v1/request/" + id);
                return null;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
