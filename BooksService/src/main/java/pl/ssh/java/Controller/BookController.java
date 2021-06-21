package pl.ssh.java.Controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.ssh.java.Entity.Book;
import pl.ssh.java.FileStorage.BlobStorage;
import pl.ssh.java.Repository.BookRepository;
import pl.ssh.java.Service.BookService;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final BlobStorage blobStorage;

    public BookController(BookRepository bookRepository, BookService bookService, BlobStorage blobStorage) {
        this.bookRepository = bookRepository;
        this.bookService = bookService;
        this.blobStorage = blobStorage;
    }

    @GetMapping("/get")
    public List<Book> getAllBooks() {
        var books =  bookRepository.findAll();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for (Book b : books) {
            if(b.publicationDate != null)
            {
                b.setFormattedDate(df.format(b.publicationDate));
            }
        }
        return books;
    }

    @GetMapping("/get/{id}")
    public Book getBookById(@ModelAttribute("id") UUID id) {
        var book = bookRepository.findById(id);
        if (book.isPresent()) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if(book.get().publicationDate != null)
            {
                book.get().setFormattedDate(df.format(book.get().publicationDate));
            }
            return book.get();
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Entity does not exist!"
        );
    }

    @PostMapping("/create")
    public UUID createBook(@RequestBody Book book) {
        if (book.filename != null && book.fileContent != null) {
            var stream = bookService.convertFileContentToStream(book.fileContent);
            try {
                book.cover = blobStorage.uploadFile(book.filename, stream, stream.available());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        var createdBook = bookRepository.save(book);
        return createdBook.id;
    }

    @PostMapping("/update")
    public UUID updateBook(@RequestBody Book book) {
        var currentBook = bookRepository.findById(book.id);
        if (currentBook.isPresent()) {
            bookRepository.save(book);
            return book.id;
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Entity does not exist!"
        );
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBook(@ModelAttribute("id") UUID id) {
        var book = bookRepository.findById(id);
        book.ifPresent(bookRepository::delete);
    }
}
