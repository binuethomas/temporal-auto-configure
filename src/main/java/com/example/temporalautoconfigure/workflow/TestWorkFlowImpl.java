package com.example.temporalautoconfigure.workflow;

import com.example.temporalautoconfigure.activity.TestActivitiesImpl;
import com.example.temporalautoconfigure.common.QueueNames;
import com.example.temporalautoconfigure.dto.TestRequestDto;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import lombok.extern.apachecommons.CommonsLog;
import org.slf4j.MDC;

import java.time.Duration;



@CommonsLog
@WorkflowImpl(taskQueues = QueueNames.TEST_QUEUE)
public class TestWorkFlowImpl implements TestWorkFlow {
    private int maxAttempts = 3;

    private final ActivityOptions activity1Options =
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(120))
                    .setRetryOptions(RetryOptions.newBuilder()
                        .setMaximumAttempts(1) // no activity retries
                        .build())
                .build();
    private final ActivityOptions activity2Options =
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(120))
                    .setRetryOptions(
                            RetryOptions.newBuilder()
                                    .setMaximumAttempts(maxAttempts)
                                    .setInitialInterval(Duration.ofMinutes(1))
                                    .setBackoffCoefficient(1)
                                    .build())
                    .build();

    private final ActivityOptions activity3Options =
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(120))
                    .setRetryOptions(
                            RetryOptions.newBuilder()
                                    .setMaximumAttempts(maxAttempts)
                                    .setInitialInterval(Duration.ofSeconds(15))
                                    .setBackoffCoefficient(2)
                                    .build())
                    .build();
    private final TestActivitiesImpl activity1 =
            Workflow.newActivityStub(TestActivitiesImpl.class, activity1Options);
    private final TestActivitiesImpl activity2 =
            Workflow.newActivityStub(TestActivitiesImpl.class, activity2Options);

    private final TestActivitiesImpl activity3 =
            Workflow.newActivityStub(TestActivitiesImpl.class, activity3Options);


    @Override
    public String execute(TestRequestDto dto) {

        log.info("Starting Workflow for dsId: " + dto.getId() );

        boolean wasSuccess = true;
        try {
            activity1.lookup(dto);
            Workflow.sleep(10000);
            activity2.validate(dto);
        }  catch (ActivityFailure ex) {
            wasSuccess = false;
            log.error("Activity exception before callback" + dto);
            try {
                activity3.notify(dto, "Failure"); //retry every 15 seconds for 3 times
                log.info("Notifying UM of Failure" +  dto);
                return "WORKFLOW COMPLETED";
            }  catch (ActivityFailure exception) {
                log.error("Activity exception during UM callback. Lock failed"+ dto );
            } finally {
                MDC.clear();
            }
        }

        if (wasSuccess) {
            try {
                activity3.notify(dto, "Success"); //retry every 15 seconds for 3 times
                log.info("WorkflowSuccess  for dsId: " + dto);
                return "WORKFLOW COMPLETED";
            } catch (ActivityFailure ex) {
                log.error("Activity exception during UM callback. Lock success" + dto);
            }
        }

        return "WORKFLOW COMPLETED";
    }
}

