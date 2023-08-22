package com.example.ttcn2etest.controller;

import com.example.ttcn2etest.constant.DateTimeConstant;
import com.example.ttcn2etest.model.dto.ConsultingRegistrationDTO;
import com.example.ttcn2etest.model.etity.ConsultingRegistration;
import com.example.ttcn2etest.request.consultingRegistration.CreateConsultingRegistrationRequest;
import com.example.ttcn2etest.request.consultingRegistration.FilterConsultingRegistrationRequest;
import com.example.ttcn2etest.request.consultingRegistration.UpdateConsultingRegistrationRequest;
import com.example.ttcn2etest.service.consultingRegistration.ConsultingRegistrationService;
import com.example.ttcn2etest.utils.MyUtils;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/consulting/registration")
public class ConsultingRegistrationController extends BaseController {
    private final ConsultingRegistrationService consultingRegistrationService;
    private final ModelMapper modelMapper = new ModelMapper();

    public ConsultingRegistrationController(ConsultingRegistrationService consultingRegistrationService) {
        this.consultingRegistrationService = consultingRegistrationService;
    }

    @GetMapping("/all")
    ResponseEntity<?> getAllConsultingRegistration() {
        try {
            List<ConsultingRegistrationDTO> response = consultingRegistrationService.getAllConsultingRegistration();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getByIdConsultingRegistration(@PathVariable Long id) {
        ConsultingRegistrationDTO response = consultingRegistrationService.getByIdConsultingRegistration(id);
        return buildItemResponse(response);
    }

    @PostMapping("")
    ResponseEntity<?> createConsultingRegistration(@Validated @RequestBody CreateConsultingRegistrationRequest request) {
        ConsultingRegistrationDTO response = consultingRegistrationService.createConsultingRegistration(request);
        return buildItemResponse(response);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateConsultingRegistration(@Validated @RequestBody UpdateConsultingRegistrationRequest request,
                                                   @PathVariable("id") Long id) {
        ConsultingRegistrationDTO response = consultingRegistrationService.updateConsultingRegistration(request, id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteByIdConsultingRegistration(@PathVariable Long id) {
        ConsultingRegistrationDTO response = consultingRegistrationService.deleteByIdConsultingRegistration(id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/delete/all")
    ResponseEntity<?> deleteAllConsultingRegistration(@RequestBody List<Long> ids) {
        try {
            List<ConsultingRegistrationDTO> response = consultingRegistrationService.deleteAllConsultingRegistration(ids);
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterConsultingRegistration(@Validated @RequestBody FilterConsultingRegistrationRequest request) throws ParseException {
        Page<ConsultingRegistration> consultingRegistrationPage = consultingRegistrationService.filterConsultingRegistration(
                request,
                !Strings.isEmpty(request.getDateFrom()) ? MyUtils.convertDateFromString(request.getDateFrom(), DateTimeConstant.DATE_FORMAT) : null,
                !Strings.isEmpty(request.getDateTo()) ? MyUtils.convertDateFromString(request.getDateTo(), DateTimeConstant.DATE_FORMAT) : null);
        List<ConsultingRegistrationDTO> consultingRegistrationDTOS = consultingRegistrationPage.getContent().stream().map(
                consultingRegistration -> modelMapper.map(consultingRegistration, ConsultingRegistrationDTO.class)
        ).collect(Collectors.toList());
        return buildListItemResponse(consultingRegistrationDTOS, consultingRegistrationPage.getTotalElements());
    }
}
