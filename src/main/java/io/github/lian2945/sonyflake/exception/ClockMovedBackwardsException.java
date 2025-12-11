package io.github.lian2945.sonyflake.exception;

import static io.github.lian2945.sonyflake.constants.SonyflakeConstants.TICK_MILLIS;

public class ClockMovedBackwardsException extends RuntimeException {

  public ClockMovedBackwardsException(Long difference) {
    super("Clock moved backwards by " + difference * TICK_MILLIS + " ms");
  }
}
