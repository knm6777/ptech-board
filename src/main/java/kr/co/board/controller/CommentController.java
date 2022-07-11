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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;

    @PostMapping("")
    public String saveComment(@CurrentUser Member currentMember, Long postId, @Validated CommentVo vo, BindingResult bindingResult) {
        Post post = postService.findById(postId);

        if (bindingResult.hasErrors()) {
            return "redirect:/posts/" + post.getId();
        }
        //해당 댓글의 membberid와 비교해서 수정권한 체크하기

        Comment newComment = new Comment(vo);
        newComment.assignMember(currentMember);
        newComment.assignPost(post);
        commentService.save(newComment);

        return "redirect:/posts/" + post.getId();
    }

    @DeleteMapping("")
    public String delete(Long id, @CurrentUser Member currentMember, RedirectAttributes redirAttrs) {
        Comment deletedComment = commentService.findById(id);

        if (!deletedComment.isSameMember(currentMember)) {
            redirAttrs.addFlashAttribute("error", "삭제 권한이 없습니다.");
        } else {
            commentService.deleteById(id);
        }
        return "redirect:/posts/" + deletedComment.getPost().getId();
    }
}