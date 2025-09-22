package sv.edu.udb.dto;

    import jakarta.validation.constraints.*;
    import java.time.LocalDate;

    public class BookDTO {

        private Long id; // solo en responses

        @NotBlank(message = "The title is mandatory")
        @Size(min = 2, max = 100, message = "The title must be between 2 and 100 characters")
        private String title;

        @NotBlank(message = "The author is mandatory")
        @Size(min = 2, max = 60, message = "The author's name must be between 2 and 60 characters")
        private String author;

        @Email(message = "The author's email must be valid")
        private String authorEmail;

        @PastOrPresent(message = "The publication date cannot be in the future")
        private LocalDate publishedDate;

        @NotNull(message = "The number of pages is mandatory")
        @Min(value = 1, message = "The book must have at least 1 page")
        private Integer pages;

        @Min(value = 1450, message = "The publication year must be after 1450")
        private Integer publicationYear;

        // Getters y setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }

        public String getAuthorEmail() { return authorEmail; }
        public void setAuthorEmail(String authorEmail) { this.authorEmail = authorEmail; }

        public LocalDate getPublishedDate() { return publishedDate; }
        public void setPublishedDate(LocalDate publishedDate) { this.publishedDate = publishedDate; }

        public Integer getPages() { return pages; }
        public void setPages(Integer pages) { this.pages = pages; }

        public Integer getPublicationYear() { return publicationYear; }
        public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }
    }