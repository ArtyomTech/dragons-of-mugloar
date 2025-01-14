package eu.bigbank.dragons_of_mugloar.service;

import eu.bigbank.dragons_of_mugloar.client.ApiClient;
import eu.bigbank.dragons_of_mugloar.client.dto.PurchaseResultDto;
import eu.bigbank.dragons_of_mugloar.client.dto.ShopItemDto;
import eu.bigbank.dragons_of_mugloar.client.dto.TaskResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ShopService {

    private static final String HEALING_POTION_ITEM_NAME = "Healing potion";

    private final SseService sseService;
    private final ApiClient client;

    public List<ShopItemDto> getShopItems(String gameId) {
        return client.getShopItems(gameId);
    }

    public Optional<ShopItemDto> purchaseHealingPotion(
            String gameId,
            TaskResultDto taskResult,
            List<ShopItemDto> shopItems
    ) {
        return shopItems.stream()
                .filter(shopItem -> HEALING_POTION_ITEM_NAME.equals(shopItem.name()))
                .filter(healingPotion -> taskResult.gold() >= healingPotion.cost())
                .findAny()
                .flatMap(healingPotion -> {
                    PurchaseResultDto purchaseResult = client.purchaseItem(gameId, healingPotion.id());
                    sseService.send("Purchase result: " + purchaseResult);

                    return purchaseResult.shoppingSuccess() ? Optional.of(healingPotion) : Optional.empty();
                });
    }

}
