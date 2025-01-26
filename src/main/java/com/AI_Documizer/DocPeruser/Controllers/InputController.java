package com.AI_Documizer.DocPeruser.Controllers;

import com.AI_Documizer.DocPeruser.Services.DocumentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/")
public class InputController {
    private final DocumentService documentService;

    public InputController( DocumentService documentService) {
        this.documentService=documentService;
    }

    @GetMapping("/")
    public String simplify(@RequestParam(value = "question") String question) {

        return documentService.getAnswer(question);
    }
}
