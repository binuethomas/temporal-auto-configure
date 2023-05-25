package com.example.temporalautoconfigure.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TestRequestDto {

    private long id;
    private int typeId;
    private long createTime;
    private String tracingUUID;
}
