package com.jamir.easycrm.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jamir.easycrm.exception.UserException;
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

	public List<User> findAll() {
		return ur.findAll();
	}

	public User findById(Long id) {
		return ur.findById(id).orElseThrow(() -> new UserException("Usuario não encontrado"));
	}	

	public void deleteImg(User u) {
		String filePath = u.getImgUrl();
		if (filePath.startsWith("/")) {
			filePath = filePath.substring(1);
		}
		String basePath = System.getProperty("user.dir");
		File imgFile = new File(basePath, filePath);

		if (imgFile.exists()) {
			if (!imgFile.delete()) {
				throw new UserException("Falha ao remover imagem do usuario");
			}
		}
	}

	public String createUserImage(MultipartFile imgFile) {
		if (imgFile != null && imgFile.isEmpty()) {
			throw new UserException("Arquivo foto de perfil inválido");
		}
		String originalName = imgFile.getOriginalFilename();
		String extension = originalName.substring(originalName.lastIndexOf("."));

		String fileName = UUID.randomUUID().toString() + extension;
		Path uploadPath = Paths.get("uploads/users");

		if (!Files.exists(uploadPath)) {
			try {
				Files.createDirectories(uploadPath);
			} catch (IOException e) {
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

	public void replaceImg(User u, MultipartFile imgFile) {
		String filePath = u.getImgUrl();
		if (filePath.startsWith("/")) {
			filePath = filePath.substring(1);
		}
		String basePath = System.getProperty("user.dir");
		File oldImgFile = new File(basePath, filePath);

		if (oldImgFile.exists()) {
			try {
				imgFile.transferTo(oldImgFile);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new UserException("Falha ao substituir imagem do usuario: arquivo antigo não encontrado");
		}
	}

	public Optional<User> update(Long iduser, User user, MultipartFile imgFile, String newPassword) {
		return ur.findById(iduser).map(userFound -> {
			if(pe.matches(user.getPassword(), userFound.getPassword()) == false) {
				throw new UserException("Senha atual incorreta");
			}
			if (userFound.getImgUrl() != null && imgFile != null && !imgFile.isEmpty()) {
				replaceImg(userFound, imgFile);
			}
			if (userFound.getImgUrl() == null && imgFile != null && !imgFile.isEmpty() && userFound.getImgUrl() != null) {
				deleteImg(userFound);
				String fileName = createUserImage(imgFile);
				userFound.setImgUrl("/uploads/users/" + fileName);
			}
			userFound.setName(user.getName());
			userFound.setPhone(user.getPhone());
			userFound.setEmail(user.getEmail());
			if(newPassword != null && !newPassword.isEmpty()) {
				userFound.setPassword(pe.encode(newPassword));
			}
			return ur.save(userFound);
		});
	}
}
