package com.back.domain.post.post;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PostController {
    @GetMapping("/posts/write")
    @ResponseBody
    public String write() {
        return """
                <form action="/posts/doWrite" target="_blank">
                    <input type="text" name="title" placeholder="제목" value="안녕"><br>
                    <textarea name="content" placeholder="내용">반가워.</textarea><br>
                    <button type="submit">작성</button>
                </form>
                """;
    }
}
