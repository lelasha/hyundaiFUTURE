package com.web.hyundai.repo.home;

import com.web.hyundai.model.home.HomeCarBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HomeCarBannerRepo extends JpaRepository<HomeCarBanner, Long> {
    @Query(nativeQuery = true,
            value = "select * from home_car_banner where vehicle_type=:type and visibility=1")
    HomeCarBanner findByVehicleType(@Param("type") String type);
}
