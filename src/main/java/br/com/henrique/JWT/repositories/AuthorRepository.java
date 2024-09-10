package br.com.henrique.JWT.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.henrique.JWT.models.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

}
