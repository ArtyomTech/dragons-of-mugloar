package eu.bigbank.dragons_of_mugloar.client.dto;

public record PurchaseResultDto(
        boolean shoppingSuccess,
        int gold,
        int lives,
        int level,
        int turn
) { }
