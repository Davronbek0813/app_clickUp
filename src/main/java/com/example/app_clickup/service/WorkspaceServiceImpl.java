package com.example.app_clickup.service;

import com.example.app_clickup.entity.*;
import com.example.app_clickup.entity.enums.AddType;
import com.example.app_clickup.entity.enums.WorkspacePermissionName;
import com.example.app_clickup.entity.enums.WorkspaceRoleName;
import com.example.app_clickup.payload.ApiResponse;
import com.example.app_clickup.payload.MemberDTO;
import com.example.app_clickup.payload.WorkspaceDTO;
import com.example.app_clickup.payload.WorkspaceRoleDTO;
import com.example.app_clickup.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    @Autowired
    UserRepository userRepository;

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
        WorkspaceRole adminRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_ADMIN.name(), null));
        WorkspaceRole memberRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_MEMBER.name(), null));
        WorkspaceRole guestRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_GUEST.name(), null));

        //Ownerga huquqlar berish
        WorkspacePermissionName[] workspacePermissionNames = WorkspacePermissionName.values();
        List<WorkspacePermission> workspacePermissions = new ArrayList<>();

        for (WorkspacePermissionName workspacePermissionName : workspacePermissionNames) {
            WorkspacePermission workspacePermission = new WorkspacePermission(ownerRole, workspacePermissionName);
            workspacePermissions.add(workspacePermission);

            if (workspacePermissionName.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_ADMIN)) {
                workspacePermissions.add(new WorkspacePermission(adminRole, workspacePermissionName));
            }

            if (workspacePermissionName.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_MEMBER)) {
                workspacePermissions.add(new WorkspacePermission(memberRole, workspacePermissionName));
            }

            if (workspacePermissionName.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_GUEST)) {
                workspacePermissions.add(new WorkspacePermission(guestRole, workspacePermissionName));
            }
        }
        workspacePermissionRepository.saveAll(workspacePermissions);

        //Workspace user ochildi
        workspaceUserRepository.save(new WorkspaceUser(workspace, user, ownerRole, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        return new ApiResponse("Ishxona saqlandi", true);
    }

    @Override
    public ApiResponse editWorkspace(WorkspaceDTO workspaceDTO) {
        Workspace workspace = workspaceRepository.findById(workspaceDTO.getId()).orElseThrow(()->new ResourceNotFoundException("Workspace"));
        workspace.setColor(workspaceDTO.getColor());
        workspace.setName(workspaceDTO.getName());
        workspace.setAvatar(workspaceDTO.getAvatarId()==null?null:attachmentRepository.findById(workspaceDTO.getAvatarId()).orElseThrow(()->new ResourceNotFoundException("Avatar")));
        workspaceRepository.save(workspace);
        return new ApiResponse("Workspace edited",true);

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
        if (memberDTO.getAddType().equals(AddType.ADD)) {
            WorkspaceUser workspaceUser = new WorkspaceUser(workspaceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("id")),
                    userRepository.findById(memberDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("id")),
                    workspaceRoleRepository.findById(memberDTO.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("id")),
                    new Timestamp(System.currentTimeMillis()),
                    null
            );
            workspaceUserRepository.save(workspaceUser);
            //TODO Emailga invite xabar yuborish
        } else if (memberDTO.getAddType().equals(AddType.EDIT)) {
            WorkspaceUser workspaceUser = workspaceUserRepository.findByWorkspaceIdAndUserId(id, memberDTO.getId()).orElseGet(WorkspaceUser::new);
            workspaceUser.setWorkspaceRole(workspaceRoleRepository.findById(memberDTO.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("id")));
            workspaceUserRepository.save(workspaceUser);
        } else if (memberDTO.getAddType().equals(AddType.REMOVE)) {
            workspaceUserRepository.deleteByWorkspaceIdAndUserId(id, memberDTO.getId());
        }

        return new ApiResponse("Muvaffaqqiyatli", true);
    }

    @Override
    public ApiResponse joinToWorkspace(Long id, User user) {
        Optional<WorkspaceUser> optionalWorkspaceUser = workspaceUserRepository.findByWorkspaceIdAndUserId(id, user.getId());
        if (optionalWorkspaceUser.isPresent()) {
            WorkspaceUser workspaceUser = optionalWorkspaceUser.get();
            workspaceUser.setDateJoined(new Timestamp(System.currentTimeMillis()));
            workspaceUserRepository.save(workspaceUser);
            return new ApiResponse("Muvaffaqiyatli", true);
        }
        return new ApiResponse("Xatolik", false);
    }

    @Override
    public List<MemberDTO> getMemberAndGuest(Long id) {
        List<WorkspaceUser> workspaceUsers = workspaceUserRepository.findAllByWorkspaceId(id);
//        List<MemberDTO> members = new ArrayList<>();

//        for (WorkspaceUser workspaceUser : workspaceUsers) {
//            members.add(mapWorkspaceUserToMemberDTO(workspaceUser));
//        }
        return workspaceUsers.stream().map(this::mapWorkspaceUserToMemberDTO).collect(Collectors.toList());

    }

    @Override
    public ApiResponse addRole(Long workspaceId, WorkspaceRoleDTO workspaceRoleDTO, User user) {
        if(workspaceRoleRepository.existsByWorkspaceIdAndName(workspaceId,workspaceRoleDTO.getName())){
            return  new ApiResponse("Error",false);
        }
        WorkspaceRole workspaceRole = workspaceRoleRepository.save(new WorkspaceRole(getMyWorkspaceAfterThrow(workspaceId), workspaceRoleDTO.getName(), workspaceRoleDTO.getExtendsRole()));
        List<WorkspacePermission> workspacePermissions = workspacePermissionRepository.findAllByWorkspaceRole_NameAndWorkspaceRole_WorkspaceId(workspaceRoleDTO.getExtendsRole().name(), workspaceId);
        List<WorkspacePermission> newWorkspacePermissions = new ArrayList<>();
        for (WorkspacePermission workspacePermission : workspacePermissions) {
            WorkspacePermission newWorkspacePermission = new WorkspacePermission(workspaceRole, workspacePermission.getPermissionName());
            newWorkspacePermissions.add(workspacePermission);
        }
        workspacePermissionRepository.saveAll(newWorkspacePermissions);
        return new ApiResponse("Accepted",true);
    }

    private Workspace getMyWorkspaceAfterThrow(Long workspaceId) {
        return new Workspace();
    }

    @Override
    public ApiResponse addOrRemovePermissionToRole(WorkspaceRoleDTO workspaceRoleDTO) {

        WorkspaceRole workspaceRole = workspaceRoleRepository.findById(workspaceRoleDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("workspaceRole"));
        Optional<WorkspacePermission> optionalWorkspacePermission = workspacePermissionRepository.findByWorkspaceRoleIdAndPermissionName(workspaceRole.getId(), workspaceRoleDTO.getPermissionName());
        if (workspaceRoleDTO.getAddType().equals(AddType.ADD)) {
            if (optionalWorkspacePermission.isPresent()) {
                return new ApiResponse("Allaqachon qo'shilgan", false);
            }
            WorkspacePermission workspacePermission = new WorkspacePermission(workspaceRole, workspaceRoleDTO.getPermissionName());
            workspacePermissionRepository.save(workspacePermission);
            return new ApiResponse("Muvaffaqiyatli qo'shidi", true);
        } else if (workspaceRoleDTO.getAddType().equals(AddType.REMOVE)) {
            if (optionalWorkspacePermission.isPresent()) {
                workspacePermissionRepository.delete(optionalWorkspacePermission.get());
                return new ApiResponse("Muvaffaqqiyatli o'chirildi", true);
            }
            return new ApiResponse("Bunday object yo'q", false);
        }
        return new ApiResponse("Bunday buyruq  yo'q", false);
    }

    @Override
    public List<WorkspaceDTO> getMyWorkspace(User user) {
        List<WorkspaceUser> workspaces = workspaceUserRepository.findAllByUserId(user.getId());
        return workspaces.stream().map(workspaceUser -> mapWorkspaceUserToWorkspaceDTO(workspaceUser.getWorkspace())).collect(Collectors.toList());

    }
    //My method

    public WorkspaceDTO mapWorkspaceUserToWorkspaceDTO(Workspace workspace) {
        WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        workspaceDTO.setId(workspace.getId());
        workspaceDTO.setName(workspace.getName());
        workspaceDTO.setInitialLetter(workspace.getInitialLeter());
        workspaceDTO.setAvatarId(workspace.getAvatar() == null ? null : workspace.getAvatar().getId());
        workspaceDTO.setColor(workspace.getColor());
        return workspaceDTO;
    }

    public MemberDTO mapWorkspaceUserToMemberDTO(WorkspaceUser workspaceUser) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(workspaceUser.getUser().getId());
        memberDTO.setFullName(workspaceUser.getUser().getFullName());
        memberDTO.setEmail(workspaceUser.getUser().getEmail());
        memberDTO.setRoleName(workspaceUser.getWorkspaceRole().getName());
        memberDTO.setLastActive(workspaceUser.getUser().getLastActiveTime());
        return memberDTO;
    }
}
