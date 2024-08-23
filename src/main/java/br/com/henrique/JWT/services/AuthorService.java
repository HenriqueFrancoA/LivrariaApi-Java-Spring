package br.com.henrique.JWT.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.henrique.JWT.models.Author;
import br.com.henrique.JWT.repositorys.AuthorRepository;

import java.util.logging.Logger;

@Service
public class AuthorService extends GenericService<Author, Long> {

    public AuthorService(AuthorRepository authorRepository) {
        super(authorRepository);
    }

    private final Logger logger = Logger.getLogger(AuthorService.class.getName());

    @Autowired
    private AuthorRepository authorRepository;


    @Override
    public Author update(Long id, Author author, String simpleName) {
        logger.info("Atualizando Autor, ID: " + id);
        Author aut = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID não encontrado."));

        aut.setName(author.getName());
        aut.setBiography(author.getBiography());

        return authorRepository.save(aut);
    }
}
