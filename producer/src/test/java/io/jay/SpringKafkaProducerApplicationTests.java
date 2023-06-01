package io.jay;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jay.producer.MyKafkaProducer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@EmbeddedKafka
@ExtendWith(SpringExtension.class)
@Import(SpringKafkaProducerApplicationTests.KafkaTestContainersConfiguration.class)
@ContextConfiguration(classes = {SpringKafkaProducerApplicationTests.KafkaTestContainersConfiguration.class})
class SpringKafkaProducerApplicationTests {

    private static String topic = "sample-topic";

    @Autowired
    private MyKafkaProducer producer;

    private KafkaMessageListenerContainer<String, String> container;
    private BlockingQueue<ConsumerRecord<String, String>> consumerRecords;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @BeforeEach
    public void setup() {
        consumerRecords = new LinkedBlockingQueue<>();

        Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps("test-group-id", "false", embeddedKafka);
        DefaultKafkaConsumerFactory<String, String> consumer = new DefaultKafkaConsumerFactory<>(consumerProperties);

        ContainerProperties containerProperties = new ContainerProperties(topic);
        container = new KafkaMessageListenerContainer<>(consumer, containerProperties);
        container.setupMessageListener((MessageListener<String, String>) record -> consumerRecords.add(record));
        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafka.getPartitionsPerTopic());
    }

    @AfterEach
    public void after() {
        container.stop();
    }

    @Test
    public void sendEventTest() throws InterruptedException, JsonProcessingException {
        String payload = "message to send";
        producer.sendMessage(payload);


        ConsumerRecord<String, String> consumerRecord = consumerRecords.poll(1, TimeUnit.SECONDS);


        assertThat(consumerRecord.key(), is(nullValue()));
        assertThat(consumerRecord.value(), equalTo(payload));
    }

    @TestConfiguration
    @EmbeddedKafka
    @ExtendWith(SpringExtension.class)
    @ComponentScan(basePackages = {"io.jay"})
    static class KafkaTestContainersConfiguration {
        @Autowired
        private EmbeddedKafkaBroker embeddedKafkaBroker;

        @Bean("simpleProducerKafkaTemplate")
        @Primary
        public KafkaTemplate<String, String> kafkaTemplate() {
            Map<String, Object> configProps = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
            DefaultKafkaProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(), new StringSerializer());
            return new KafkaTemplate<>(producerFactory);
        }
    }
}


