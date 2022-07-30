package com.web.hyundai.model.home;

import com.web.hyundai.model.car.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeCarBanner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
    private String link;
    private String image;
    private boolean visibility;

    public static HomeCarBanner from(HomeCarBannerDto homeCarBannerDto){
        return HomeCarBanner.builder()
                .link(homeCarBannerDto.getLink())
                .vehicleType(homeCarBannerDto.getVehicleType())
                .visibility(homeCarBannerDto.isVisibility())
                .build();
    }
}
