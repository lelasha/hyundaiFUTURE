package com.web.hyundai.model.home;

import com.web.hyundai.model.car.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeCarBannerDto {
    private Long id;
    @NotEmpty
    private VehicleType vehicleType;
    @NotEmpty
    private String link;
    @NotEmpty
    private boolean visibility;
}