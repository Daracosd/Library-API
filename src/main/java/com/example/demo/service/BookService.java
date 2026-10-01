package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Saves a new book, ensuring the available count defaults to the total number of copies.
    public Book saveBook(Book book) {
        if (book.getAvailableCopies() == null) {
            book.setAvailableCopies(book.getTotalCopies());
        }
        return bookRepository.save(book);
    }

    // Updates only the editable catalog fields for a book.
    public Book updateBook(Long id, Book updatedBook) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(updatedBook.getTitle());
                    book.setAuthor(updatedBook.getAuthor());
                    book.setPublisher(updatedBook.getPublisher());
                    book.setIsbn(updatedBook.getIsbn());
                    return bookRepository.save(book);
                })
                .orElse(null);
    }

    // Reads a book by ID.
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    // Returns the full catalog.
    public Iterable<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Finds books by author name.
    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    // Finds books by publisher name.
    public List<Book> getBooksByPublisher(String publisher) {
        return bookRepository.findByPublisher(publisher);
    }

    // Removes a book from the collection.
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}