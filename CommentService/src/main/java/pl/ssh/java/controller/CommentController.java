package pl.ssh.java.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
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

    @GetMapping("/{parentId}/{commentId}")
    public Comment getComment(@ModelAttribute("parentId") UUID parentId, @ModelAttribute("commentId") UUID commentId){
        var comment = commentsRepository.getComment(parentId, commentId);

        if(comment == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment does not exists");
        }

        return comment;
    }

    @PostMapping("/{id}")
    public void updateComment(@ModelAttribute("id") UUID commendId,  @RequestBody CommentDto commentDto){
        var comment = commentsRepository.getComment(commentDto.parentId, commendId);
        comment.setAuthor(commentDto.author);
        comment.setContent(commentDto.content);
        commentsRepository.update(comment);
    }

    @DeleteMapping("/{parentId}/{commentId}")
    public void deleteComment(@ModelAttribute("parentId") UUID parentId, @ModelAttribute("commentId") UUID commendId){
        var comment = commentsRepository.getComment(parentId, commendId);

        if(comment != null){
            commentsRepository.delete(comment);
        }
    }
}
