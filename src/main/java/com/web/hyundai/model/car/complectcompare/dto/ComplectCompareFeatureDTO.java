package com.web.hyundai.model.car.complectcompare.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplectCompareFeatureDTO {
    private Long id;
    private String value;
    @JsonProperty("complectId")
    private Long engineId;
}
