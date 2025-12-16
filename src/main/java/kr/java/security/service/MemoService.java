package kr.java.security.service;

import kr.java.security.model.entity.Memo;
import kr.java.security.model.entity.UserAccount;
import kr.java.security.model.repository.MemoRepository;
import kr.java.security.model.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public boolean updateMemo(Long id, String title, String content, String username) {
        Memo memo = getMemo(id);

        // 본인 글인지 확인
        if (!memo.getAuthor().getUsername().equals(username)) {
            return false;
        }

        memo.setTitle(title);
        memo.setContent(content);
        return true;
    }

    @Transactional
    public boolean deleteMemo(Long id, String username) {
        Memo memo = getMemo(id);

        if (!memo.getAuthor().getUsername().equals(username)) {
            return false;
        }

        memoRepository.delete(memo);
        return true;
    }
}