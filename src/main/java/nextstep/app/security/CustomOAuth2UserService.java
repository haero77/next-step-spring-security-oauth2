package nextstep.app.security;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.oauth2.CustomOAuth2User;
import nextstep.security.oauth2.GitHubUserInfo;
import nextstep.security.oauth2.OAuth2User;
import nextstep.security.oauth2.OAuth2UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService implements OAuth2UserService {

    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(GitHubUserInfo gitHubUserInfo) {
        String username = getFormattedUsername(gitHubUserInfo);

        Optional<Member> memberOpt = memberRepository.findByEmail(username);
        if (memberOpt.isPresent()) {
            return new CustomOAuth2User(memberOpt.get().getEmail());
        }

        // 신규 회원인 경우 회원가입 처리
        Member newMember = Member.byOAuth2Joining(username);
        memberRepository.save(newMember);
        return new CustomOAuth2User(newMember.getEmail());
    }

    private String getFormattedUsername(GitHubUserInfo gitHubUserInfo) {
        return "github_%d".formatted(gitHubUserInfo.getId());
    }
}
