package com.example.app_clickup.service;

import com.example.app_clickup.entity.*;
import com.example.app_clickup.entity.enums.WorkspacePermissionName;
import com.example.app_clickup.entity.enums.WorkspaceRoleName;
import com.example.app_clickup.payload.ApiResponse;
import com.example.app_clickup.payload.MemberDTO;
import com.example.app_clickup.payload.WorkspaceDTO;
import com.example.app_clickup.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    @Autowired
    WorkspaceRepository workspaceRepository;
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    WorkspaceUserRepository workspaceUserRepository;
    @Autowired
    WorkspaceRoleRepository workspaceRoleRepository;
    @Autowired
    WorkspacePermissionRepository workspacePermissionRepository;

    @Override
    public ApiResponse addWorkspace(WorkspaceDTO workspaceDTO, User user) {
        //User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //Workspace ochildi
        if (workspaceRepository.existsByOwnerIdAndName(user.getId(), workspaceDTO.getName())) {
            return new ApiResponse("Sizda bunday nomli ishxona allaqachon mavjud", false);
        }

        Workspace workspace = new Workspace(
                workspaceDTO.getName(),
                workspaceDTO.getColor(),
                user,
                workspaceDTO.getAvatarId() == null ? null : attachmentRepository.findById(workspaceDTO.getAvatarId()).orElseThrow(() -> new ResourceNotFoundException("Attachment")
                ));
        workspaceRepository.save(workspace);
        //Workspace role ochildi
        WorkspaceRole ownerRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_OWNER.name(), null));

        //Ownerga huquqlar berish
        WorkspacePermissionName[] workspacePermissionNames = WorkspacePermissionName.values();
        List<WorkspacePermission> workspacePermissions =new ArrayList<>();

        for (WorkspacePermissionName workspacePermissionName : workspacePermissionNames) {
            WorkspacePermission workspacePermission = new WorkspacePermission(ownerRole,workspacePermissionName);
            workspacePermissions.add(workspacePermission);
        }
        workspacePermissionRepository.saveAll(workspacePermissions);

        //Workspace user ochildi
        workspaceUserRepository.save(new WorkspaceUser(workspace, user, ownerRole, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        return new ApiResponse("Ishxona saqlandi", true);
    }

    @Override
    public ApiResponse editWorkspace(WorkspaceDTO workspaceDTO) {
        return null;
    }

    @Override
    public ApiResponse changeOwnerWorkspace(Long id, UUID ownerId) {
        return null;
    }

    @Override
    public ApiResponse deleteWorkspace(Long id) {

        try {
            workspaceRepository.deleteById(id);
            return new ApiResponse("O'chirildi", true);
        } catch (Exception e) {
            return new ApiResponse("Xatolik", false);
        }
    }

    @Override
    public ApiResponse addOrEditOrRemoveWorkspace(Long id, MemberDTO memberDTO) {
        return null;
    }


}
