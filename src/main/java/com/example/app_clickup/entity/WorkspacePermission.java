
package com.example.app_clickup.entity;

import com.example.appclickup.entity.enums.WorkspacePermissionName;
import com.example.appclickup.entity.template.AbsLongEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WorkspacePermission extends AbsLongEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private    WorkspaceRole workspaceRole;

    @Enumerated(EnumType.STRING)
    private WorkspacePermissionName permissionName;
}
