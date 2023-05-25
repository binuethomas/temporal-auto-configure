package com.example.temporalautoconfigure.activity;

import com.example.temporalautoconfigure.dto.TestRequestDto;
import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface TestActivities {
    void lookup(TestRequestDto dto);

    void validate(TestRequestDto dto);

    void notify(TestRequestDto dto, String status);
}
