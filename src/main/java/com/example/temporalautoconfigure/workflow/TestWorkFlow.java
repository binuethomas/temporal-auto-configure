package com.example.temporalautoconfigure.workflow;

import com.example.temporalautoconfigure.dto.TestRequestDto;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

//Workflows encapsulate execution/orchestration of Tasks which include Activities and child Workflows
@WorkflowInterface
public interface TestWorkFlow {
    @WorkflowMethod
    String execute(TestRequestDto dto);


}
