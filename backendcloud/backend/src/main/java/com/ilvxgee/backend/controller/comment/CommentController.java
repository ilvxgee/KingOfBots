package com.ilvxgee.backend.controller.comment;

import com.ilvxgee.backend.pojo.Comment;
import com.ilvxgee.backend.service.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class CommentController {


    @Autowired
    private CommentService commentService;

    @GetMapping("/api/comment/getCommentList/")
    public List<Comment> getCommentList() {
        return commentService.getCommentList();
    }

    @PostMapping("/api/comment/addComment/")
    Map<String, String> addComment(@RequestParam Map<String, String> data) {
        return commentService.addComment(data);
    }


}
