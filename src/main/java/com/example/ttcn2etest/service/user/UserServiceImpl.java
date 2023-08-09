package com.example.ttcn2etest.service.user;

import com.example.ttcn2etest.constant.DateTimeConstant;
import com.example.ttcn2etest.exception.UsernameAlreadyExistsException;
import com.example.ttcn2etest.model.dto.UserDTO;
import com.example.ttcn2etest.model.etity.User;
import com.example.ttcn2etest.repository.user.CustomUserRepository;
import com.example.ttcn2etest.repository.user.UserRepository;
import com.example.ttcn2etest.request.user.CreateUserRequest;
import com.example.ttcn2etest.request.user.FilterUserRequest;
import com.example.ttcn2etest.request.user.UpdateUserRequest;
import com.example.ttcn2etest.utils.MyUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
//    private final PasswordEncoder encoder;

//    public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder) {
//        this.userRepository = userRepository;
//        this.encoder = encoder;
//    }

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream().map(
                user -> modelMapper.map(user, UserDTO.class)
        ).collect(Collectors.toList());
    }

    @Override
    public UserDTO getById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return modelMapper.map(user.get(), UserDTO.class);
        } else {
            throw new RuntimeException("ID người dùng không tồn tại trong hệ thống!");
        }
    }

    @Override
    @Transactional
    public UserDTO create(CreateUserRequest request) {
        try {
            if (userRepository.existsByUsername(request.getUsername())){
                throw new UsernameAlreadyExistsException("Tên người dùng đã tồn tại, nhập lại username khác!");
            }
            User user = User.builder()
                    .name(request.getName())
                    .username(request.getUsername())
                    .dateOfBirth(MyUtils.convertDateFromString(request.getDateOfBirth(), DateTimeConstant.DATE_FORMAT))
                    .email(request.getEmail())
//                    .password(encoder.encode(request.getPassword()))
                    .password(request.getPassword())
                    .passwordNoEncode(request.getPassword())
                    .address(request.getAddress())
                    .isSuperAdmin(false)
                    .phone(request.getPhone())
                    .avatar(request.getAvatar())
                    .createdDate(new Timestamp(System.currentTimeMillis()))
                    .updateDate(new Timestamp(System.currentTimeMillis()))
                    .build();
            user = userRepository.saveAndFlush(user);
            return modelMapper.map(user, UserDTO.class);
        }catch (UsernameAlreadyExistsException ex){
            throw ex;
        }catch (Exception ex) {
            throw new RuntimeException("Có lỗi cảy ra trong quá tình tọa người dùng mới!");
        }
    }

    @Override
    @Transactional
    public UserDTO update(UpdateUserRequest request, Long id) throws ParseException {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(request.getUsername());
            user.setName(request.getName());
            user.setDateOfBirth(MyUtils.convertDateFromString(request.getDateOfBirth(), DateTimeConstant.DATE_FORMAT));
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
//            user.setPassword(encoder.encode(request.getPassword()));
            user.setPasswordNoEncode(request.getPassword());
            user.setPhone(request.getPhone());
            user.setAvatar(request.getAvatar());
            user.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            return modelMapper.map(userRepository.save(user), UserDTO.class);
        }
        throw new RuntimeException("Có lỗi xảy ra trong quá trình cập nhật thông tin người dùng!");
    }

    @Override
    @Transactional
    public UserDTO deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Người dùng có id:" + id + "cần xóa không tồn tại trong hệ thống!");
        }
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return modelMapper.map(userOptional, UserDTO.class);
        }
        throw new RuntimeException("Có lỗi xảy ra trong quá trình xóa người dùng!");
    }

    @Override
    @Transactional
    public List<UserDTO> deleteAllId(List<Long> ids) {
        List<UserDTO> userDTOS = new ArrayList<>();
        for(User user: userRepository.findAllById(ids)){
            userDTOS.add(modelMapper.map(user, UserDTO.class));
        }
        userRepository.deleteAllByIdInBatch(ids);
        return userDTOS;
    }

    @Override
    public Page<User> filter(FilterUserRequest request, Date dateFrom, Date dateTo, Date dateOfBirthFrom, Date dateOfBirthTo) {
        Specification<User> specification = CustomUserRepository.filterSpecification(dateFrom, dateTo, dateOfBirthFrom,dateOfBirthTo, request);
        Page<User> userPage = userRepository.findAll(specification, PageRequest.of(request.getStart(), request.getLimit()));
        return userPage;
    }
}
