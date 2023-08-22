package com.example.ttcn2etest.model.etity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "permission")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Permission {
    @Id
    @Column(name = "permission_id")
    private String permissionId;
    @NotBlank
    @Size(max = 100)
    private String name;
    @NotBlank
    @Size(max = 200)
    private String description;

    @Column(name = "create_date")
    private Timestamp createdDate;
    @Column(name = "update_date")
    private Timestamp updateDAte;

//    @ManyToMany(mappedBy = "permissions")
//    private Collection<Role> roles;
}
