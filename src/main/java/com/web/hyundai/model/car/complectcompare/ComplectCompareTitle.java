package com.web.hyundai.model.car.complectcompare;

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
public class ComplectCompareTitle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private int orderId;
    private String locale;
    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "compare_title_id")
    private List<ComplectCompareFeature> complectCompareFeatures = new ArrayList<>();
}
