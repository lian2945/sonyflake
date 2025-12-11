package com.github.lian2945.sonyflake.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import com.github.lian2945.sonyflake.Sonyflake;

public class SonyflakeIdGenerator implements IdentifierGenerator {
    private final Sonyflake sonyflake;

    public SonyflakeIdGenerator(Sonyflake sonyflake) {
        this.sonyflake = sonyflake;
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        return sonyflake.nextId();
    }
}
