package br.com.henrique.JWT.models.dto;

import br.com.henrique.JWT.models.Permission;
import java.util.List;

public class UserDto{

    private Long id;
    private String fullName;
    private Boolean enabled;
    private List<Permission> permissions;

    public UserDto() {
    }

    public UserDto(Long id, String fullName, Boolean enabled, List<Permission> permissions) {
        this.id = id;
        this.fullName = fullName;
        this.enabled = enabled;
        this.permissions = permissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
