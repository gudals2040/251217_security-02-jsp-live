package kr.java.security.service;

import kr.java.security.model.entity.Memo;
import kr.java.security.model.entity.UserAccount;
import kr.java.security.model.repository.MemoRepository;
import kr.java.security.model.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
// org.springframework.transaction.annotation.Transactional;
@Transactional(readOnly = true)
public class MemoService {

    private final MemoRepository memoRepository;
    private final UserAccountRepository userAccountRepository;

    public List<Memo> getMyMemos(String username) {
        UserAccount user = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return memoRepository.findByAuthorIdWithAuthor(user.getId());
    }

    public Memo getMemo(Long id) {
        return memoRepository.findByIdWithAuthor(id)
                .orElseThrow(() -> new IllegalArgumentException("메모를 찾을 수 없습니다."));
    }

    @Transactional
    public Memo createMemo(String title, String content, String username) {
        UserAccount author = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Memo memo = new Memo();
        memo.setTitle(title);
        memo.setContent(content);
        memo.setAuthor(author);

        return memoRepository.save(memo);
    }

    // #(1)-8
    @Transactional
    // @Secured() <- 생략 - ROLE
    @PreAuthorize("#username == authentication.name") // SpEL
    // #username -> 메서드 인자로 전달 받은 값 -> 글의 유저네임
    // authentication -> authentication.name 로그인 세션에 저장된 username
//    @PreAuthorize("#username == principal.username")
//    @PostAuthorize()
    public void updateMemo(Long id, String title, String content, String username) {
        Memo memo = getMemo(id);
        memo.setTitle(title);
        memo.setContent(content);
    }

    // #(1)-9
    @Transactional
    @PreAuthorize("#username == authentication.name or hasRole('ADMIN')") // 2개 이상의 조건
    public void deleteMemo(Long id, String username) {
        Memo memo = getMemo(id);
        memoRepository.delete(memo);
    }
}