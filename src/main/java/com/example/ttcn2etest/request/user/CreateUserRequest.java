package com.example.ttcn2etest.request.user;

import com.example.ttcn2etest.validator.DateValidateAnnotation;
import com.example.ttcn2etest.validator.EmailAnnotation;
import com.example.ttcn2etest.validator.PhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserRequest {
    @NotBlank(message = "User name nguời dùng không được để trống!")
    @Size(min = 6, max = 100, message = "User name người dùng phải có ít nhất 6, nhiều nhất 100 kí tự!")
    private String username;
    @NotBlank(message = "Tên nguời dùng không được để trống!")
    @Size(min = 6, max = 100, message = "Tên người dùng phải có ít nhất 6, nhiều nhất 100 kí tự!")
    private String name;
    @NotBlank(message = "Mật khẩu không được để trống!")
    private String password;
    @NotBlank(message = "Ngày sinh không được để trống!")
    @DateValidateAnnotation(message = "Định dạng ngày tháng phải là dd/mm/yyyy")
    private String dateOfBirth;
    @NotBlank(message = "Số điện thoại không được để trống!")
    @PhoneNumber
    private String phone;
    @NotBlank(message = "Email không được để trống!")
//    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email không hợp lệ!")
    @EmailAnnotation
    private String email;
    @NotBlank(message = "Địa chỉ không được để trống!")
    private String address;
    @NotNull(message = "Không được để trống Chỉ định người dùng có là Admin không? Nhập tùy chọn(1: có, 0: không)")
    private Boolean isSuperAdmin = false;
    @NotBlank(message = "Avatar không được để trống!")
    @Size(max = 500)
    private String avatar;
//    private String roleId;

//    private Timestamp createdDate;
//    private Timestamp updateDate;
}
