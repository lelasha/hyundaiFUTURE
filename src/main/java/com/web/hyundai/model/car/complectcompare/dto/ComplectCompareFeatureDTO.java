package com.web.hyundai.model.car.complectcompare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplectCompareFeatureDTO {
    private Long id;
    private String title;
    private String value;
    private int orderId;
}
