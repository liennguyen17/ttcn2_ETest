package com.example.ttcn2etest.controller;

import com.example.ttcn2etest.constant.DateTimeConstant;
import com.example.ttcn2etest.model.dto.DocumentDTO;
import com.example.ttcn2etest.model.etity.Document;
import com.example.ttcn2etest.request.document.CreateDocumentRequest;
import com.example.ttcn2etest.request.document.FilterDocumentRequest;
import com.example.ttcn2etest.request.document.UpdateDocumentRequest;
import com.example.ttcn2etest.service.document.DocumentService;
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
@RequestMapping("/document")
public class DocumentController extends BaseController {
    private final DocumentService documentService;

    private final ModelMapper modelMapper = new ModelMapper();

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/all")
    ResponseEntity<?> getAllDocument() {
        try {
            List<DocumentDTO> response = documentService.getAllDocument();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getById(@PathVariable Long id) {
        DocumentDTO response = documentService.getByIdDocument(id);
        return buildItemResponse(response);
    }

    @PostMapping("")
    ResponseEntity<?> createDocument(@Validated @RequestBody CreateDocumentRequest request) {
        DocumentDTO response = documentService.createDocument(request);
        return buildItemResponse(response);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateService(@Validated @RequestBody UpdateDocumentRequest request,
                                    @PathVariable("id") Long id) {
        DocumentDTO response = documentService.updateDocument(request, id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable Long id) {
        DocumentDTO response = documentService.deleteByIdDocument(id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/delete/all1")
    ResponseEntity<?> deleteAllId(@RequestBody List<Long> ids) {
        try {
            List<DocumentDTO> response = documentService.deleteAllIdDoc(ids);
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @DeleteMapping("/delete/all")
    ResponseEntity<?> deleteAllIdDocument(@RequestBody List<Long> ids) {
        try {
            List<DocumentDTO> response = documentService.deleteAllIdDocument(ids);
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filter(@Validated @RequestBody FilterDocumentRequest request) throws ParseException {
        Page<Document> documentPage = documentService.filterDocument(
                request,
                !Strings.isEmpty(request.getDateFrom()) ? MyUtils.convertDateFromString(request.getDateFrom(), DateTimeConstant.DATE_FORMAT) : null,
                !Strings.isEmpty(request.getDateTo()) ? MyUtils.convertDateFromString(request.getDateTo(), DateTimeConstant.DATE_FORMAT) : null);
        List<DocumentDTO> documentDTOS = documentPage.getContent().stream().map(
                document -> modelMapper.map(document, DocumentDTO.class)
        ).collect(Collectors.toList());
        return buildListItemResponse(documentDTOS, documentPage.getTotalElements());
    }
}
