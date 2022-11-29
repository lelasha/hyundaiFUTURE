package com.web.hyundai.model.car.complectcompare;

import com.web.hyundai.model.car.Engine;
import com.web.hyundai.model.car.modif.CarComplect;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ComplectCompareFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String value;
    @OneToOne
    private Engine engine;
}
