package pl.ssh.frontservice.model.dto;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class PostMovie {

    public UUID id;

    public String title;

    public String publisher;

    public String author;

    public String description;

    public String cover;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date releaseDate;

    public String filename;

    public String fileContent;

    public MultipartFile multipartFile;

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

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
        try {
            byte[] encodeBase64 = Base64.getEncoder().encode(multipartFile.getBytes()); //changing byte array of image to base64
            fileContent = new String(encodeBase64, StandardCharsets.UTF_8);
            filename = multipartFile.getName();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
