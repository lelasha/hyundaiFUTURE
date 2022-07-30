package com.web.hyundai.model.car;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Data
@NoArgsConstructor
@Entity
public class EngineDesc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nameGEO;
    @Enumerated
    private EngineDescLogo engineDescLogo;
    @ManyToOne
    @JoinColumn(name="icon_id")
    private EngineDescIcon engineDescIcon;
    @ManyToOne
    @JoinColumn(name = "engine_id ")
    @JsonIgnore
    private Engine engine;

    public EngineDesc(String name, String nameGEO, Engine engine) {
        this.name = name;
        this.nameGEO = nameGEO;
        this.engine = engine;
    }
}
