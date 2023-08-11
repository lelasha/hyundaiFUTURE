package com.web.hyundai.repo.car.modif;

import com.web.hyundai.model.car.complectcompare.ComplectCompare;
import com.web.hyundai.model.car.complectcompare.ComplectCompareFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ComplectCompareFeatureRepo extends JpaRepository<ComplectCompareFeature, Long> {

    @Query(value = "delete from complect_compare_feature where engine_id = :engineId", nativeQuery = true)
    @Modifying
    void deleteByEngineId(@Param("engineId") Long engineId);
}
