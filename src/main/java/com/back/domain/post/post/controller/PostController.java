package com.back.domain.post.post.controller;

import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    private String getWriteFormHtml() {
        return getWriteFormHtml("", "", "", "");
    }

    private String getWriteFormHtml(String errorFieldName, String errorMessage, String title, String content) {
        return """
                <div style="color:red;">%s</div>
                <form action="doWrite" method="POST">
                    <input type="text" name="title" placeholder="제목" value="%s" autofocus><br>
                    <textarea name="content" placeholder="내용">%s</textarea><br>
                    <button type="submit">작성</button>
                </form>
                
                <script>
                    const errorFieldName = "%s";
                    if(errorFieldName.length > 0) {
                        // 현재까지 나온 모든 폼 검색
                        const forms = document.querySelectorAll("form");
                        // 그 중에서 가장 마지막에 나온 폼을 선택
                        const lastForm = forms[forms.length - 1];
                        // 해당 폼의 에러 필드에 포커스
                        lastForm[errorFieldName].focus();
                    }
                </script>
                """.formatted(errorMessage, title, content, errorFieldName);
    }

    @GetMapping("/posts/write")
    @ResponseBody
    public String showWrite() {
        return getWriteFormHtml();
    }

    @PostMapping("/posts/doWrite")
    @ResponseBody
    @Transactional
    public String doWrite(@RequestParam(defaultValue = "") String title, @RequestParam(defaultValue = "") String content) {
        if (title.isBlank()) return getWriteFormHtml("title", "제목을 입력해주세요.", title, content);
        if (title.length() < 2) return getWriteFormHtml("title", "제목은 2글자 이상이어야 합니다.", title, content);
        if (title.length() > 20) return getWriteFormHtml("title", "제목은 최대 20글자까지 가능합니다.", title, content);
        if (content.isBlank()) return getWriteFormHtml("content", "내용을 입력해주세요.", title, content);
        if (content.length() < 2) return getWriteFormHtml("content", "내용은 2글자 이상이어야 합니다.", title, content);
        if (content.length() > 100) return getWriteFormHtml("content", "내용은 최대 100글자까지 가능합니다.", title, content);

        Post post = postService.write(title, content);

        return "%d번 글이 작성되었습니다.".formatted(post.getId());
    }
}
