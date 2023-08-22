package com.example.ttcn2etest.request.examSchedule;

import com.example.ttcn2etest.model.etity.ExamSchedule;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateExamScheduleRequest {
    @NotNull(message = "Mã khu vực không được để trống!")
    @Enumerated(EnumType.STRING)
    private ExamSchedule.Area areaId;
    @NotBlank(message = "Tên khu vực không được để trống!")
    @Size(max = 100)
    private String nameArea;
    @NotBlank(message = "Mã trường không được để trống!")
    private String schoolId;
    @NotBlank(message = "Tên trường không được để trống!")
    private String nameExamSchool;
    @NotBlank(message = "Thời gian thi không được để trống!")
    private String examTime;
    @NotBlank(message = "Hạn đăng ký thi không được để trống!")
    private String registrationTerm;
    @NotBlank(message = "Hình thức thi không được để trống!")
    private String examMethod;
    @NotBlank(message = "Đối tượng thi không được để trống!")
    private String examinationObject;
    @NotBlank(message = "Lệ phí thi không được để trống!")
    private String examinationFee;
    @NotBlank(message = "Hồ sơ đăng ký thi không được để trống!")
    private String examRegistrationRecords;
    @NotBlank(message = "Thời gian cấp chứng nhận không được để trống!")
    private String certificationTime;
}
