package com.web.hyundai.model.car.complectcompare;

import com.web.hyundai.model.car.Car;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "complect_compare ", uniqueConstraints = {
        @UniqueConstraint(name = "compareConst", columnNames = {"car_id"})
})
@ToString
public class ComplectCompare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image1;
    private String image2;
    @OneToOne
    private Car car;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "compare_id")
    private List<ComplectCompareFeature> complectCompareFeatureList = new ArrayList<>();

}
