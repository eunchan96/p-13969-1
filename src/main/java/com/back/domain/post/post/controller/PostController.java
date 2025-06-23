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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/posts/write")
    public String showWrite(@ModelAttribute("form") WriteForm form) {
        return "post/post/write";
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
    @Transactional
    public String doWrite(@ModelAttribute("form") @Valid WriteForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "post/post/write";
        }
        Post post = postService.write(form.getTitle(), form.getContent());
        model.addAttribute("post", post);

        return "post/post/writeDone";
    }
}
