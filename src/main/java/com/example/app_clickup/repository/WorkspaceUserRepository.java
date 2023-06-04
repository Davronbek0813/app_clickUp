package com.example.app_clickup.repository;

import com.example.app_clickup.entity.WorkspaceUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkspaceUserRepository extends JpaRepository<WorkspaceUser, UUID> {
}
