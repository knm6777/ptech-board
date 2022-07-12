package kr.co.board.controller;

import kr.co.board.model.Member;
import kr.co.board.model.Post;
import kr.co.board.model.vo.MemberVo;
import kr.co.board.service.CommentService;
import kr.co.board.service.MemberService;
import kr.co.board.service.PostService;
import kr.co.board.util.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Controller
@Validated
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final CommentService commentService;
    private final PostService postService;

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage();
                log.error("ERROR ====== " + errorMessage);
            }
        }
        model.addAttribute("errorMessage", errorMessage);

        return "app/users/login";
    }

    @GetMapping("/new")
    public String newMember(Model model) {
        model.addAttribute("memberVo", new MemberVo());
        return "app/users/new";
    }

    @GetMapping("/mypage")
    public String indexMypage(@CurrentUser Member member, Model model, @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Post> postPage = postService.findAllByMemberId(member.getId(), pageable);

        model.addAttribute("comments", commentService.findAllByMember(member));
        model.addAttribute("posts", postPage);
        model.addAttribute("member", member);

        int totalPages = postPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "app/users/mypage";
    }
    
    @PostMapping("")
    public String saveMember(@Validated @ModelAttribute MemberVo memberVo, BindingResult bindingResult) {
        // 양식 오류
        if (bindingResult.hasErrors()) {
            return "app/users/new";
        }

        if (!memberVo.getPassword().equals(memberVo.getPasswordCheck())) {
            bindingResult.rejectValue("passwordCheck", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "app/users/new";
        }

        // db 오류
        try {
            memberService.save(memberVo);
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "app/users/new";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "app/users/new";
        }
        return "redirect:/";
    }
}
