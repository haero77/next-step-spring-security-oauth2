package nextstep.app.security;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.oauth2.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository, SecurityOAuth2Properties oAuth2Properties) {
        super(oAuth2Properties);
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String username = getFormattedUsername(oAuth2User);

        Optional<Member> memberOpt = memberRepository.findByEmail(username);
        if (memberOpt.isPresent()) {
            return new CustomOAuth2User(memberOpt.get().getEmail());
        }

        // 신규 회원인 경우 회원가입 처리
        Member newMember = Member.byOAuth2Joining(username);
        memberRepository.save(newMember);
        return new CustomOAuth2User(newMember.getEmail());
    }

    private String getFormattedUsername(OAuth2User oAuth2User) {
        return "github_%s".formatted(oAuth2User.getName());
    }
}
