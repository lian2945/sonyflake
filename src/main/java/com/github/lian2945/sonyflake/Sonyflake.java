package com.github.lian2945.sonyflake;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.lian2945.sonyflake.exception.ClockMovedBackwardsException;
import com.github.lian2945.sonyflake.properties.SonyflakeProperties;

import java.util.concurrent.atomic.AtomicLong;

import static com.github.lian2945.sonyflake.constants.SonyflakeConstants.*;

@Component
public class Sonyflake {

    private final SonyflakeProperties sonyflakeProperties;
    // state layout: [sign(1)] [tick (39)] [machine (8)] [sequence (16)]
    private final AtomicLong state = new AtomicLong(0L);

    @Autowired
    public Sonyflake(SonyflakeProperties sonyflakeProperties) {
        this.sonyflakeProperties = sonyflakeProperties;
    }

    public Long nextId() {
        while (true) {
            // Get current tick
            long currentTick = currentTick();
            // Get before state(tick + sequence)
            long prev = state.get();

            // Get before tick and sequence
            long beforeTick = prev >>> (MACHINE_BITS + SEQUENCE_BITS);
            long beforeSequence = prev & SEQUENCE_MAX;

            // Validate clock moved backwards
            if (currentTick < beforeTick) {
                throw new ClockMovedBackwardsException(beforeTick - currentTick);
            }

            long nextTick;
            long nextSequence;

            if(currentTick > beforeTick) {
                nextTick = currentTick;
                nextSequence = 0L;
            }
            else {
                // Sequence increment
                nextSequence = (beforeSequence + 1) & SEQUENCE_MAX;

                // Sequence overflow, wait next tick
                if (nextSequence == 0) {
                    nextTick = waitNextTick(beforeTick);
                }
                else {
                    nextTick = beforeTick;
                }
            }

            // newState generate
            long newState = (nextTick << SEQUENCE_BITS) | nextSequence;

            if (state.compareAndSet(prev, newState)) {
                return (nextTick << (MACHINE_BITS + SEQUENCE_BITS))
                        | (sonyflakeProperties.getMachineId() << SEQUENCE_BITS)
                        | nextSequence;
            }
        }
    }

    private long currentTick() {
        long now = System.currentTimeMillis();
        long elapsed = now - sonyflakeProperties.getEpochMillis();

        return elapsed / TICK_MILLIS;
    }

    private long waitNextTick(long lastTick) {
        long t = currentTick();
        while (t <= lastTick) {
            try {
                Thread.sleep(1);
            }
            catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            t = currentTick();
        }
        return t;
    }

}
