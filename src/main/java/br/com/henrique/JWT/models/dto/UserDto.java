package br.com.henrique.JWT.models.dto;

public class UserDto{

    private Long id;
    private String fullName;
    private Boolean enabled;

    public UserDto() {
    }

    public UserDto(Long id, String fullName, Boolean enabled) {
        this.id = id;
        this.fullName = fullName;
        this.enabled = enabled;
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

}
