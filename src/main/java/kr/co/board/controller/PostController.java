package kr.co.board.controller;

import kr.co.board.exception.CustomException;
import kr.co.board.model.BoardSearchParam;
import kr.co.board.model.Comment;
import kr.co.board.model.enums.ErrorCode;
import kr.co.board.model.Member;
import kr.co.board.model.Post;
import kr.co.board.model.helper.Pagination;
import kr.co.board.model.vo.CommentVo;
import kr.co.board.model.vo.PostVo;
import kr.co.board.service.CommentService;
import kr.co.board.service.FileService;
import kr.co.board.service.PostService;
import kr.co.board.model.helper.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final FileService fileService;

    @GetMapping("")
    public String index(Model model, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @ModelAttribute(name = "boardSearchParam") BoardSearchParam searchParam) {

        Pagination pagination = new Pagination(pageable);
        Page<Post> postPage = postService.findAllBySearchParam(pagination, searchParam);

        pagination.setTotalPages(postPage.getTotalPages());
        pagination.setTotalElements(postPage.getTotalElements());
        pagination.setQuery(searchParam.getQuery());

        model.addAttribute("pagination", pagination);
        model.addAttribute("posts", postPage);

        return "app/posts/index";
    }

    @GetMapping("/{id}")
    public String show(@CurrentUser Member currentMember, @PathVariable Long id, Model model,
                       @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Post post = postService.findById(id);

        if(post == null) {
            throw new CustomException(ErrorCode.POSTS_NOT_FOUND);
        }

        // 본인이 아닐 시 조회수 올라감
        if(!post.isWriter(currentMember)) {
            postService.updateHit(id);
        }

        List<Comment> comments = post.getComments();

//        댓글 페이징 이게 최선일까..
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), comments.size());
        final Page<Comment> commentPage = new PageImpl<>(comments.subList(start, end), pageable, comments.size());

        Pagination pagination = new Pagination(pageable);
        pagination.setTotalPages(commentPage.getTotalPages());
        pagination.setTotalElements(commentPage.getTotalElements());

        Post prePost = postService.findPrePost(id);
        Post nextPost = postService.findNextPost(id);

        model.addAttribute("post", post);
        model.addAttribute("nextPost", nextPost);
        model.addAttribute("prePost", prePost);
        model.addAttribute("comments", commentPage);
        model.addAttribute("currentMember", currentMember);
        model.addAttribute("pagination", pagination);

        CommentVo comment = new CommentVo();
        model.addAttribute("comment", comment);

        return "app/posts/show";
    }

    @GetMapping("/new")
    public String newPost(Model model) {
        PostVo post = new PostVo();
        model.addAttribute("post", post);

        return "app/posts/new";
    }

    @Transactional
    @PostMapping("")
    public String save(@CurrentUser Member currentMember, @Validated @ModelAttribute(name = "post") PostVo postVo, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return "app/posts/new";
        }

        Post post = Post.builder()
                .title(postVo.getTitle())
                .content(postVo.getContent())
                .member(currentMember).build();

        post = postService.save(post);

        if (postVo.hasFile()) {
            for(int i=0; i<postVo.getFs().size(); i++) {
                if(!postVo.getFs().get(i).getOriginalFilename().equals("")) {
                    fileService.saveAttachment(postVo.getFs().get(i), post);
                }
            }
        }
        if (postVo.getSavedImgIds().length > 0) {
            fileService.setPostId(postVo.getSavedImgIds(), post);
        }

        return "redirect:/posts/" + post.getId();
    }

    @GetMapping("/{id}/edit")
    public String update(@PathVariable("id") Long id, Model model, @CurrentUser Member currentMember, RedirectAttributes redirAttrs) {
        Post post = postService.findById(id);

        if (!post.isWriter(currentMember)) {
            redirAttrs.addFlashAttribute("postError", "수정 권한이 없습니다.");
            return "redirect:/posts/" + post.getId();
        }
        model.addAttribute("post", post);

        return "app/posts/new";
    }

    @PutMapping("")
    public String update(@CurrentUser Member currentMember, Long id, @Validated @ModelAttribute(name="post") PostVo vo,
                             BindingResult bindingResult, RedirectAttributes redirAttrs) throws Exception {
        Post postForUpdate = postService.findById(id);

        if (bindingResult.hasErrors()) {
            return "app/posts/new";
        }
        if (!postForUpdate.isWriter(currentMember)) {
            redirAttrs.addFlashAttribute("postError", "수정 권한이 없습니다.");
            return "redirect:/posts/" + postForUpdate.getId();
        }

        postForUpdate.update(vo);
        postService.save(postForUpdate);
        fileService.updateAttachment(postForUpdate, vo.getDeleteFileIds(), vo.getFs());

        return "redirect:/posts/" + postForUpdate.getId();
    }

    @DeleteMapping("")
    public String delete(Long id, @CurrentUser Member currentMember, RedirectAttributes redirAttrs) {
        Post post = postService.findById(id);
        if (!post.isWriter(currentMember)) {
            redirAttrs.addFlashAttribute("postError", "삭제 권한이 없습니다.");
            return "redirect:/posts/" + post.getId();
        } else {
            postService.deleteById(id);
            return "redirect:/posts";
        }
    }
}