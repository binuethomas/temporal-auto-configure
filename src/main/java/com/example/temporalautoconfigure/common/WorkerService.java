package com.example.temporalautoconfigure.common;

import com.example.temporalautoconfigure.dto.TestRequestDto;
import com.example.temporalautoconfigure.workflow.TestWorkFlow;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
public class WorkerService {

    private WorkflowClient workflowClient;

    public WorkerService(WorkflowClient workflowClient) {
        this.workflowClient = workflowClient;
    }

    public String execute(TestRequest request) {
        log.info("workflow start request received for request :" + request);

        TestRequestDto dto = TestRequestDto.builder()
                .id(request.getId())
                .typeId(22)
                .createTime(System.currentTimeMillis())
                .build();

        TestWorkFlow testWorkFlow = workflowClient.newWorkflowStub(
                TestWorkFlow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowId("WORKFLOW_" + dto.getCreateTime())
                        .setTaskQueue(QueueNames.TEST_QUEUE)
                        .build());

        WorkflowExecution start = WorkflowClient.start(testWorkFlow::execute, dto);
        log.info("workflow started with workflowId :" + start.getWorkflowId());

        return "FINISHED";
    }
}
