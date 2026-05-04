package com.jamir.easycrm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jamir.easycrm.exception.AdmException;
import com.jamir.easycrm.model.User;
import com.jamir.easycrm.repository.UserRepository;

@Service
public class AdmService {
    @Autowired
    private UserService us;
    @Autowired
    private UserRepository ur;
    @Autowired
    private PasswordEncoder pe;

    public User update(Long iduser, User user, MultipartFile imgFile) {
        User userFound = ur.findById(iduser).orElseThrow(() -> new AdmException("Usuário não encontrado"));
        try {

            if (userFound.getImgUrl() != null && imgFile != null && !imgFile.isEmpty()) {
                us.replaceImg(userFound, imgFile);
            }
            if (userFound.getImgUrl() == null && imgFile != null && !imgFile.isEmpty()
                    && userFound.getImgUrl() != null) {
                us.deleteImg(userFound);
                String fileName = us.createUserImage(imgFile);
                userFound.setImgUrl("/uploads/users/" + fileName);
            }
            userFound.setName(user.getName());
            userFound.setPhone(user.getPhone());
            userFound.setEmail(user.getEmail());
            userFound.setStatus(user.getStatus());
            userFound.setRole(user.getRole());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                userFound.setPassword(pe.encode(user.getPassword()));
            }
            return ur.save(userFound);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw new AdmException(
                    "Falha ao atualizar os dados do usuário. Verifique as informações e tente novamente.");
        }
    }

}
