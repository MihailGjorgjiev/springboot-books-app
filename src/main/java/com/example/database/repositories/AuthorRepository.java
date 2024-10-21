package com.example.database.repositories;

import com.example.database.domain.entities.AuthorEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<AuthorEntity,Long> {
    Iterable<AuthorEntity> ageLessThan(int age);

    @Query(value = "select a from AuthorEntity a where a.age > ?1")
    Iterable<AuthorEntity> findAuthorWithAgeGreaterThan(int age);
}
