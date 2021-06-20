package pl.ssh.frontservice.model.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.UUID;

public class Book {

    public UUID id;

    public String title;

    public String publisher;

    public String author;

    public String description;

    public String cover;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date publicationDate;

    public Long isbn;

    public String filename;

    public String fileContent;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }
}
