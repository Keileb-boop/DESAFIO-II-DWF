CREATE TABLE book (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    author_email VARCHAR(255),
    published_date DATE,
    pages INT,
    publication_year INT
);
