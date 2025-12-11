package com.github.lian2945.sonyflake.properties;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import static com.github.lian2945.sonyflake.constants.SonyflakeConstants.*;

@Validated
@ConfigurationProperties(prefix = "sonyflake")
public class SonyflakeProperties {
    @Min(
            value = 0,
            message = "machineId must be greater than or equal to 0"
    )
    @Max(
            value = MACHINE_MAX,
            message = "machineId must be less than or equal to " + MACHINE_MAX
    )
    @NotNull(message = "machineId is required")
    private final Long machineId;

    @Max(value = LOCAL_DATE_MAX, message = "epochMillis must be less than or equal to " + LOCAL_DATE_MAX)
    @NotNull(message = "epochMillis is required")
    private final Long epochMillis;

    @ConstructorBinding
    public SonyflakeProperties(Long machineId, Long epochMillis) {
        this.machineId = machineId != null ? machineId : 1L;
        this.epochMillis = epochMillis != null ? epochMillis : 1735657200000L;
    }

    public Long getMachineId() {
        return machineId;
    }

    public Long getEpochMillis() {
        return epochMillis;
    }
}
