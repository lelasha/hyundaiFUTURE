package com.web.hyundai.model.car.complectcompare.dto;

import com.web.hyundai.model.car.complectcompare.ComplectCompareFeature;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplectCompareTitleDTO {
    private Long id;
    private String title;
    private int orderId;
    private String locale;
    private List<ComplectCompareFeatureDTO> complectCompareFeatures = new ArrayList<>();
}
