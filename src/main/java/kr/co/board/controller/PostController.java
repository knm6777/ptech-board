package kr.co.board.controller;

import kr.co.board.model.Comment;
import kr.co.board.model.Member;
import kr.co.board.model.Post;
import kr.co.board.model.vo.CommentVo;
import kr.co.board.model.vo.PostVo;
import kr.co.board.service.CommentService;
import kr.co.board.service.FileService;
import kr.co.board.service.PostService;
import kr.co.board.util.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final FileService fileService;

    @GetMapping("")
    public String index(Model model, @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Post> postPage = postService.findAll(pageable);

        model.addAttribute("postPage", postPage);

        int totalPages = postPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "app/posts/index";
    }

    @GetMapping("/{id}")
    public String showPost(@PathVariable Long id, Model model) {
        Post post = postService.findById(id);
        postService.updateHit(id);
        Post prePost = postService.findPrePost(id);
        Post nextPost = postService.findNextPost(id);

        model.addAttribute("post", post);
        model.addAttribute("nextPost", nextPost);
        model.addAttribute("prePost", prePost);

        List<Comment> commentList = commentService.findAllByPostId(id);
        model.addAttribute("commentList", commentList);

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

    @PostMapping("")
    public String savePost(@CurrentUser Member currentMember, @Validated @ModelAttribute(name = "post") PostVo postVo, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return "app/posts/new";
        }

        Post post = Post.builder()
                .title(postVo.getTitle())
                .content(postVo.getContent())
                .member(currentMember).build();

        postService.save(post);

        if (postVo.hasFile()) {
            fileService.saveAttachment(postVo.getFile(), post);
        }

        return "redirect:/posts/" + post.getId();
    }

    @GetMapping("/{id}/edit")
    public String updatePost(@PathVariable("id") Long id, Model model, @CurrentUser Member currentMember, RedirectAttributes redirAttrs) {
        Post post = postService.findById(id);

        if (!post.isSameMember(currentMember)) {
            redirAttrs.addFlashAttribute("error", "수정 권한이 없습니다.");
            return "redirect:/posts/" + post.getId();
        }
        model.addAttribute("post", post);

        return "app/posts/new";
    }

    @PutMapping("")
    public String updatePost(Long id, @ModelAttribute PostVo vo, @CurrentUser Member currentMember,
                             RedirectAttributes redirAttrs, BindingResult bindingResult) throws Exception {
        Post postForUpdate = postService.findById(id);

        if (bindingResult.hasErrors()) {
            return "redirect:/posts/" + postForUpdate.getId();
        }
        if (!postForUpdate.isSameMember(currentMember)) {
            redirAttrs.addFlashAttribute("error", "수정 권한이 없습니다.");
        }

        postForUpdate.update(vo);
        postService.save(postForUpdate);
        fileService.updateAttachment(postForUpdate, vo.getDeleteFileId(), vo.getFile());


        return "redirect:/posts/" + postForUpdate.getId();
    }

    @DeleteMapping("")
    public String delete(Long id, @CurrentUser Member currentMember, RedirectAttributes redirAttrs) {
        Post post = postService.findById(id);
        if (!post.isSameMember(currentMember)) {
            redirAttrs.addFlashAttribute("error", "삭제 권한이 없습니다.");
            return "redirect:/posts/" + post.getId();
        } else {
            postService.deleteById(id);
            return "redirect:/posts";
        }
    }
}