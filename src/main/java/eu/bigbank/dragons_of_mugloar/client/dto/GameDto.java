package eu.bigbank.dragons_of_mugloar.client.dto;

public record GameDto(
        String gameId,
        int lives,
        int gold,
        int level,
        int score,
        int highScore,
        int turn
) { }
