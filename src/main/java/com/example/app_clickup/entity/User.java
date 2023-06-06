package com.example.app_clickup.entity;



import com.example.app_clickup.entity.enums.SystemRoleName;
import com.example.app_clickup.entity.template.AbsUUIDEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User extends AbsUUIDEntity implements UserDetails {
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String color;

    private String initialLetter;

    private String emailCode;

    private Timestamp lastActiveTime;

    @OneToOne(fetch = FetchType.LAZY)
    private Attachment avatar;

    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;

    @Enumerated(EnumType.STRING)
    private SystemRoleName systemRoleName;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(this.systemRoleName.name());

        return Collections.singletonList(simpleGrantedAuthority);
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return  this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public User(String fullName, String email, String password, SystemRoleName systemRoleName, boolean accountNonLocked, boolean accountNonExpired, boolean credentialsNonExpired) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.systemRoleName = systemRoleName;
        this.accountNonLocked=accountNonLocked;
        this.accountNonExpired=accountNonExpired;
        this.credentialsNonExpired=credentialsNonExpired;
    }
}
