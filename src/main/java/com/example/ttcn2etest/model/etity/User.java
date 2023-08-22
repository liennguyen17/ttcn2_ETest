package com.example.ttcn2etest.model.etity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "users")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(name = "username", unique = true, nullable = false, length = 100)
    private String username;
    @NotBlank
    @Size(max = 100)
    private String name;
    @NotBlank
    @Size(max = 100)
    private String password;
    @NotBlank
    @Size(max = 15)
    private String phone;
    @NotBlank
    private String email;

    @Column(name = "password_no_encode", length = 100)
    private String passwordNoEncode;
    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    private String address;
    @Column(name = "created_date")
    private Timestamp createdDate;
    @Column(name = "update_date")
    private Timestamp updateDate;
    @Column(name = "is_super_admin")
    private Boolean isSuperAdmin = false;
    @Size(max = 500)
    private String avatar;

//    @ManyToMany(mappedBy = "users")
//    private Collection<Service> services;
//
//    @ManyToOne
//    @JoinColumn(name = "role_id")
//    private Role role;

}
