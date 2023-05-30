package member.application;

import java.util.List;
import java.util.Optional;
import member.dao.MemberDao;
import member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberQueryService {

    private final MemberDao memberDao;

    public MemberQueryService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<Member> getAllMembers() {
        return memberDao.getAllMembers();
    }

    public Optional<Member> findByEmailAndPassword(String email, String password) {
        return memberDao.findByEmailAndPassword(email, password);
    }
}