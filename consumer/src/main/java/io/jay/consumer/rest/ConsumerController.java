package io.jay.consumer.rest;

import io.jay.consumer.store.EventStore;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/consumer")
@RequiredArgsConstructor
public class ConsumerController {

    private final EventStore eventStore;

    @GetMapping("/all")
    public List<String> all() {
        return eventStore.findAll();
    }
}
