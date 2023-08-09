package com.example.ttcn2etest.model.dto;

import com.example.ttcn2etest.constant.DateTimeConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String userId;
    private String username;
    private String name;
    private String passwordNoEncode;
    @JsonFormat(pattern = DateTimeConstant.DATE_FORMAT, timezone = DateTimeConstant.TIME_ZONE)
    private Date dateOfBirth;
    private String phone;
    private String email;
    private String address;
    private Boolean isSuperAdmin = false;
    private String avatar;
    @JsonFormat(pattern = DateTimeConstant.DATE_TIME_FORMAT, timezone = DateTimeConstant.TIME_ZONE)
    private Timestamp createdDate;
    @JsonFormat(pattern = DateTimeConstant.DATE_TIME_FORMAT, timezone = DateTimeConstant.TIME_ZONE)
    private Timestamp updateDate;
//    private Role1DTO role;
//    private Service1DTO services;
}
