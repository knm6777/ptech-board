package kr.co.board.controller;

import kr.co.board.model.Member;
import kr.co.board.model.Post;
import kr.co.board.model.vo.PostVo;
import kr.co.board.service.PostService;
import kr.co.board.util.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @GetMapping("/new")
    public String newPost(Model model) {
        PostVo post = new PostVo();
        model.addAttribute("post", post);
        this.activateNav(model);
        return "app/posts/new";
    }

    @PostMapping("")
    public String create(@ModelAttribute PostVo vo, @CurrentUser Member currentMember) throws IOException {
        Post post = Post.builder()
                .title(vo.getTitle())
                .content(vo.getContent())
                .member(currentMember).build();
        postService.save(post);
//        if (vo.hasFile()) {
        return "redirect:/posts/" + post.getId();
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        Post post = postService.findById(id);
        model.addAttribute("post", post);
        this.activateNav(model);
        return "app/posts/show";
    }

    public void activateNav(Model model) {
        model.addAttribute("navActive", "posts");
    }
}