package com.example.ttcn2etest.model.etity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;

import java.sql.Timestamp;

@Entity
@Table(name = "slide")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Slide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 500)
    private String image;
    @Size(max = 200)
    private String location;

    @Column(name = "created_date")
    private Timestamp createdDate;
    @Column(name = "update_date")
    private Timestamp updateDate;

}
