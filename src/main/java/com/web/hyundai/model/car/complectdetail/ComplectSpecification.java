package com.web.hyundai.model.car.complectdetail;

import com.web.hyundai.model.car.modif.CarComplect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class ComplectSpecification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String titleGeo;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "complect_specification")
    private Set<SpecificationTopic> specificationTopics;
    @OneToOne
    private CarComplect carComplect;
}
