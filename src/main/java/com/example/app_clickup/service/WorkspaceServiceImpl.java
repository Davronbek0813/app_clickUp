package com.example.app_clickup.service;

import com.example.app_clickup.entity.User;
import com.example.app_clickup.entity.Workspace;
import com.example.app_clickup.payload.ApiResponse;
import com.example.app_clickup.payload.WorkspaceDTO;
import com.example.app_clickup.repository.AttachmentRepository;
import com.example.app_clickup.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Override
    public ApiResponse addWorkspace(WorkspaceDTO workspaceDTO, User user) {
        //        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (workspaceRepository.existsByOwnerIdAndName(user.getId(), workspaceDTO.getName())) {
            return new ApiResponse("Sizda bunday nomli ishxona allaqachon mavjud", false);
        }

        Workspace workspace = new Workspace(
                workspaceDTO.getName(),
                workspaceDTO.getColor(),
                user,
                workspaceDTO.getName().substring(0, 1),
                workspaceDTO.getAvatarId() == null ? null: attachmentRepository.findById(workspaceDTO.getAvatarId()).orElseThrow(() ->new ResourceNotFoundException("Attachment")
        ));
        return null;
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

        return null;
    }
}
