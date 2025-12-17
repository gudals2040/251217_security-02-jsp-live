package kr.java.security.controller;

import kr.java.security.model.entity.Memo;
import kr.java.security.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/memo")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @GetMapping
    // org.springframework.ui.Model;
    public String list(Principal principal, Model model) {
        // principal.getName() = 로그인한 사용자의 username
        List<Memo> memos = memoService.getMyMemos(principal.getName());
        model.addAttribute("memos", memos);
        return "memo/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Principal principal, Model model) {
        Memo memo = memoService.getMemo(id);

        boolean isOwner = memo.getAuthor().getUsername().equals(principal.getName());

        model.addAttribute("memo", memo);
        model.addAttribute("isOwner", isOwner);
        return "memo/detail";
    }

    @GetMapping("/new")
    public String createForm() {
        return "memo/form";
    }

    @PostMapping("/new")
    public String create(
            @RequestParam String title,
            @RequestParam String content,
            Principal principal) {

        memoService.createMemo(title, content, principal.getName());
        return "redirect:/memo";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Principal principal, Model model) {
        Memo memo = memoService.getMemo(id);

        if (!memo.getAuthor().getUsername().equals(principal.getName())) {
            return "redirect:/memo";
        }

        model.addAttribute("memo", memo);
        return "memo/edit";
    }

    // #(1)-5
    private String getAuthorName(Long memoId) {
        // 작성자 이름 받아오기
        return memoService.getMemo(memoId).getAuthor().getUsername();
    }

    // #(1)-6
    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String content) {

        // 작성자의 이름을 넣어줌
        memoService.updateMemo(id, title, content, getAuthorName(id));
        // 예외 발생 시 이 흐름을 벗어남 (Spring Security)
        return "redirect:/memo/" + id;
    }

    // #(1)-7
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        memoService.deleteMemo(id, getAuthorName(id));
        return "redirect:/memo";
    }
}