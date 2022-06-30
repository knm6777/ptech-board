package kr.co.board.controller;

import kr.co.board.model.MemberCreateForm;
import kr.co.board.model.vo.MemberVo;
import kr.co.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("memberVo", new MemberVo());
        return "app/users/login";
    }

    @GetMapping("/new")
    public String newMember(Model model) {
        model.addAttribute("memberCreateForm", new MemberCreateForm());
        return "app/users/new";
    }


    @PostMapping("/new")
    public String signup(@Validated @ModelAttribute MemberCreateForm memberCreateForm, BindingResult bindingResult) {
        // 양식 오류
        if (bindingResult.hasErrors()) {
            return "app/users/new";
        }

        if (!memberCreateForm.getPassword().equals(memberCreateForm.getPasswordCheck())) {
            bindingResult.rejectValue("passwordCheck", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "app/users/new";
        }

        MemberVo mvo = new MemberVo();
        mvo.setEmail(memberCreateForm.getEmail());
        mvo.setUsername(memberCreateForm.getUsername());
        mvo.setPassword(memberCreateForm.getPassword());

        // db 오류
        try {
            memberService.save(mvo);
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
