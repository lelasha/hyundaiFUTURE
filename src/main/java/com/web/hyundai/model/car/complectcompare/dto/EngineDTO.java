package com.web.hyundai.model.car.complectcompare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EngineDTO {
    private Long id;
    private String price;
    private String image;
    private String name;
}
