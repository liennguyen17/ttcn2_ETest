package com.example.ttcn2etest.model.etity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "exam_schedule")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "area_id")
    @Enumerated(EnumType.STRING)
    private Area areaId;
    @Column(name = "area_name")
    @Size(max = 100)
    private String nameArea;
    @Column(name = "school_id")
    private String schoolId;
    @Column(name = "name_exam_school")
    private String nameExamSchool;
    @Column(name = "exam_time")
    private String examTime;
    @Column(name = "registration_term")
    private String registrationTerm;          //han dki thi
    @Column(name = "exam_method")
    private String examMethod;
    @Column(name = "examination_object")
    private String examinationObject;
    @Column(name = "examination_fee")
    private String examinationFee;
    @Column(name = "exam_registration_records")
    private String examRegistrationRecords;
    @Column(name = "certification_time")
    private String certificationTime;
    @Column(name = "created_date")
    private Timestamp createdDate;
    @Column(name = "update_date")
    private Timestamp updateDate;

    public enum Area {
        BAC, TRUNG, NAM
    }
}
