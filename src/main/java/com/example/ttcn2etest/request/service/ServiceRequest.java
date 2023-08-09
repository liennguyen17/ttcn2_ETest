package com.example.ttcn2etest.request.service;

import com.example.ttcn2etest.model.etity.Service;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServiceRequest {
    @NotBlank(message = "Tên dịch vụ không được để trống!")
    @Size(min = 6, max = 100, message = "Tên dịch vụ phải có ít nhất 6, nhiều nhất 100 kí tự!")
    private String name;
    @NotBlank(message = "Mô tả không được để trống!")
    @Size(max = 5000)
    private String description;
    @NotBlank(message = "Mục tiêu đạt được không được để trống!")
    @Size(max = 5000)
    private String studyGoals;
    @NotBlank(message = "Lịch học không được để trống!")
    @Size(max = 500)
    private String schedule;
    @NotNull(message = "Số buổi không được để trống!")
    private int numberTeachingSessions;
    @NotBlank(message = "Lộ trình đào tạo không được để trống!")
    @Size(max = 500)
    private String curriculum;
    @NotNull(message = "Hình thức học không được để trống!")
    @Enumerated(EnumType.STRING)
    private Service.Learn learnOnlineOrOffline;
    @NotNull(message = "Giá tiền không được để trống!")
    private BigDecimal coursePrice;
    @NotBlank(message = "Yều cầu đầu vào của học viên không được để trống!")
    private String requestStudents;
    @NotNull(message = "Loại dịch vụ không được để trống!")
    @Enumerated(EnumType.STRING)
    private Service.TypeService typeOfService;
}
