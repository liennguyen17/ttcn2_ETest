package com.example.ttcn2etest.service.user;

import com.example.ttcn2etest.model.dto.UserDTO;
import com.example.ttcn2etest.model.etity.User;
import com.example.ttcn2etest.request.user.CreateUserRequest;
import com.example.ttcn2etest.request.user.FilterUserRequest;
import com.example.ttcn2etest.request.user.UpdateUserRequest;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface UserService {
    List<UserDTO> getAll();
    UserDTO getById(Long id);
    UserDTO create(CreateUserRequest request);
    UserDTO update(UpdateUserRequest request, Long id) throws ParseException;
    UserDTO deleteById(Long id);
    List<UserDTO> deleteAllId(List<Long> ids);
    Page<User> filter(FilterUserRequest request, Date dateFrom, Date dateTo, Date dateOfBirthFrom, Date dateOfBirthTo);

}
