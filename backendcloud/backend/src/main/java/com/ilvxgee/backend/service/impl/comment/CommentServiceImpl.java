package com.ilvxgee.backend.service.impl.comment;


import com.ilvxgee.backend.mapper.CommentMapper;
import com.ilvxgee.backend.pojo.Comment;

import com.ilvxgee.backend.service.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Override
    public List<Comment> getCommentList() {
        System.out.println(commentMapper.selectList(null));
        return commentMapper.selectList(null);
    }

    @Override
    public Map<String, String> addComment(Map<String, String> data) {
        int user_id = Integer.parseInt(data.get("user_id"));
        String content= data.get("comment");
        String username= data.get("username");

        Comment comment = new Comment(null,user_id,username,content,new Date());
        commentMapper.insert(comment);
        return null;
    }


}
