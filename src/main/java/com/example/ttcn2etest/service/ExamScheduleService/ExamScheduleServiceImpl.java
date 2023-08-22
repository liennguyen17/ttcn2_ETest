package com.example.ttcn2etest.service.ExamScheduleService;

import com.example.ttcn2etest.model.dto.ExamScheduleDTO;
import com.example.ttcn2etest.model.etity.ExamSchedule;
import com.example.ttcn2etest.repository.examSchedule.CustomExamScheduleRepository;
import com.example.ttcn2etest.repository.examSchedule.ExamScheduleRepository;
import com.example.ttcn2etest.request.examSchedule.CreateExamScheduleRequest;
import com.example.ttcn2etest.request.examSchedule.FilterExamScheduleRequest;
import com.example.ttcn2etest.request.examSchedule.UpdateExamScheduleRequest;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExamScheduleServiceImpl implements ExamScheduleService {
    private final ExamScheduleRepository examScheduleRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public ExamScheduleServiceImpl(ExamScheduleRepository examScheduleRepository) {
        this.examScheduleRepository = examScheduleRepository;
    }

    @Override
    public List<ExamScheduleDTO> getALLExamScheduleService() {
        return examScheduleRepository.findAll().stream().map(
                examSchedule -> modelMapper.map(examSchedule, ExamScheduleDTO.class)
        ).collect(Collectors.toList());
    }

    @Override
    public ExamScheduleDTO getByIdExamScheduleService(Long id) {
        Optional<ExamSchedule> examScheduleOptional = examScheduleRepository.findById(id);
        if (examScheduleOptional.isPresent()) {
            return modelMapper.map(examScheduleOptional.get(), ExamScheduleDTO.class);
        } else {
            throw new RuntimeException("Id không tồn tại trong hệ thống!");
        }

    }

    @Override
    public ExamScheduleDTO createExamScheduleService(CreateExamScheduleRequest request) {
        try {
            ExamSchedule examSchedule = ExamSchedule.builder()
                    .areaId(request.getAreaId())
                    .nameArea(request.getNameArea())
                    .schoolId(request.getSchoolId())
                    .nameExamSchool(request.getNameExamSchool())
                    .examTime(request.getExamTime())
                    .registrationTerm(request.getRegistrationTerm())
                    .examMethod(request.getExamMethod())
                    .examinationFee(request.getExaminationFee())
                    .examinationObject(request.getExaminationObject())
                    .examRegistrationRecords(request.getExamRegistrationRecords())
                    .certificationTime(request.getCertificationTime())
                    .createdDate(new Timestamp(System.currentTimeMillis()))
                    .updateDate(new Timestamp(System.currentTimeMillis()))
                    .build();
            examSchedule = examScheduleRepository.saveAndFlush(examSchedule);
            return modelMapper.map(examSchedule, ExamScheduleDTO.class);
        } catch (Exception ex) {
            throw new RuntimeException("Có lỗi xảy ra trong quá trình thêm mới!");
        }
    }

    @Override
    public ExamScheduleDTO updateExamScheduleService(UpdateExamScheduleRequest request, Long id) {
        Optional<ExamSchedule> examScheduleOptional = examScheduleRepository.findById(id);
        if (examScheduleOptional.isPresent()) {
            ExamSchedule examSchedule = examScheduleOptional.get();
            examSchedule.setAreaId(request.getAreaId());
            examSchedule.setNameArea(request.getNameArea());
            examSchedule.setSchoolId(request.getSchoolId());
            examSchedule.setNameExamSchool(request.getNameExamSchool());
            examSchedule.setExamTime(request.getExamTime());
            examSchedule.setRegistrationTerm(request.getRegistrationTerm());
            examSchedule.setExamMethod(request.getExamMethod());
            examSchedule.setExaminationFee(request.getExaminationFee());
            examSchedule.setExaminationObject(request.getExaminationObject());
            examSchedule.setExamRegistrationRecords(request.getExamRegistrationRecords());
            examSchedule.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            return modelMapper.map(examScheduleRepository.saveAndFlush(examSchedule), ExamScheduleDTO.class);
        }
        throw new RuntimeException("Có lỗi xảy ra trong quá trình cập nhât!");
    }

    @Override
    public ExamScheduleDTO deleteByIdExamScheduleService(Long id) {
        if (!examScheduleRepository.existsById(id)) {
            throw new RuntimeException("Id: \"+id+\" cần xóa không tồn tại trong hệ thống!");
        }
        Optional<ExamSchedule> examScheduleOptional = examScheduleRepository.findById(id);
        if (examScheduleOptional.isPresent()) {
            examScheduleRepository.deleteById(id);
            return modelMapper.map(examScheduleOptional, ExamScheduleDTO.class);
        }
        throw new RuntimeException("Có lỗi xảy ra trong quá trinh xóa!");
    }

    @Override
    public List<ExamScheduleDTO> deleteAllExamScheduleService(List<Long> ids) {
        List<ExamScheduleDTO> examScheduleDTOS = new ArrayList<>();
        for (Long id : ids) {
            Optional<ExamSchedule> examScheduleOptional = examScheduleRepository.findById(id);
            if (examScheduleOptional.isPresent()) {
                ExamSchedule examSchedule = examScheduleOptional.get();
                examScheduleDTOS.add(modelMapper.map(examSchedule, ExamScheduleDTO.class));
                examScheduleRepository.delete(examSchedule);
            } else {
                throw new RuntimeException("Có lỗi xảy ra trong quá trình xóa danh sách lịch ôn tập!");
            }
        }
        return examScheduleDTOS;
    }

    @Override
    public Page<ExamSchedule> filterExamScheduleService(FilterExamScheduleRequest request) {
        Specification<ExamSchedule> specification = CustomExamScheduleRepository.filterSpecification(request);
        Page<ExamSchedule> examSchedules = examScheduleRepository.findAll(specification, PageRequest.of(request.getStart(), request.getLimit()));
        return examSchedules;
    }
}
