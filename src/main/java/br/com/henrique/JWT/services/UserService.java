package br.com.henrique.JWT.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.HashMap;


import br.com.henrique.JWT.exceptions.ListEmptyException;
import br.com.henrique.JWT.exceptions.UsernameAlreadyExistsException;
import br.com.henrique.JWT.mapper.DozerMapper;
import br.com.henrique.JWT.models.Permission;
import br.com.henrique.JWT.models.UserPermission;
import br.com.henrique.JWT.models.dto.UserDto;
import br.com.henrique.JWT.models.dto.UserFullnameDto;
import br.com.henrique.JWT.models.dto.UserToCreateDto;
import br.com.henrique.JWT.models.embedd.UserPermissionId;
import br.com.henrique.JWT.repositorys.PermissionRepository;
import br.com.henrique.JWT.repositorys.UserPermissionRepository;
import br.com.henrique.JWT.resources.UserResource;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm;


import br.com.henrique.JWT.models.User;
import br.com.henrique.JWT.repositorys.UserRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserService implements UserDetailsService{

    private Logger logger = Logger.getLogger(UserService.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Procurando Usuário pelo nome: " + username);

        User user = userRepository.findByUsername(username);

        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("Usuário :" + username + " não encontrado!");
        }

    }

    public List<UserDto> findAll() {
        logger.info("Retornando todos Usuários...");

        List<UserDto> listUsers = DozerMapper.parseListObjects(userRepository.findAll(), UserDto.class);

        for (UserDto u : listUsers) {
            u.add(linkTo(methodOn(UserResource.class).findById(u.getKey())).withSelfRel());
        }

        return listUsers;
    }

    public UserDto findById(Long id) {
        logger.info("Procurando Usuário do ID: " + id);
        User user = userRepository.findById(id)
                .orElseThrow(() ->  new EntityNotFoundException("ID do Usuário não encontrado."));

        UserDto userDto = DozerMapper.parseObject(user, UserDto.class);
        userDto.add(linkTo(methodOn(UserResource.class).findById(id)).withSelfRel());

        return userDto;
    }

    public UserDto update(Long id, UserFullnameDto userFullname) {
        logger.info("Atualizando Usuário, ID: " + id);

        User user = userRepository.findById(id)
                .orElseThrow(() ->  new EntityNotFoundException("ID do Usuário não encontrado."));

        user.setFullName(userFullname.getFullName());

        UserDto userDto = DozerMapper.parseObject( userRepository.save(user), UserDto.class) ;

        userDto.add(linkTo(methodOn(UserResource.class).findById(id)).withSelfRel());

        return userDto;
    }


    public UserDto save(UserToCreateDto userToCreateDto) {
        logger.info("Criando Usuário.");

        Permission permission;
        List<Permission> listPermission = new ArrayList<>();

        if(userToCreateDto.getPermissionsIds().isEmpty())
            throw new ListEmptyException("Lista de Permissão está vazia.");

        for(long id : userToCreateDto.getPermissionsIds()){
             permission = permissionRepository.findById(id)
                    .orElseThrow(() ->  new EntityNotFoundException("ID da Permissão não encontrado."));
             listPermission.add(permission);
        }

        User userNameInUse = userRepository.findByUsername(userToCreateDto.getUserName());

        if(userNameInUse != null)
            throw new UsernameAlreadyExistsException("O nome de usuário: '" + userToCreateDto.getUserName() + "' já está em uso.");

        String password = encodePassword(userToCreateDto.getPassword());

        User user = new User(
                userToCreateDto.getUserName(),
                userToCreateDto.getFullName(),
                password.substring(8),
                true,
                true,
                true,
                true
        );

        User userCreated = userRepository.save(user);

        UserPermission userPermission;

        UserPermissionId userPermissionId;


        for(Permission perm : listPermission){
            userPermissionId = new UserPermissionId(user.getId(), perm.getId());
            userPermission = new UserPermission(userPermissionId, user, perm);

            userPermissionRepository.save(userPermission);
        }


        UserDto userDto = DozerMapper.parseObject(userCreated, UserDto.class);
        userDto.add(linkTo(methodOn(UserResource.class).findById(userCreated.getId())).withSelfRel());

        return userDto;
    }

    private static String encodePassword(String password) {
        Map<String, PasswordEncoder> encoders = new HashMap<>();

        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder("",
        8, 185000,
        SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

        encoders.put("pbkdf2", pbkdf2PasswordEncoder);

        DelegatingPasswordEncoder passwordEncoder = new
        DelegatingPasswordEncoder("pbkdf2", encoders);

        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2PasswordEncoder);

        return passwordEncoder.encode(password);
    }
}
