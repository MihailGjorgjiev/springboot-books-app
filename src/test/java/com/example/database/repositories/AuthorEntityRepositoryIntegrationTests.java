package com.example.database.repositories;

import com.example.database.TestDataUtil;
import com.example.database.domain.entities.AuthorEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorEntityRepositoryIntegrationTests {
    private AuthorRepository authorRepository;

    @Autowired
    public AuthorEntityRepositoryIntegrationTests(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Test
    public void testThatAuthorCanBeCreatedAndRecalled(){
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        authorRepository.save(authorEntity);
        Optional<AuthorEntity> result=authorRepository.findById(authorEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorEntity);
    }

    @Test
    public void testThatMultipleAuthorsCanBeCreatedAndRecalled(){
        AuthorEntity authorEntityA =TestDataUtil.createTestAuthorA();
        authorRepository.save(authorEntityA);
        AuthorEntity authorEntityB =TestDataUtil.createTestAuthorB();
        authorRepository.save(authorEntityB);
        AuthorEntity authorEntityC =TestDataUtil.createTestAuthorC();
        authorRepository.save(authorEntityC);

        Iterable<AuthorEntity> result= authorRepository.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(authorEntityA, authorEntityB, authorEntityC);
    }

    @Test
    public void testThatAuthorCanBeUpdated(){
        AuthorEntity authorEntityA =TestDataUtil.createTestAuthorA();
        authorRepository.save(authorEntityA);
        authorEntityA.setName("UPDATED");
        authorRepository.save(authorEntityA);
        Optional<AuthorEntity> result=authorRepository.findById(authorEntityA.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorEntityA);
    }

    @Test
    public void testThatAuthorCanBeDeleted(){
        AuthorEntity authorEntity =TestDataUtil.createTestAuthorA();
        authorRepository.save(authorEntity);
        authorRepository.deleteById(authorEntity.getId());
        Optional<AuthorEntity> result=authorRepository.findById(authorEntity.getId());
        assertThat(result).isEmpty();
    }

    @Test
    public void testThatGetAuthorWithAgeLessThan(){
        AuthorEntity testAuthorEntityA =TestDataUtil.createTestAuthorA();
        authorRepository.save(testAuthorEntityA);
        AuthorEntity testAuthorEntityB =TestDataUtil.createTestAuthorB();
        authorRepository.save(testAuthorEntityB);
        AuthorEntity testAuthorEntityC =TestDataUtil.createTestAuthorC();
        authorRepository.save(testAuthorEntityC);

        Iterable<AuthorEntity> result=authorRepository.ageLessThan(50);
        assertThat(result).containsExactly(testAuthorEntityB, testAuthorEntityC);
    }

    @Test
    public void testThatGetAuthorsWithAgeGreaterThan(){
        AuthorEntity testAuthorEntityA =TestDataUtil.createTestAuthorA();
        authorRepository.save(testAuthorEntityA);
        AuthorEntity testAuthorEntityB =TestDataUtil.createTestAuthorB();
        authorRepository.save(testAuthorEntityB);
        AuthorEntity testAuthorEntityC =TestDataUtil.createTestAuthorC();
        authorRepository.save(testAuthorEntityC);

        Iterable<AuthorEntity> result=authorRepository.findAuthorWithAgeGreaterThan(50);
        assertThat(result).containsExactly(testAuthorEntityA);
    }
}
