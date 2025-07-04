package com.example.kidsspellinggame.controller;

import com.example.kidsspellinggame.model.CheckRequest;
import com.example.kidsspellinggame.model.CheckResponse;
import com.example.kidsspellinggame.model.Word;
import com.example.kidsspellinggame.service.SpellingGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SpellingGameController {

    @Autowired
    private SpellingGameService spellingGameService;

    @GetMapping("/word")
    public Word getRandomWord() {
        return spellingGameService.getRandomWord();
    }

    @PostMapping("/check")
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
