package io.jay.data;

import io.jay.domain.ProcessedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
