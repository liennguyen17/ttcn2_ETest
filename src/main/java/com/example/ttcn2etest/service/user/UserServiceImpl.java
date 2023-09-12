package com.example.ttcn2etest.service.user;

import com.example.ttcn2etest.constant.DateTimeConstant;
import com.example.ttcn2etest.exception.UsernameAlreadyExistsException;
import com.example.ttcn2etest.model.dto.UserDTO;
import com.example.ttcn2etest.model.etity.Role;
import com.example.ttcn2etest.model.etity.User;
import com.example.ttcn2etest.repository.role.RoleRepository;
import com.example.ttcn2etest.repository.service.ServiceRepository;
import com.example.ttcn2etest.repository.user.CustomUserRepository;
import com.example.ttcn2etest.repository.user.UserRepository;
import com.example.ttcn2etest.request.user.CreateUserRequest;
import com.example.ttcn2etest.request.user.FilterUserRequest;
import com.example.ttcn2etest.request.user.UpdateUserRequest;
import com.example.ttcn2etest.utils.MyUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ServiceRepository serviceRepository;

    private final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ServiceRepository serviceRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.serviceRepository = serviceRepository;
    }


    @Override
    public List<UserDTO> getAllUser() {
        return userRepository.findAll().stream().map(
                user -> modelMapper.map(user, UserDTO.class)
        ).toList();
    }

    @Override
    public UserDTO getByIdUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return modelMapper.map(user.get(), UserDTO.class);
        } else {
            throw new RuntimeException("ID người dùng không tồn tại trong hệ thống!");
        }
    }

    @Override
    @Transactional
    public UserDTO createUser(CreateUserRequest request) {
        try {
            checkServiceIsValid(request.getServices());
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new UsernameAlreadyExistsException("Tên người dùng đã tồn tại, nhập lại username khác!");
            }
            checkRoleIsValid(request.getRoleId());
            Optional<Role> roleOptional = roleRepository.findById(request.getRoleId());
            User user = User.builder()
                    .name(request.getName())
                    .username(request.getUsername())
                    .dateOfBirth(MyUtils.convertDateFromString(request.getDateOfBirth(), DateTimeConstant.DATE_FORMAT))
                    .email(request.getEmail())
                    .isVerified(request.isVerified())
//                    .password(encoder.encode(request.getPassword()))
                    .password(request.getPassword())
                    .address(request.getAddress())
                    .isSuperAdmin(false)
                    .phone(request.getPhone())
                    .avatar(request.getAvatar())
                    .createdDate(new Timestamp(System.currentTimeMillis()))
                    .updateDate(new Timestamp(System.currentTimeMillis()))
                    .build();
            List<com.example.ttcn2etest.model.etity.Service> services = buildService(request.getServices());
            user.setServices(services);
            user.setRole(buildRole(roleOptional.get().getRoleId()));

//            user = userRepository.saveAndFlush(user);
            return modelMapper.map(userRepository.save(user), UserDTO.class);
        } catch (UsernameAlreadyExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Có lỗi xảy ra trong quá trình tọa người dùng mới!");
        }
    }

    @Override
    @Transactional
    public UserDTO updateUser(UpdateUserRequest request, Long id) throws ParseException {
        checkServiceIsValid(request.getServices());
        checkRoleIsValid(request.getRoleId());
        Optional<Role> roleOptional = roleRepository.findById(request.getRoleId());
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            List<com.example.ttcn2etest.model.etity.Service> services = buildService(request.getServices());
            User user = userOptional.get();
            user.setUsername(request.getUsername());
            user.setName(request.getName());
            user.setDateOfBirth(MyUtils.convertDateFromString(request.getDateOfBirth(), DateTimeConstant.DATE_FORMAT));
            user.setEmail(request.getEmail());
            user.setVerified(request.isVerified());
            user.setPassword(request.getPassword());
            user.setPhone(request.getPhone());
            user.setAvatar(request.getAvatar());
            user.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            user.setRole(buildRole(roleOptional.get().getRoleId()));
            user.setServices(services);
            return modelMapper.map(userRepository.save(user), UserDTO.class);
        }
        throw new RuntimeException("Có lỗi xảy ra trong quá trình cập nhật thông tin người dùng!");
    }

    @Override
    @Transactional
    public UserDTO deleteByIdUser(Long id) {
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
    public List<UserDTO> deleteAllIdUser(List<Long> ids) {
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : userRepository.findAllById(ids)) {
            userDTOS.add(modelMapper.map(user, UserDTO.class));
        }
        userRepository.deleteAllByIdInBatch(ids);
        return userDTOS;
    }

    @Override
    public Page<User> filterUser(FilterUserRequest request, Date dateFrom, Date dateTo, Date dateOfBirthFrom, Date dateOfBirthTo) {
        Specification<User> specification = CustomUserRepository.filterSpecification(dateFrom, dateTo, dateOfBirthFrom, dateOfBirthTo, request);
        Page<User> userPage = userRepository.findAll(specification, PageRequest.of(request.getStart(), request.getLimit()));
        return userPage;
    }


    private Role buildRole(String roleId){
        return roleRepository.findById(roleId).orElseThrow(()-> new RuntimeException("Role không tồn tại!"));
    }

    private void checkRoleIsValid(String roleId){
        if(roleId == null)
            return;
        Role role = buildRole(roleId);
        if(role == null){
            throw new RuntimeException("Role không tồn tại!");
        }
    }

    private List<com.example.ttcn2etest.model.etity.Service> buildService(List<Long> serviceIds){
        return serviceRepository.findAllById(serviceIds);
    }

    private void checkServiceIsValid(List<Long> serviceIds){
        List<com.example.ttcn2etest.model.etity.Service> services = buildService(serviceIds);

        if(CollectionUtils.isEmpty(services)){
            throw new RuntimeException("Dịch vụ học không tồn tại!");
        }
        List<Long> listIdExists = services.stream().map(com.example.ttcn2etest.model.etity.Service::getId).toList();
        List<Long> idNotExists = serviceIds.stream().filter(s->!listIdExists.contains(s)).toList();

        if (!idNotExists.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Trong danh sách mã dịch vụ có mã không tồn tại trên hệ thống: ");
            for (Long id : idNotExists) {
                errorMessage.append(id).append(", ");
            }
            throw new RuntimeException(errorMessage.toString());
        }
    }



}
