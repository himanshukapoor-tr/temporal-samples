package io.temporal.samples.springboot.dividend;

import io.temporal.samples.springboot.dividend.model.Position;
import io.temporal.spring.boot.ActivityImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@ActivityImpl
public class PositionActivityImpl implements PositionActivity {

  @Autowired private RestTemplate restTemplate;

  @Override
  public List<Position> fetchPositions() {
    return restTemplate.getForEntity("http://localhost:3030/positions", List.class).getBody();
  }
}
