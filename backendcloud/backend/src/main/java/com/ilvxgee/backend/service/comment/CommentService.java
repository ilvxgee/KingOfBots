package com.ilvxgee.backend.service.comment;


import com.ilvxgee.backend.pojo.Comment;

import java.util.List;
import java.util.Map;

public interface CommentService {

    List<Comment> getCommentList();

    Map<String, String> addComment(Map<String, String> data);
}
