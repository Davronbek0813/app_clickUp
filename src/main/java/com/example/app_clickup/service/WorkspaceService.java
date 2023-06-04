package com.example.app_clickup.service;

import com.example.app_clickup.entity.User;
import com.example.app_clickup.payload.ApiResponse;
import com.example.app_clickup.payload.MemberDTO;
import com.example.app_clickup.payload.WorkspaceDTO;

import java.util.UUID;


public interface WorkspaceService {
    ApiResponse addWorkspace(WorkspaceDTO workspaceDTO, User user);
    ApiResponse editWorkspace(WorkspaceDTO workspaceDTO);
    ApiResponse changeOwnerWorkspace(Long id, UUID ownerId);
    ApiResponse deleteWorkspace(Long id);
    ApiResponse addOrEditOrRemoveWorkspace(Long id, MemberDTO memberDTO);

}
