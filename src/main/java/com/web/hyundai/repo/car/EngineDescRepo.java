package com.web.hyundai.repo.car;

import com.web.hyundai.model.car.EngineDesc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EngineDescRepo extends JpaRepository<EngineDesc,Long> {
    @Query(value = "select * from engine_desc  where engine_id=:id",nativeQuery = true)
    List<EngineDesc> findAllByEngineId(@Param("id") Long id);

    @Query(value = "select icon_id from engine_desc  where id=:id",nativeQuery = true)
    Long findIconId(@Param("id") Long id);
}
