package kr.co.board.controller;

import kr.co.board.model.Member;
import kr.co.board.model.Post;
import kr.co.board.model.helper.Pagination;
import kr.co.board.model.vo.MemberEditVo;
import kr.co.board.model.vo.MemberVo;
import kr.co.board.service.CommentService;
import kr.co.board.service.MemberService;
import kr.co.board.service.PostService;
import kr.co.board.model.helper.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@Validated
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final CommentService commentService;
    private final PostService postService;

    private final AuthenticationManager authenticationManager;

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
        Page<Post> postPage = postService.findAllByMember(member, pageable);

        String url = "/member/mypage";
        Pagination pagination = new Pagination(postPage, pageable, url);

        model.addAttribute("comments", commentService.findAllByMember(member));
        model.addAttribute("pagination", pagination);
        model.addAttribute("member", member);

        return "app/users/mypage";
    }

    @GetMapping("/updatepwd")
    public String updatePwd() {
        return "app/users/updatepwd";
    }

    @PostMapping("/updatepwd")
    @ResponseBody
    public boolean updatePassword(@CurrentUser Member member, @RequestParam String curPassword, @RequestParam String changePassword) {
        log.info("checkPwd 진입");
        if(memberService.checkPassword(member.getId(), curPassword)) {
            String password = memberService.passwordEncoding(changePassword);
            member.setPassword(password);
            memberService.update(member);

            return true;
        }else {
            return false;
        }
    }

    @GetMapping("/update")
    public String update(@CurrentUser Member member, Model model) {
        model.addAttribute("member", member);

        return "app/users/update";
    }

    @PutMapping("")
    @ResponseBody
    public boolean update(@RequestBody MemberEditVo memberEditVo) {

        log.info("MemberController 진입");
        Member member = memberService.findById(memberEditVo.getId());
        // 회원 정보 수정
        member.update(memberEditVo);
        memberService.update(member);

        return true;
    }

    @PostMapping("")
    public String save(@Validated @ModelAttribute MemberVo memberVo, BindingResult bindingResult) {
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
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "app/users/new";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "app/users/new";
        }
        return "redirect:/";
    }

    @PostMapping("/delete")
    @ResponseBody
    public boolean delete(@CurrentUser Member member, HttpSession session) {
        postService.deleteByMember(member);
        commentService.deleteByMember(member);
        memberService.deleteById(member.getId());

        session.invalidate();

        return true;
    }
}
