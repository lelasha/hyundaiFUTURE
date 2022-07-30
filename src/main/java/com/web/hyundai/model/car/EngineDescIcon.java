package com.web.hyundai.model.car;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class EngineDescIcon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "icon_id")
    @JsonIgnore
    private List<EngineDesc> engineDesc;

}
