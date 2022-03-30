package com.example.springsession.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuessResponse {
    private Integer tries;
    private String word;
    private List<Map<Character, WordStatus>> wordStatus;
    private GameStatus gameStatus;
}
