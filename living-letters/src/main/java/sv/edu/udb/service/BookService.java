package sv.edu.udb.service;

import sv.edu.udb.model.Book;

import java.util.List;

public interface BookService {

    List<Book> getAllBooks();
    Book createBook(Book book);
    List<Book> searchBooksByTitle(String title);
    void deleteBook(Long id);
    Book getBookById(Long id);
    Book updateBook(Long id, Book updatedBook);

}
