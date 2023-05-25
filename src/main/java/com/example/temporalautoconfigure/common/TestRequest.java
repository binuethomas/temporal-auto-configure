package com.example.temporalautoconfigure.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import javax.annotation.Nonnull;
import javax.validation.constraints.Min;
@SuperBuilder
@Getter
@EqualsAndHashCode
@ToString
@Value
@Jacksonized
public class TestRequest {

    @Min(value = 1, message = "Valid ID must be specified")
    private final long id;

    @Nonnull
    public String getRequestType() {
        return "Test";
    }
}
