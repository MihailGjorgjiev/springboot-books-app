package com.example.database.controllers;

import com.example.database.domain.dto.AuthorDto;
import com.example.database.domain.dto.BookDto;
import com.example.database.domain.entities.AuthorEntity;
import com.example.database.domain.entities.BookEntity;
import com.example.database.mappers.Mapper;
import com.example.database.services.BookService;
import jakarta.servlet.ServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BookController {
    private Mapper<BookEntity,BookDto> bookMapper;
    private BookService bookService;
    public BookController(Mapper<BookEntity, BookDto> bookMapper,BookService bookService) {
        this.bookMapper = bookMapper;
        this.bookService=bookService;
    }

    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> createUpdateBook(@PathVariable("isbn") String isbn,
                                              @RequestBody BookDto bookDto){
        BookEntity bookEntity=bookMapper.mapFrom(bookDto);

        boolean bookExists=bookService.isExists(isbn);

        BookEntity savedBookEntity= bookService.createUpdateBook(isbn,bookEntity);
        BookDto savedBookDto= bookMapper.mapTo(savedBookEntity);
        if(bookExists){
            return  new ResponseEntity<>(savedBookDto, HttpStatus.OK);
        }else{
            return  new ResponseEntity<>(savedBookDto, HttpStatus.CREATED);
        }


    }
    @PatchMapping("/books/{isbn}")
    public ResponseEntity<BookDto> partialUpdateBook(
            @PathVariable("isbn") String isbn,
            @RequestBody BookDto bookDto,
            ServletRequest servletRequest){
      if(!bookService.isExists(isbn)){
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      BookEntity bookEntity=bookMapper.mapFrom(bookDto);
      BookEntity updatedBookEntity=bookService.partialUpdate(isbn,bookEntity);
      return new ResponseEntity<>(bookMapper.mapTo(updatedBookEntity),
              HttpStatus.OK);
    }

    @GetMapping(path = "/books")
    public Page<BookDto> listBooks(Pageable pageable){
        Page<BookEntity> books=bookService.findAll(pageable);
        return books.map(bookMapper::mapTo);
    }
    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> getBook(@PathVariable("isbn") String isbn){
        Optional<BookEntity> book=bookService.findOne(isbn);
        return book.map(bookEntity -> {

            BookDto bookDto=bookMapper.mapTo(bookEntity);
            return new ResponseEntity<>(bookDto,HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity deleteBook(@PathVariable("isbn") String isbn){
        bookService.delete(isbn);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
