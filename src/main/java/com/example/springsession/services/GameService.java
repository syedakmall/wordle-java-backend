package com.example.springsession.services;

import com.example.springsession.model.GuessResponse;
import com.example.springsession.model.WordStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

import java.util.*;

import static com.example.springsession.model.GameStatus.*;
import static com.example.springsession.model.WordStatus.*;

@Service
public class GameService {

    private static final List<String> words = List.of(
            "redis", "sprig", "model", "tests", "forts"
    );

    public String generateWord() {
        return words.get(new Random().nextInt(words.size()));
    }

    public GuessResponse guessWord(String userWord, String realWord, Integer count, HttpSession session) {

        if (count > 5) {
            session.invalidate();
            return new GuessResponse(null, null, null, FINISHED);
        }

        var statusWord = new ArrayList<Map<Character, WordStatus>>();
        var wordStatus = new LinkedHashMap<Character, WordStatus>();
        var gameStatus = IN_PROGRESS;
        if (userWord.equalsIgnoreCase(realWord)) {
            for (int i = 0; i < userWord.length(); i++) {
                wordStatus.put(userWord.charAt(i), CORRECT);
                statusWord.add((Map<Character, WordStatus>) wordStatus.clone());
                wordStatus.clear();
            }
            gameStatus = WIN;
            session.invalidate();
            return new GuessResponse(count, userWord, null, gameStatus);
        }

        for (int j = 0; j < userWord.length(); j++) {
            if (userWord.charAt(j) == realWord.charAt(j)) {
                wordStatus.put(userWord.charAt(j), CORRECT);
                statusWord.add((Map<Character, WordStatus>) wordStatus.clone());
                wordStatus.clear();
            } else {
                for (int k = 1; k < realWord.length(); k++) {
                    if (userWord.charAt(j) == realWord.charAt(k)) {
                        wordStatus.put(userWord.charAt(j), MISPLACED);
                        statusWord.add((Map<Character, WordStatus>) wordStatus.clone());
                        wordStatus.clear();
                        break;
                    }
                    if (k == 4) {
                        wordStatus.put(userWord.charAt(j), NOT_AVAILABLE);
                        statusWord.add((Map<Character, WordStatus>) wordStatus.clone());
                        wordStatus.clear();
                    }
                }
            }
        }

        return new GuessResponse(count, userWord, statusWord, gameStatus);
    }


}
