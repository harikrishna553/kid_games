package com.example.kidsspellinggame.controller;

import com.example.kidsspellinggame.model.CheckRequest;
import com.example.kidsspellinggame.model.CheckResponse;
import com.example.kidsspellinggame.model.Word;
import com.example.kidsspellinggame.service.SpellingGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class SpellingGameController {

    @Autowired
    private SpellingGameService spellingGameService;

    @GetMapping("/")
    public String home() {
        return "redirect:/home.html";
    }

    @GetMapping("/api/word")
    @ResponseBody
    public Word getRandomWord() {
        return spellingGameService.getRandomWord();
    }

    @PostMapping("/api/check")
    @ResponseBody
    public CheckResponse checkAnswer(@RequestBody CheckRequest request) {
        boolean isCorrect = spellingGameService.checkAnswer(request.getUserInput(), request.getOriginalWord());
        
        String message;
        if (isCorrect) {
            message = "🎉 Excellent! You got it right!";
        } else {
            message = "😊 Try again! You can do it!";
        }
        
        return new CheckResponse(isCorrect, message);
    }
}
