package com.web.hyundai.repo.car.modif;

import com.web.hyundai.model.car.complectcompare.ComplectCompare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ComplectCompareRepo extends JpaRepository<ComplectCompare, Long> {

    @Query(value = "select * from complect_compare where car_id = :carId", nativeQuery = true)
    Optional<ComplectCompare> findByCarId(@Param("carId") Long carId);


}
