package kr.co.board.controller;

import kr.co.board.model.vo.MemberVo;
import kr.co.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {

        // 세션이 이미 있다면 그 세션을 돌려주고 없으면 새로 생성
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
