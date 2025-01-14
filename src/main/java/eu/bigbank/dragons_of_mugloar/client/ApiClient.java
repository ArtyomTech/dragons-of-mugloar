package eu.bigbank.dragons_of_mugloar.client;

import eu.bigbank.dragons_of_mugloar.client.dto.*;
import eu.bigbank.dragons_of_mugloar.client.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ApiClient {

    private final WebClient client;

    public GameDto startGame() {
        return client.post()
                .uri("/game/start")
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> Mono.error(new GameStartException("Failed to start game"))
                )
                .bodyToMono(GameDto.class)
                .block();
    }

    public ReputationDto investigateReputation(String gameId) {
        return client.post()
                .uri("/{gameId}/investigate/reputation", gameId)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> Mono.error(new ReputationInvestigationException(
                                "Failed to investigate reputation"
                        ))
                )
                .bodyToMono(ReputationDto.class)
                .block();
    }

    public List<TaskDto> getTasks(String gameId) {
        return client.get()
                .uri("/{gameId}/messages", gameId)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> Mono.error(new TaskRetrievalException("Failed to retrieve tasks"))
                )
                .bodyToFlux(TaskDto.class)
                .collectList()
                .block();
    }

    public TaskResultDto solveTask(String gameId, String taskId) {
        return client.post()
                .uri("/{gameId}/solve/{taskId}", gameId, taskId)
                .retrieve()
                .onStatus(
                        status -> status.value() == 410,
                        response -> Mono.error(new AdventureException("Adventure failed"))
                )
                .onStatus(
                        HttpStatusCode::isError,
                        response -> Mono.error(new TaskSolveException("Failed to solve task"))
                )
                .bodyToMono(TaskResultDto.class)
                .block();
    }

    public List<ShopItemDto> getShopItems(String gameId) {
        return client.get()
                .uri("/{gameId}/shop", gameId)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> Mono.error(new ShopItemsRetrievalException(
                                "Failed to retrieve shop items"
                        ))
                )
                .bodyToFlux(ShopItemDto.class)
                .collectList()
                .block();
    }

    public PurchaseResultDto purchaseItem(String gameId, String itemId) {
        return client.post()
                .uri("/{gameId}/shop/buy/{itemId}", gameId, itemId)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> Mono.error(new PurchaseItemException("Failed to purchase the item"))
                )
                .bodyToMono(PurchaseResultDto.class)
                .block();
    }

}
