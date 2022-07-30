package com.web.hyundai.model.car.complectdetail;

import com.web.hyundai.model.car.modif.CarComplect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class ComplectOverview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String field;
    private String fieldGeo;
    private String value;
    private String valueGeo;
    @OneToOne
    private CarComplect carComplect;
}
