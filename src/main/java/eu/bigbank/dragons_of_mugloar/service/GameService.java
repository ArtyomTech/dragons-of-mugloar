package eu.bigbank.dragons_of_mugloar.service;

import eu.bigbank.dragons_of_mugloar.client.ApiClient;
import eu.bigbank.dragons_of_mugloar.client.dto.GameDto;
import eu.bigbank.dragons_of_mugloar.client.dto.ReputationDto;
import eu.bigbank.dragons_of_mugloar.client.dto.ShopItemDto;
import eu.bigbank.dragons_of_mugloar.client.dto.TaskDto;
import eu.bigbank.dragons_of_mugloar.config.GameSettingsConfig;
import eu.bigbank.dragons_of_mugloar.service.domain.AdventureResult;
import eu.bigbank.dragons_of_mugloar.service.domain.Task;
import eu.bigbank.dragons_of_mugloar.service.domain.TaskDifficulty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final TaskService taskService;
    private final ShopService shopService;
    private final SseService sseService;

    private final GameSettingsConfig gameSettingsConfig;
    private final ApiClient client;

    public void play() {
        int score = 0;
        int turn = 0;
        GameDto gameDto = client.startGame();
        String gameId = gameDto.gameId();
        sseService.send("Adventure started! Game ID: " + gameId);
        List<ShopItemDto> shopItems = shopService.getShopItems(gameId);
        AdventureResult adventureResult = new AdventureResult(gameDto.lives(), score, turn);
        do {
            adventureResult.setTurn(turn);
            List<TaskDto> tasks = taskService.getTasks(gameId);
            List<Task> difficultyTasks = tasks.stream()
                    .map(task -> new Task(
                            task,
                            TaskDifficulty.fromProbability(task.probability()),
                            false
                    ))
                    .toList();
            adventureResult = taskService.solveTasks(
                    gameId,
                    adventureResult,
                    difficultyTasks,
                    shopItems
            );
        } while (adventureResult.getScore() < gameSettingsConfig.getGoal() && adventureResult.getLives() > 0);

        if (adventureResult.getScore() >= gameSettingsConfig.getGoal()) {
            ReputationDto reputation = client.investigateReputation(gameId);
            sseService.send("Reputation result: " + reputation);
            sseService.send("Congratulations! Completed adventure with score: " + score);
        } else {
            sseService.send("Adventure failed with score: " + adventureResult.getScore());
        }
        sseService.complete();
    }

}
