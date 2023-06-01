package io.jay.consumer;

import io.jay.domain.ProcessedEvent;
import io.jay.data.EventStore;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyKafkaConsumer {

    private final String groupId = "test-group-basic-consumer";
    private final EventStore eventStore;

    @KafkaListener(topics = { "test-topic" }, containerFactory = "basicListenerContainerFactory", groupId = groupId)
    public void listen(String message) {
        System.out.println("[" + groupId + "] basic consumer : " + message);
        eventStore.save(new ProcessedEvent(message, message));
        // handle business
    }
}
