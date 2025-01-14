package eu.bigbank.dragons_of_mugloar.client.dto;

public record TaskResultDto(
        boolean success,
        int lives,
        int gold,
        int score,
        int highScore,
        int turn,
        String message
) { }
