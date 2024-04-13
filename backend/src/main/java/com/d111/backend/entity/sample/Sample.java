package com.d111.backend.entity.sample;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sample {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "sample_column")
    String sampleColumn;

}
