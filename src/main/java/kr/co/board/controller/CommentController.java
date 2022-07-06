package kr.co.board.controller;

import kr.co.board.model.Comment;
import kr.co.board.model.Member;
import kr.co.board.model.Post;
import kr.co.board.model.vo.CommentVo;
import kr.co.board.service.CommentService;
import kr.co.board.service.PostService;
import kr.co.board.util.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;

    @PostMapping("")
    public String saveComment(@ModelAttribute CommentVo vo, Long id, @CurrentUser Member currentMember) {
        Post post = postService.findById(id);
        Comment newComment = new Comment(vo);
        newComment.assignMember(currentMember);
        newComment.assignPost(post);
        commentService.save(newComment);

        return "redirect:/posts/" + post.getId();
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        Comment deletedComment = commentService.deleteById(id);
        return "redirect:/posts/" + deletedComment.getPost().getId();
    }
}