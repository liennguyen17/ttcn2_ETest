package com.example.ttcn2etest.service.ExamScheduleService;

import com.example.ttcn2etest.model.dto.ExamScheduleDTO;
import com.example.ttcn2etest.model.etity.ExamSchedule;
import com.example.ttcn2etest.request.examSchedule.CreateExamScheduleRequest;
import com.example.ttcn2etest.request.examSchedule.FilterExamScheduleRequest;
import com.example.ttcn2etest.request.examSchedule.UpdateExamScheduleRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ExamScheduleService {
    List<ExamScheduleDTO> getALLExamScheduleService();

    ExamScheduleDTO getByIdExamScheduleService(Long id);

    ExamScheduleDTO createExamScheduleService(CreateExamScheduleRequest request);

    ExamScheduleDTO updateExamScheduleService(UpdateExamScheduleRequest request, Long id);

    ExamScheduleDTO deleteByIdExamScheduleService(Long id);

    List<ExamScheduleDTO> deleteAllExamScheduleService(List<Long> ids);

    Page<ExamSchedule> filterExamScheduleService(FilterExamScheduleRequest request);

}
