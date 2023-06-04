package com.example.app_clickup.payload;

import com.example.app_clickup.entity.WorkspaceRole;
import com.example.app_clickup.entity.enums.WorkspaceRoleName;
import lombok.Data;

import java.util.UUID;

@Data
public class MemberDTO {
    private UUID id;

    private WorkspaceRoleName workspaceRoleName;
}
