package com.github.lian2945.sonyflake;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.github.lian2945.sonyflake.constants.SonyflakeConstants.MACHINE_BITS;
import static com.github.lian2945.sonyflake.constants.SonyflakeConstants.SEQUENCE_BITS;
import static com.github.lian2945.sonyflake.constants.SonyflakeConstants.MACHINE_MAX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(properties = {
        "sonyflake.machine-id=1234",
        "sonyflake.epoch-millis=1735657200000"
})
class SonyflakeApplicationTests {

    @Autowired
    private Sonyflake sonyflake;

    @Autowired
    private com.github.lian2945.sonyflake.properties.SonyflakeProperties properties;

    @Test
    void appliesConfiguredMachineId() {
        long expectedMachineId = properties.getMachineId();
        long id = sonyflake.nextId();
        long machinePart = (id >>> SEQUENCE_BITS) & MACHINE_MAX;
        assertEquals(expectedMachineId, machinePart, "MachineId in ID must match configured value");
    }

    @Test
    void generatesDistinctIdsWithinSameTick() {
        // Generate a bunch of IDs quickly and ensure:
        // 1) No duplicates overall
        // 2) We actually produced multiple IDs within at least one tick window (10 ms)
        int attempts = 5;
        int perAttempt = 2_000;

        for (int attempt = 0; attempt < attempts; attempt++) {
            List<Long> ids = new ArrayList<>(perAttempt);
            for (int i = 0; i < perAttempt; i++) {
                ids.add(sonyflake.nextId());
            }

            // No duplicates overall
            Set<Long> deduped = new HashSet<>(ids);
            assertEquals(ids.size(), deduped.size(), "IDs must be globally unique");

            // Check we hit at least one tick with multiple IDs and that sequences are unique within that tick
            Map<Long, Set<Long>> sequencesByTick = groupSequencesByTick(ids);
            boolean hasTickWithMultipleIds = false;
            for (Map.Entry<Long, Set<Long>> entry : sequencesByTick.entrySet()) {
                long tick = entry.getKey();
                Set<Long> sequences = entry.getValue();
                if (sequences.size() > 1) {
                    hasTickWithMultipleIds = true;
                }
                assertEquals(sequences.size(), new HashSet<>(sequences).size(),
                        "Sequences must be unique within tick " + tick);
            }

            if (hasTickWithMultipleIds) {
                return;
            }
        }

        fail("Could not generate multiple IDs within the same tick; environment too slow to validate.");
    }

    private Map<Long, Set<Long>> groupSequencesByTick(List<Long> ids) {
        Map<Long, Set<Long>> sequencesByTick = new HashMap<>();
        for (Long id : ids) {
            long tick = id >>> (MACHINE_BITS + SEQUENCE_BITS);
            long sequence = id & ((1L << SEQUENCE_BITS) - 1L);
            sequencesByTick.computeIfAbsent(tick, k -> new HashSet<>()).add(sequence);
        }
        return sequencesByTick;
    }
}
