package com.example.ttcn2etest.model.dto;

import com.example.ttcn2etest.model.etity.Service;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDTO {
    private Long id;
    private String name;
    private String description;
    private String studyGoals;
    private String schedule;
    private int numberTeachingSessions;
    private String curriculum;
    private Service.Learn learnOnlineOrOffline;
    private BigDecimal coursePrice;
    private String requestStudents;
    private Service.TypeService typeOfService;
    private Timestamp createdDate;
    private Timestamp updateDate;

}