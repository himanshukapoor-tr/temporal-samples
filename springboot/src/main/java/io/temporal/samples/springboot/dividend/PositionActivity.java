package io.temporal.samples.springboot.dividend;

import io.temporal.activity.ActivityInterface;
import io.temporal.samples.springboot.dividend.model.Position;
import java.util.List;

@ActivityInterface
public interface PositionActivity {
  List<Position> fetchPositions();
}
