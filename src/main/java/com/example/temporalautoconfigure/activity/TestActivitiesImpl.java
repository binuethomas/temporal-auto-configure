package com.example.temporalautoconfigure.activity;


import com.example.temporalautoconfigure.common.QueueNames;
import com.example.temporalautoconfigure.dto.TestRequestDto;
import io.temporal.spring.boot.ActivityImpl;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

@Component
@ActivityImpl(taskQueues = QueueNames.TEST_QUEUE)
@CommonsLog
public class TestActivitiesImpl implements TestActivities {
    @Override
    public void lookup(TestRequestDto dto) {
        log.info("In Lookup");
    }

    @Override
    public void validate(TestRequestDto dto) {
        log.info("In Validate");

    }

    @Override
    public void notify(TestRequestDto dto, String status) {
        log.info("In Notify with status : "+ status);

    }
}
