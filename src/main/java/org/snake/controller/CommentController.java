package org.snake.controller;

import org.snake.dto.Comment;
import org.snake.model.CommentSearchResult;
import org.snake.repository.CommentRepository;
import org.snake.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@CrossOrigin(origins = "http://localhost", maxAge = 3600)
@EnableAutoConfiguration
@RestController
@RequestMapping("/api")
@Transactional
public class CommentController {
    @Autowired
    private SimpMessagingTemplate webSocket;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    //public DemoController(CommentRepository commentRepository) {this.commentRepository=commentRepository;}
    // private List<Comment> commentList = new ArrayList<>();
    // private long id = -1;
    /*
    @GetMapping("/comment")
    public ResponseEntity helloWorld()
    {
        return ResponseEntity.ok(Comment.builder().id(1L).text("Kommentar").build() );
    }*/
    /*
    @GetMapping("/comments/search")
    public ResponseEntity<List<Comment>> searchComment(@RequestParam String suchAnfrage) {
        if (StringUtils.isEmpty(StringUtils.trim(suchAnfrage))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String[] suche; // = new String[ suchAnfrage.split(";;").length];
        suche = suchAnfrage.split("&");
        return ResponseEntity.ok().body(commentService.searchComment(suche));
    }*/

    @PostMapping("/comments")
    public ResponseEntity addComment(@RequestBody Comment comment) {
        if (StringUtils.isEmpty(StringUtils.trim(comment.getText()))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        commentService.saveComment(comment);

        System.out.println("\nDeBugAusgabe: ");
        for (int i = 0; i < commentRepository.findAll().size(); i++) {
            System.out.println("Kommentar Nr: " + i);
            System.out.println(commentRepository.findAll().get(i));
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------");
        webSocket.convertAndSend("/comment/new", comment);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/comments/generator")
    public List<Comment> addComments() {
        /*
        List<Comment> comments = commentRepository.findAll();
        long id;

        System.out.println("LÄNGE = " + comments.toArray().length);
        if(comments.isEmpty()) {
            id = 0;

            Comment c = new Comment("Delete Me=)(/&%$§", null);
            comments.add(c);

            List<Comment> ret = new ArrayList<>();
            commentRepository.saveAll(comments).forEach(ret::add);
            id = comments.get(0).getId();
            deleteComment("Delete Me=)(/&%$§");

        }else{
            id = comments.get(comments.toArray().length-1).getId();
        }*/

        List<Comment> comments = commentService.generateComment();
        for (int i = 0; i < comments.size(); i++) {
            webSocket.convertAndSend("/comment/new", comments.get(i));
        }

        return comments;
    }

    @GetMapping("/comments/search")
    public ResponseEntity<CommentSearchResult> searchCommentWithArray(@RequestParam String[] suchAnfrage) {
        if (StringUtils.isEmpty(StringUtils.trim(suchAnfrage[0]))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().body(commentService.searchComment(suchAnfrage));
    }

    @GetMapping("/comments")
    public ResponseEntity getComments() {

        return ResponseEntity.ok(commentRepository.findAll());
    }

    @GetMapping("/comments/delete")
    public boolean deleteComment(@RequestParam String suchAnfrage) {

        commentRepository.deleteAllByTextContaining(suchAnfrage);
        return true;
    }

    @DeleteMapping("/comments/deleteById/{id}")
    public void deleteCommentById(@PathVariable("id") long ID) {
        commentRepository.deleteById(ID);
        webSocket.convertAndSend("/comment/deleteById", "{ \"id\": " + ID + " }");
    }

}
