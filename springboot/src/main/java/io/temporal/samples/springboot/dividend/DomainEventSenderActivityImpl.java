package io.temporal.samples.springboot.dividend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.temporal.failure.ApplicationFailure;
import io.temporal.spring.boot.ActivityImpl;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@ActivityImpl
public class DomainEventSenderActivityImpl implements DomainEventSenderActivity {

  @Autowired(required = false)
  private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired private CashTransactionRepository cashTransactionRepository;

  @Value(value = "${samples.message.topic.name}")
  private String topicName;

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public boolean sendDomainEvent(UUID corporateActionId) {
    try {
      cashTransactionRepository.findByCorporateActionId(corporateActionId).stream()
          .forEach(
              cashTransaction -> {
                try {
                  kafkaTemplate
                      .send(topicName, objectMapper.writeValueAsString(cashTransaction))
                      .get();
                } catch (JsonProcessingException e) {
                  throw new RuntimeException(e);
                } catch (ExecutionException e) {
                  throw new RuntimeException(e);
                } catch (InterruptedException e) {
                  throw new RuntimeException(e);
                }
              });

    } catch (Exception e) {
      throw ApplicationFailure.newFailure(
          "Unable to send message.", e.getClass().getName(), e.getMessage());
    }
    return true;
  }
}
