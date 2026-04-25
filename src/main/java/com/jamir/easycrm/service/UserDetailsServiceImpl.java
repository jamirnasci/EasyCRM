package com.jamir.easycrm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jamir.easycrm.config.UserPrincipal;
import com.jamir.easycrm.repository.UserRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserRepository ur;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return ur.findByEmail(email).map(user ->{
			return new UserPrincipal(user);
		}).orElseGet(() -> {
			throw new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email);
		});
	}
	
}
