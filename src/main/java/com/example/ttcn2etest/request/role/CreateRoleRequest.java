package com.example.ttcn2etest.request.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateRoleRequest {
    @NotBlank(message = "Tên vai trò không được để trống!")
    @Size(min = 5, max = 100, message = "Tên vai trò phải có ít nhất 5, nhiều nhất 100 kí tự!")
    private String name;

    @NotBlank(message = "Mô tả vai trò không được để trống!")
    private String description;
//    private List<String> permissionIds;
}
