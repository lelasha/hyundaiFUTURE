package com.web.hyundai.repo.car.modif;

import com.web.hyundai.model.car.complectcompare.ComplectCompare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComplectCompareRepo extends JpaRepository<ComplectCompare, Long> {
    Optional<ComplectCompare> findByCarIdAndComplectCompareFeatureList_LocaleOrderByComplectCompareFeatureList_OrderId(Long carId,String locale);
}
