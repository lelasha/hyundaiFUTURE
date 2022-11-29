package com.web.hyundai.model.car.complectcompare.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.hyundai.model.car.complectcompare.ComplectCompare;
import com.web.hyundai.model.car.complectcompare.ComplectCompareTitle;
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
public class ComplectCompareDTO {
    private Long id;
    private String image1;
    private String image2;
    private Long carId;
    private String pdf;
    private List<ComplectCompareTitleDTO> compareTitles = new ArrayList<>();
    @JsonProperty("complectDTO")
    private List<EngineDTO> engineDTO = new ArrayList<>();


    public static ComplectCompareDTO from(ComplectCompare complectCompare, String file) {
        List<ComplectCompareTitleDTO> titlesDTos = fromTitle(complectCompare);

        return ComplectCompareDTO.builder()
                .id(complectCompare.getId())
                .image1(complectCompare.getImage1())
                .image2(complectCompare.getImage2())
                .carId(complectCompare.getCar().getId())
                .compareTitles(titlesDTos)
                .pdf(file)
                .build();
    }

    private static List<ComplectCompareTitleDTO> fromTitle(ComplectCompare complectCompare) {
        List<ComplectCompareTitle> titles = complectCompare.getComplectCompareTitles();
        List<ComplectCompareTitleDTO> titlesDTos = new ArrayList<>();

        titles.forEach(t -> {
            List<ComplectCompareFeatureDTO> featureDTOS = new ArrayList<>();

            t.getComplectCompareFeatures().forEach(f -> {

                ComplectCompareFeatureDTO.ComplectCompareFeatureDTOBuilder build =
                        ComplectCompareFeatureDTO.builder()
                                .value(f.getValue())
                                .id(f.getId());
                if (null != f.getEngine()) build.engineId(f.getEngine().getId());
                featureDTOS.add(build.build());
            });

            titlesDTos.add(
                    ComplectCompareTitleDTO.builder()
                            .id(t.getId())
                            .locale(t.getLocale())
                            .orderId(t.getOrderId())
                            .title(t.getTitle())
                            .complectCompareFeatures(featureDTOS)
                            .build()
            );

        });
        return titlesDTos;
    }

    public static List<ComplectCompareDTO> from(List<ComplectCompare> all) {
        ArrayList<ComplectCompareDTO> list = new ArrayList<>();
        all.forEach(c -> list.add(ComplectCompareDTO.builder()
                .id(c.getId())
                .image1(c.getImage1())
                .image2(c.getImage2())
                .carId(c.getCar().getId())
                .compareTitles(fromTitle(c))
                .build()));
        return list;
    }
}
