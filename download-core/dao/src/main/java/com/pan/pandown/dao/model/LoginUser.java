package com.pan.pandown.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pan.pandown.dao.entity.PandownUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author yalier(wenyao)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LoginUser implements UserDetails, Serializable {
    private static final long serialVersionUID = 1L;

    private PandownUser pandownUser;

    private Set<GrantedAuthority> authorities;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    public LoginUser(PandownUser pandownUser){
        this.pandownUser = pandownUser;
        this.authorities = null;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    public LoginUser(PandownUser pandownUser, List<GrantedAuthority> authorityList){
        this.pandownUser = pandownUser;
        this.authorities = authorityList.stream().collect(Collectors.toSet());
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return pandownUser.getPassword();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return pandownUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
