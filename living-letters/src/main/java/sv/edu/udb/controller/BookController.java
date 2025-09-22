package sv.edu.udb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.dto.BookDTO;
import sv.edu.udb.model.Book;
import sv.edu.udb.service.BookService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@CrossOrigin
@Validated  // enable @RequestParam validation
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    // Book to DTO
    private BookDTO toDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setAuthorEmail(book.getAuthorEmail());
        dto.setPublishedDate(book.getPublishedDate());
        dto.setPages(book.getPages());
        dto.setPublicationYear(book.getPublicationYear());
        return dto;
    }

    // DTO to Book
    private Book toEntity(BookDTO dto) {
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setAuthorEmail(dto.getAuthorEmail());
        book.setPublishedDate(dto.getPublishedDate());
        book.setPages(dto.getPages());
        book.setPublicationYear(dto.getPublicationYear());
        return book;
    }

    @Operation(summary = "Get all books")
    @GetMapping
    public ResponseEntity<List<BookDTO>> listBooks() {
        List<BookDTO> books = bookService.getAllBooks()
                .stream().map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Find a book by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(toDTO(book));
    }

    @Operation(summary = "Create a new book")
    @ApiResponse(responseCode = "201", description = "Book successfully created")
    @PostMapping
    public ResponseEntity<BookDTO> addBook(@Valid @RequestBody BookDTO dto) {
        Book saved = bookService.createBook(toEntity(dto));
        return ResponseEntity.created(URI.create("/api/books/" + saved.getId()))
                .body(toDTO(saved));
    }

    @Operation(summary = "Search the books by title")
    @GetMapping("/search")
    public ResponseEntity<List<BookDTO>> searchByTitle(
            @RequestParam
            @NotBlank(message = "The title cannot be blank")
            @Size(min = 2, message = "The title must have at least 2 characters") String title) {

        List<BookDTO> books = bookService.searchBooksByTitle(title)
                .stream().map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Delete a book by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book deleted"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a book by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book updated"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO dto) {
        Book updated = bookService.updateBook(id, toEntity(dto));
        return ResponseEntity.ok(toDTO(updated));
    }

    // endpoint for testing purposes
    @GetMapping("/api")
    public String home() {
        return "Verification: API is working";
    }
}