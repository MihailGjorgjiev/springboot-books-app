package com.example.database.repositories;

import com.example.database.TestDataUtil;
import com.example.database.domain.entities.AuthorEntity;
import com.example.database.domain.entities.BookEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookEntityRepositoryIntegrationTests {
    private BookRepository bookRepository;

    @Autowired
    public BookEntityRepositoryIntegrationTests(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Test
    public void testThatBookCanBeCreatedAndRecalled(){
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        BookEntity bookEntity =TestDataUtil.createTestBookA(authorEntity);
        bookRepository.save(bookEntity);
        Optional<BookEntity> result=bookRepository.findById(bookEntity.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookEntity);
    }

    @Test
    public void testThatMultipleBooksCanBeCreatedAndRecalled(){
        AuthorEntity authorEntity =TestDataUtil.createTestAuthorA();

        BookEntity bookEntityA =TestDataUtil.createTestBookA(authorEntity);
        bookRepository.save(bookEntityA);
        BookEntity bookEntityB =TestDataUtil.createTestBookB(authorEntity);
        bookRepository.save(bookEntityB);
        BookEntity bookEntityC =TestDataUtil.createTestBookC(authorEntity);
        bookRepository.save(bookEntityC);

        Iterable<BookEntity> result= bookRepository.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(bookEntityA, bookEntityB, bookEntityC);
    }

    @Test
    public void testThatBookCanBeUpdated(){
        AuthorEntity authorEntityA =TestDataUtil.createTestAuthorA();
        AuthorEntity authorEntityB =TestDataUtil.createTestAuthorB();

        BookEntity bookEntityA =TestDataUtil.createTestBookA(authorEntityA);
        bookRepository.save(bookEntityA);

        bookEntityA.setTitle("Updated");
        bookEntityA.setAuthorEntity(authorEntityB);
        bookRepository.save(bookEntityA);

        Optional<BookEntity> result=bookRepository.findById(bookEntityA.getIsbn());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookEntityA);

    }

    @Test
    public void testThatBookCanBeDeleted(){
        AuthorEntity authorEntity=TestDataUtil.createTestAuthorA();
        BookEntity bookEntity=TestDataUtil.createTestBookA(authorEntity);

        bookRepository.save(bookEntity);

        bookRepository.deleteById(bookEntity.getIsbn());

        Optional<BookEntity> result=bookRepository.findById(bookEntity.getIsbn());
        assertThat(result).isEmpty();
    }

}
