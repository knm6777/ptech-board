package kr.co.board.controller;

import kr.co.board.model.Member;
import kr.co.board.model.Post;
import kr.co.board.model.dto.PostDto;
import kr.co.board.model.vo.PostVo;
import kr.co.board.service.PostService;
import kr.co.board.util.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @GetMapping("")
    public String index(Model model) {
        List<Post> postList = postService.getPosts();
        model.addAttribute("postList", postList);
        return "app/posts/index";
    }

    @GetMapping("/new")
    public String newPost(Model model) {
        PostVo post = new PostVo();
        model.addAttribute("post", post);
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
        return "redirect:/posts/details?id=" + post.getId();
    }

    @GetMapping("/details")
    public String show(@RequestParam Long id, Model model) {
        Post post = postService.findById(id);
        model.addAttribute("post", post);
        //this.activateNav(model);
        return "app/posts/detail";
    }
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model, @CurrentUser Member currentMember) throws Exception {
        Post post = postService.findById(id);
        if (!post.isSameMember(currentMember)) {
            throw new Exception("수정 권한이 없습니다.");
        }
        model.addAttribute("post", post);
        this.activateNav(model);
        return "app/posts/new";
    }

    @PutMapping("/{id}")
    @ResponseBody
    public PostDto update(@PathVariable("id") Long id, @ModelAttribute PostVo vo, @CurrentUser Member currentMember) throws Exception {
        Post postForUpdate = postService.findById(id);
        if (!postForUpdate.isSameMember(currentMember)) {
            throw new Exception("수정 권한이 없습니다.");
        }
        postForUpdate.update(vo);
        postService.save(postForUpdate);
        return new PostDto(postForUpdate);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody PostDto delete(@PathVariable("id") Long id, @CurrentUser Member currentMember) throws Exception {
        Post post = postService.findById(id);
        if (!post.isSameMember(currentMember)) {
            throw new Exception("삭제 권한이 없습니다.");
        }
        return new PostDto(postService.deleteById(id));
    }

    public void activateNav(Model model) {
        model.addAttribute("navActive", "posts");
    }
}