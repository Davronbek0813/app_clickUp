package com.example.app_clickup.repository;

import com.example.app_clickup.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttachmentRepository  extends JpaRepository<Attachment, UUID> {
}
