package pl.ssh.java.controller;

import org.springframework.web.bind.annotation.*;
import pl.ssh.java.entity.Comment;
import pl.ssh.java.entity.CommentDto;
import pl.ssh.java.repository.CommentsRepository;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentsRepository commentsRepository;

    public CommentController(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    @PostMapping("/create")
    public UUID createComment(@RequestBody CommentDto commentDto){
        var comment = new Comment();
        comment.setAuthor(commentDto.author);
        comment.setContent(commentDto.content);
        comment.setPartitionKey(commentDto.parentId.toString());
        comment.setRowKey(UUID.randomUUID().toString());
        commentsRepository.create(comment);

        return UUID.fromString(comment.getRowKey());
    }

    @GetMapping("/{id}")
    public ArrayList<Comment> getByParentId(@ModelAttribute("id") UUID parentId){
        return commentsRepository.getCommentsByParentId(parentId);
    }
}
