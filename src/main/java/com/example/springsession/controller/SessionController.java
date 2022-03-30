package com.example.springsession.controller;

import com.example.springsession.model.GuessResponse;
import com.example.springsession.services.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static com.example.springsession.model.GameStatus.FINISHED;

@RestController
@Slf4j
public class SessionController {

    @Autowired
    private GameService gameService;

    @GetMapping("start")
    public String sessionCount(HttpSession session, @SessionAttribute(value = "word", required = false) String word) {
        if (word != null) return "already started game!";
        session.setAttribute("word", gameService.generateWord());
        return "started";
    }

    @PostMapping("play")
    public GuessResponse guessWord(HttpSession session, @RequestParam String userWord, @SessionAttribute(value = "word") String word, @SessionAttribute(value = "tries", required = false) Integer tries) {
        if (tries == null) {
            session.setAttribute("tries", 1);
            return gameService.guessWord(userWord, word, (Integer) session.getAttribute("tries"), session);
        }
        session.setAttribute("tries", ++tries);
        return gameService.guessWord(userWord, word, tries, session);
    }


}
