package kr.co.board.service;

import kr.co.board.model.Member;
import kr.co.board.model.MemberAdapter;
import kr.co.board.model.Role;
import kr.co.board.model.enums.Author;
import kr.co.board.model.vo.MemberVo;
import kr.co.board.repository.RoleRepository;
import kr.co.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email: " + email + " not found"));
        return new MemberAdapter(member);
    }

    @Transactional
    public void save(MemberVo vo) {
        Member member = Member.builder()
                .email(vo.getEmail())
                .password(passwordEncoder.encode(vo.getPassword()))
                .username(vo.getUsername()).build();
        memberRepository.save(member);
        Role role = Role.builder()
                .author(Author.MEMBER)
                .member(member).build();
        member.getRoles().add(role);
        roleRepository.save(role);
    }

    public String passwordEncoding(String password) {
        return passwordEncoder.encode(password);
    }

    public void update(Member member) {
        memberRepository.save(member);
    }

    public boolean checkPassword(Long id, String password) {
        Member member = memberRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        String realPassword = member.getPassword();
        return passwordEncoder.matches(password, realPassword);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    @PostConstruct
    public void initialize() {
        Optional<Member> admin = memberRepository.findByEmail("admin@admin.com");
        if (!admin.isPresent()) {
            Member mem1 = Member.builder()
                    .email("admin@admin.com")
                    .username("admin")
                    .password(passwordEncoder.encode("admin")).build();
            memberRepository.save(mem1);
            Role adminRole = Role.builder()
                    .author(Author.ADMIN)
                    .member(mem1).build();
            roleRepository.save(adminRole);
        }

        Optional<Member> testUser = memberRepository.findByEmail("test@test.com");
        if (!testUser.isPresent()) {
            Member mem2 = Member.builder()
                    .email("test@test.com")
                    .username("test")
                    .password(passwordEncoder.encode("test")).build();
            memberRepository.save(mem2);
            Role testUserRole = Role.builder()
                    .author(Author.MEMBER)
                    .member(mem2).build();
            roleRepository.save(testUserRole);
        }
    }
}
