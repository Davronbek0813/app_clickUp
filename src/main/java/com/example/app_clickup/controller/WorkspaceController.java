package com.example.app_clickup.controller;

import com.example.app_clickup.entity.User;
import com.example.app_clickup.payload.ApiResponse;
import com.example.app_clickup.payload.MemberDTO;
import com.example.app_clickup.payload.WorkspaceDTO;
import com.example.app_clickup.payload.WorkspaceRoleDTO;
import com.example.app_clickup.security.CurrentUser;
import com.example.app_clickup.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspace")
public class WorkspaceController {
    /**
     Java
     */

    @Autowired
    WorkspaceService workspaceService;

    @PostMapping
    public HttpEntity<?> addWorkspace(@Valid @RequestBody WorkspaceDTO workspaceDTO, @CurrentUser User user) {
        ApiResponse apiResponse = workspaceService.addWorkspace(workspaceDTO,user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

//    Name, color, avatar o'zgarishi mumkin
    @PutMapping()
    public HttpEntity<?> editWorkspace(@RequestBody WorkspaceDTO workspaceDTO) {
        ApiResponse apiResponse = workspaceService.editWorkspace(workspaceDTO);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/member/{id}")
    public HttpEntity<?> getMemberAndGuest(@PathVariable Long id){
        List<MemberDTO> members = workspaceService.getMemberAndGuest(id);
        return ResponseEntity.ok(members);
    }

    @GetMapping
    public HttpEntity<?> getMyWorkspace(@CurrentUser User user){
        List<WorkspaceDTO> workspaces = workspaceService.getMyWorkspace(user);
        return ResponseEntity.ok(workspaces);
    }

    @PutMapping("/addOrRemovePermission")
    public HttpEntity<?> addOrRemovePermissionToRole(@RequestBody WorkspaceRoleDTO workspaceRoleDTO){
        ApiResponse apiResponse = workspaceService.addOrRemovePermissionToRole(workspaceRoleDTO);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PutMapping("/changeOwner/{id}")
    public HttpEntity<?> changeOwnerWorkspace(@PathVariable Long id, @RequestParam UUID ownerId) {
        ApiResponse apiResponse = workspaceService.changeOwnerWorkspace(id, ownerId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteWorkspace(@PathVariable Long id) {
        ApiResponse apiResponse = workspaceService.deleteWorkspace(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/addOrEditOrRemove/{id}")
    public HttpEntity<?> addOrEditOrRemoveWorkspace(@PathVariable Long id, @RequestBody MemberDTO memberDTO){
        ApiResponse apiResponse = workspaceService.addOrEditOrRemoveWorkspace(id, memberDTO);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);

    }

    @PutMapping("/join")
    public HttpEntity<?> joinToWorkspace(@RequestParam Long id, @CurrentUser User user){
        ApiResponse apiResponse = workspaceService.joinToWorkspace(id, user);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PostMapping("/role")
    public HttpEntity<?> addRole(@RequestParam Long workspaceId, @RequestBody WorkspaceRoleDTO workspaceRoleDTO, @CurrentUser User user){
        ApiResponse apiResponse = workspaceService.addRole(workspaceId,workspaceRoleDTO,user);
        return ResponseEntity.ok(apiResponse);
    }

}
