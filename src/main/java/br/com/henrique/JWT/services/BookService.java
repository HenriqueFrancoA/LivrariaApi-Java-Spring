package br.com.henrique.JWT.services;

import br.com.henrique.JWT.mapper.DozerMapper;
import br.com.henrique.JWT.models.*;
import br.com.henrique.JWT.models.dto.BookPriceQuantityDto;
import br.com.henrique.JWT.models.dto.BookWithAuthorPublisherCategoryDto;
import br.com.henrique.JWT.repositories.AuthorRepository;
import br.com.henrique.JWT.repositories.BookRepository;
import br.com.henrique.JWT.repositories.CategoryRepository;
import br.com.henrique.JWT.repositories.PublisherRepository;
import br.com.henrique.JWT.utils.ValidationUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class BookService  {

    private final Logger logger = Logger.getLogger(BookService.class.getName());

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private CategoryRepository categoryRepository;    

    public Book update(Long id, BookPriceQuantityDto bookPriceQuantity) {
        logger.info("Atualizando Livro, ID: " + id);
        Book bo = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID não encontrado."));

        ValidationUtils.verifyQuantity(bookPriceQuantity.getStockQuantity());
        ValidationUtils.verifyValue(bookPriceQuantity.getPrice());

        bo.setPrice(bookPriceQuantity.getPrice());
        bo.setStockQuantity(bookPriceQuantity.getStockQuantity());

        return bookRepository.save(bo);
    }

    public Book save(BookWithAuthorPublisherCategoryDto bookWithAuthorPublisherCategory) {
        logger.info("Criando Livro.");

        Author author = authorRepository.findById(bookWithAuthorPublisherCategory.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("ID do Autor não encontrado."));

        Publisher publisher = publisherRepository.findById(bookWithAuthorPublisherCategory.getPublisherId())
                .orElseThrow(() -> new EntityNotFoundException("ID da Editora não encontrada."));
        
        Category category = categoryRepository.findById(bookWithAuthorPublisherCategory.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("ID da Categoria não encontrada."));
        
        Book book = new Book(
                bookWithAuthorPublisherCategory,
                author,
                publisher,
                category
        );

        return bookRepository.save(book);
    }

    public List<Book> findAll() {
        logger.info("Retornando todos livros...");
        return DozerMapper.parseListObjects(bookRepository.findAll(), Book.class);
    }

    public void delete(Long id) {
        logger.info("Deletando livro do ID: " + id);
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Livro não encontrado.");
        }
        bookRepository.deleteById(id);
    }


    public Book findById(Long id) {
        logger.info("Procurando Endereço do ID: " + id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->  new EntityNotFoundException("ID do livro não encontrado."));

        return DozerMapper.parseObject(book, Book.class);
    }
}
