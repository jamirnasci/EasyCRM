package com.jamir.easycrm.config;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jamir.easycrm.model.*;

public class UserPrincipal implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	private final User usuario;
	
	public UserPrincipal(User user) {
		this.usuario = user;
	}
	
	public Long getId() {
		return usuario.getIduser();
	}

	public User getUser() {
		return usuario;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRole()));
	}

	@Override
	public String getPassword() {
		return usuario.getPassword(); 
	}

	@Override
	public String getUsername() {
		return usuario.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}