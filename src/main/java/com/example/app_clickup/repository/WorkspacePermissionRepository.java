package com.example.app_clickup.repository;

import com.example.app_clickup.entity.WorkspacePermission;
import com.example.app_clickup.entity.enums.WorkspacePermissionName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspacePermissionRepository extends JpaRepository<WorkspacePermission, UUID> {

    Optional<WorkspacePermission> findByWorkspaceRoleIdAndPermissionName(UUID workspaceRole_id, WorkspacePermissionName permissionName);

    List<WorkspacePermission> findAllByWorkspaceRole_NameAndWorkspaceRole_WorkspaceId(String workspaceRole_name, Long workspaceRole_workspace_id);
}
