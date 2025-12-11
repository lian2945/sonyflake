package com.github.lian2945.sonyflake.annotation;

import org.hibernate.annotations.IdGeneratorType;
import com.github.lian2945.sonyflake.generator.SonyflakeIdGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@IdGeneratorType(SonyflakeIdGenerator.class)
public @interface SonyflakeId {
}
