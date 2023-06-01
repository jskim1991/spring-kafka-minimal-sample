package io.jay.consumer.store;

import io.jay.consumer.domain.ProcessedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class EventStore {

    private final EventRepository repository;

    public void save(ProcessedEvent event) {
        this.repository.save(event);
    }

    public List<String> findAll() {
        return repository.findAll().stream()
                .map(ProcessedEvent::getId)
                .toList();
    }
}
