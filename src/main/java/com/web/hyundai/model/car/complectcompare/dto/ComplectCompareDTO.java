package com.web.hyundai.model.car.complectcompare.dto;

import com.web.hyundai.model.car.complectcompare.ComplectCompare;
import com.web.hyundai.model.car.complectcompare.ComplectCompareFeature;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Transactional
public class ComplectCompareDTO {
    private Long id;
    private String image1;
    private String image2;
    private Long carId;
    private List<ComplectCompareFeatureDTO> complectCompareFeatureList = new ArrayList<>();


    public static ComplectCompareDTO from(ComplectCompare complectCompare) {
        System.out.println("FEATURE? ++ " + complectCompare.getComplectCompareFeatureList());
        List<ComplectCompareFeature> feature = complectCompare.getComplectCompareFeatureList();
        List<ComplectCompareFeatureDTO> featureDTO = fromFeature(feature);
        return ComplectCompareDTO.builder()
                .id(complectCompare.getId())
                .image1(complectCompare.getImage1())
                .image2(complectCompare.getImage2())
                .carId(complectCompare.getId())
                .complectCompareFeatureList(featureDTO)
                .build();
    }

    public static List<ComplectCompareDTO> from(List<ComplectCompare> complectCompare) {
        List<ComplectCompareDTO> newList = new ArrayList<>();
        complectCompare.forEach(each -> {
            List<ComplectCompareFeature> feature = each.getComplectCompareFeatureList();
            List<ComplectCompareFeatureDTO> featureDTO = fromFeature(feature);
            newList.add(ComplectCompareDTO.builder()
                    .id(each.getId())
                    .image1(each.getImage1())
                    .image2(each.getImage2())
                    .carId(each.getId())
                    .complectCompareFeatureList(featureDTO)
                    .build());
        });
        return newList;
    }


    public static List<ComplectCompareFeatureDTO> fromFeature(List<ComplectCompareFeature> feature) {
        List<ComplectCompareFeatureDTO> featureDTO = new ArrayList<>();
        if (feature != null) {
            feature.forEach(c -> featureDTO.add(ComplectCompareFeatureDTO.builder()
                    .id(c.getId())
                    .title(c.getTitle())
                    .value(c.getValue())
                    .orderId(c.getOrderId())
                    .build()));
        }
        return featureDTO;
    }

    public static ComplectCompareFeatureDTO from(ComplectCompareFeature c) {
        return ComplectCompareFeatureDTO.builder()
                .id(c.getId())
                .title(c.getTitle())
                .value(c.getValue())
                .orderId(c.getOrderId())
                .build();
    }
}
