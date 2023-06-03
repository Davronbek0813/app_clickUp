package com.example.app_clickup.payload;

import com.example.app_clickup.entity.Attachment;
import com.example.app_clickup.entity.User;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Data
public class WorkspaceDTO {
    private String name;

    private String color;

    private UUID avatarId;

}
