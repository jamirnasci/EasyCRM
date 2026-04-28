package com.jamir.easycrm.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jamir.easycrm.model.User;
import com.jamir.easycrm.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private PasswordEncoder pe;
	
	@Autowired
	private UserRepository ur;
	
	public User createUser(User user, MultipartFile imgFile) {
		String fileName = createUserImage(imgFile);
		user.setImgUrl("/uploads/users/" + fileName);
		user.setPassword(pe.encode(user.getPassword()));
		return ur.save(user);
	}

	public String createUserImage(MultipartFile imgFile){
		if(imgFile != null && imgFile.isEmpty()) {
			throw new RuntimeException("Arquivo foto de perfil inválido");
		}
		String originalName = imgFile.getOriginalFilename();
		String extension = originalName.substring(originalName.lastIndexOf("."));
		
		String fileName = UUID.randomUUID().toString() + extension;
		Path uploadPath = Paths.get("uploads");
		
		if(!Files.exists(uploadPath)) {
			try {
				Files.createDirectories(uploadPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Path filePath = uploadPath.resolve(fileName);
		try {
			Files.copy(imgFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}
}
