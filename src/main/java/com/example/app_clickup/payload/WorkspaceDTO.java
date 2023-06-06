package com.example.app_clickup.payload;

import com.example.app_clickup.entity.Attachment;
import com.example.app_clickup.entity.User;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceDTO {
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String color;

    private String initialLetter;

    private UUID avatarId;

}
