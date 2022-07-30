package com.web.hyundai.model.car.complectdetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ComplectDetailDto {
    private ComplectOverview complectOverview;
    private ComplectSpecification complectSpecification;
}
