package com.back.domain.post.post.controller;

import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    private String getWriteFormHtml() {
        return getWriteFormHtml("", "", "");
    }

    private String getWriteFormHtml(String errorMessage, String title, String content) {
        return """
                <ul style="color:red;">%s</ul>
                <form action="doWrite" method="POST">
                    <input type="text" name="title" placeholder="제목" value="%s" autofocus><br>
                    <textarea name="content" placeholder="내용">%s</textarea><br>
                    <button type="submit">작성</button>
                </form>
                
                <script>
                    // 현재까지 나온 모든 폼 검색
                    const forms = document.querySelectorAll("form");
                    // 그 중에서 가장 마지막에 나온 폼을 선택
                    const lastForm = forms[forms.length - 1];
                    
                    const errorFieldName = lastForm.previousElementSibling?.querySelector('li')?.dataset?.errorFieldName || '';
                    if(errorFieldName.length > 0) {
                        // 해당 폼의 에러 필드에 포커스
                        lastForm[errorFieldName].focus();
                    }
                </script>
                """.formatted(errorMessage, title, content);
    }

    @GetMapping("/posts/write")
    @ResponseBody
    public String showWrite() {
        return getWriteFormHtml();
    }


    @Getter
    @AllArgsConstructor
    public static class WriteForm {
        @NotBlank(message = "1-제목을 입력해주세요.")
        @Size(min = 2, max = 20, message = "2-제목은 2자 이상, 20자 이하로 입력 가능합니다.")
        private String title;

        @NotBlank(message = "3-내용을 입력해주세요.")
        @Size(min = 2, max = 20, message = "4-내용은 2자 이상, 20자 이하로 입력 가능합니다.")
        private String content;
    }

    @PostMapping("/posts/doWrite")
    @ResponseBody
    @Transactional
    public String doWrite(@Valid WriteForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> (fieldError.getField() + "-" + fieldError.getDefaultMessage()).split("-"))
                    .map(field -> "<!--%s--><li data-error-field-name=\"%s\">%s</li>".formatted(field[1], field[0], field[2]))
                    .sorted()
                    .collect(Collectors.joining("\n"));
            return getWriteFormHtml(errorMessage, form.getTitle(), form.getContent());
        }
        Post post = postService.write(form.getTitle(), form.getContent());

        return "%d번 글이 작성되었습니다.".formatted(post.getId());
    }
}
