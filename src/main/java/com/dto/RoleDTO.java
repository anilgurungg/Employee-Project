package com.dto;

import com.entity.RoleName;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDTO {
    private int id;
    private RoleName name;

    public RoleDTO(int id, RoleName name) {
        this.id = id;
        this.name = name;
    }
}
