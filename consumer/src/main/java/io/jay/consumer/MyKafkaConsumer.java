package io.jay.consumer;

import io.jay.consumer.domain.ProcessedEvent;
import io.jay.consumer.store.EventRepository;
import io.jay.consumer.store.EventStore;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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
