package eu.bigbank.dragons_of_mugloar.client.dto;

public record TaskDto(
        String adId,
        String message,
        int reward,
        int expiresIn,
        String probability
) { }
