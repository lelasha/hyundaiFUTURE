package com.web.hyundai.model.car.complectdetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ComplectOverviewRepo extends JpaRepository<ComplectOverview,Long> {
    @Query(value = "select * from COMPLECT_OVERVIEW where car_complect_id = :id",nativeQuery = true)
    Optional<ComplectOverview> findByComplectId(@Param("id")Long id);
}
