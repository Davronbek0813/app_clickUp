package com.example.app_clickup.service;

import com.example.app_clickup.entity.User;
import com.example.app_clickup.payload.ApiResponse;
import com.example.app_clickup.payload.MemberDTO;
import com.example.app_clickup.payload.WorkspaceDTO;
import com.example.app_clickup.payload.WorkspaceRoleDTO;

import java.util.List;
import java.util.UUID;


public interface WorkspaceService {
    ApiResponse addWorkspace(WorkspaceDTO workspaceDTO, User user);
    ApiResponse editWorkspace(WorkspaceDTO workspaceDTO);
    ApiResponse changeOwnerWorkspace(Long id, UUID ownerId);
    ApiResponse deleteWorkspace(Long id);
    ApiResponse addOrEditOrRemoveWorkspace(Long id, MemberDTO memberDTO);

    ApiResponse joinToWorkspace(Long id, User user);

    List<MemberDTO> getMemberAndGuest(Long id);

    List<WorkspaceDTO> getMyWorkspace(User user);

    ApiResponse addOrRemovePermissionToRole(WorkspaceRoleDTO workspaceRoleDTO);

    ApiResponse addRole(Long workspaceId, WorkspaceRoleDTO workspaceRoleDTO, User user);

}
