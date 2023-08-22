package com.example.ttcn2etest.controller;

import com.example.ttcn2etest.model.dto.ExamScheduleDTO;
import com.example.ttcn2etest.model.etity.ExamSchedule;
import com.example.ttcn2etest.request.examSchedule.CreateExamScheduleRequest;
import com.example.ttcn2etest.request.examSchedule.FilterExamScheduleRequest;
import com.example.ttcn2etest.request.examSchedule.UpdateExamScheduleRequest;
import com.example.ttcn2etest.service.ExamScheduleService.ExamScheduleService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/exam/schedule")
public class ExamScheduleController extends BaseController {
    private final ExamScheduleService examScheduleService;
    private final ModelMapper modelMapper = new ModelMapper();

    public ExamScheduleController(ExamScheduleService examScheduleService) {
        this.examScheduleService = examScheduleService;
    }

    @GetMapping("/all")
    ResponseEntity<?> getALLExamSchedule(){
        try {
            List<ExamScheduleDTO> response = examScheduleService.getALLExamScheduleService();
            return buildListItemResponse(response, response.size());
        }catch (Exception ex){
            return buildResponse();
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getByIdExamSchedule(@PathVariable Long id){
        ExamScheduleDTO response = examScheduleService.getByIdExamScheduleService(id);
        return buildItemResponse(response);
    }

    @PostMapping("")
    ResponseEntity<?> createExamSchedule(@Validated @RequestBody CreateExamScheduleRequest request){
        ExamScheduleDTO response = examScheduleService.createExamScheduleService(request);
        return buildItemResponse(response);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateExamSchedule(@Validated @RequestBody UpdateExamScheduleRequest request,
                                                @PathVariable("id") Long id){
        ExamScheduleDTO response = examScheduleService.updateExamScheduleService(request, id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteByIdExamSchedule(@PathVariable Long id){
        ExamScheduleDTO response = examScheduleService.deleteByIdExamScheduleService(id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/delete/all")
    ResponseEntity<?> deleteAllExamSchedule(@RequestBody List<Long> ids){
        try{
            List<ExamScheduleDTO> response = examScheduleService.deleteAllExamScheduleService(ids);
            return buildListItemResponse(response, response.size());
        }catch (Exception ex){
            return buildResponse();
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterExamScheduleService(@Validated @RequestBody FilterExamScheduleRequest request){
        Page<ExamSchedule> examSchedulePage = examScheduleService.filterExamScheduleService(request);
            List<ExamScheduleDTO> examScheduleDTOS = examSchedulePage.getContent().stream().map(
                    examSchedule -> modelMapper.map(examSchedule, ExamScheduleDTO.class)
            ).collect(Collectors.toList());
            return buildListItemResponse(examScheduleDTOS, examSchedulePage.getTotalElements());
    }

}
