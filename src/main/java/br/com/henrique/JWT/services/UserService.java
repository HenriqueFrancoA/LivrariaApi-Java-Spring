package br.com.henrique.JWT.services;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.henrique.JWT.models.User;
import br.com.henrique.JWT.repositorys.UserRepository;

@Service
public class UserService implements UserDetailsService{

    private Logger logger = Logger.getLogger(UserService.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Procurando usuário pelo nome: " + username);

        User user = userRepository.findByUsername(username);

        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("Usuário :" + username + " não encontrado!");
        }

    }

}
