package io.temporal.samples.springboot.dividend;

import io.temporal.activity.ActivityInterface;
import java.util.UUID;

@ActivityInterface
public interface DomainEventSenderActivity {

  boolean sendDomainEvent(UUID corporateActionId);
}
