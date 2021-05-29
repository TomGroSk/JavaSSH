package pl.ssh.java.Controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.ssh.java.Entity.Book;
import pl.ssh.java.FileStorage.BlobStorage;
import pl.ssh.java.Repository.BookRepository;
import pl.ssh.java.Service.BookService;

import java.io.IOException;
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
    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    @GetMapping("/get/{id}")
    public Book getBookById(@ModelAttribute("id") UUID id){
        var book = bookRepository.findById(id);

        if(book.isPresent()){
            return book.get();
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Entity does not exist!"
        );
    }

    @PostMapping("/create")
    public UUID createBook(@RequestBody Book book){
        var stream = bookService.convertFileContentToStream(book.fileContent);

        try {
            book.cover = blobStorage.uploadFile(book.filename, stream, stream.available());
        } catch (IOException e) {
            e.printStackTrace();
        }

        var createdBook = bookRepository.save(book);
        return createdBook.id;
    }

    @PostMapping("/update")
    public UUID updateBook(@RequestBody Book book){
        var currentBook = bookRepository.findById(book.id);
        if (currentBook.isPresent()){
            bookRepository.save(book);
            return book.id;
        }
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBook(@ModelAttribute("id") UUID id){
        var book = bookRepository.findById(id);
        book.ifPresent(bookRepository::delete);
    }
}
