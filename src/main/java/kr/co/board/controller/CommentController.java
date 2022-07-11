package kr.co.board.controller;

import kr.co.board.model.Comment;
import kr.co.board.model.Member;
import kr.co.board.model.Post;
import kr.co.board.model.vo.CommentVo;
import kr.co.board.model.vo.PostVo;
import kr.co.board.service.CommentService;
import kr.co.board.service.PostService;
import kr.co.board.util.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
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

        Comment newComment = new Comment(vo);
        newComment.assignMember(currentMember);
        newComment.assignPost(post);
        commentService.save(newComment);

        return "redirect:/posts/" + post.getId();
    }

    @PutMapping("")
    public String updateComment(Long id, Long postId, @ModelAttribute CommentVo vo, @CurrentUser Member currentMember, BindingResult bindingResult) {
        Comment commentForUpdate = commentService.findById(id);

        if (bindingResult.hasErrors()) {
            return "redirect:/posts/" + postId;
        }

        if (commentForUpdate.getMemberId() != currentMember.getId()) {
            log.error("Error ====== 댓글 수정 권한 없음");
        }

        commentForUpdate.update(vo);
        commentService.save(commentForUpdate);

        return "redirect:/posts/" + postId;
    }

    @DeleteMapping("")
    public String delete(@CurrentUser Member currentMember, Long id) {
        Comment deletedComment = commentService.findById(id);

        if (deletedComment.getMemberId() != currentMember.getId()) {
            log.error("Error ====== 댓글 삭제 권한 없음");
        } else {
            commentService.deleteById(id);
        }
        return "redirect:/posts/" + deletedComment.getPost().getId();
    }
}