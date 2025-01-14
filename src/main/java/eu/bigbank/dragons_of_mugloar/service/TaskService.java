package eu.bigbank.dragons_of_mugloar.service;

import eu.bigbank.dragons_of_mugloar.client.ApiClient;
import eu.bigbank.dragons_of_mugloar.client.dto.ShopItemDto;
import eu.bigbank.dragons_of_mugloar.client.dto.TaskDto;
import eu.bigbank.dragons_of_mugloar.client.dto.TaskResultDto;
import eu.bigbank.dragons_of_mugloar.config.GameSettingsConfig;
import eu.bigbank.dragons_of_mugloar.service.domain.AdventureResult;
import eu.bigbank.dragons_of_mugloar.service.domain.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final ShopService shopService;
    private final SseService sseService;

    private final GameSettingsConfig gameSettingsConfig;
    private final ApiClient client;

    public List<TaskDto> getTasks(String gameId) {
        return client.getTasks(gameId);
    }

    public AdventureResult solveTasks(
            String gameId,
            AdventureResult adventureResult,
            List<Task> tasks,
            List<ShopItemDto> shopItems
    ) {
        if (adventureResult.getScore() >= gameSettingsConfig.getGoal() || adventureResult.getLives() == 0) {
            return adventureResult;
        }

        return tasks.stream()
                .filter(task -> task.getTask().expiresIn() >= adventureResult.getTurn() && !task.isTriedToSolve())
                .min(compareByDifficulty().thenComparing(compareByReward()))
                .map(task -> solveTask(gameId, task, shopItems))
                .map(taskResult -> {
                    int nextTurns = adventureResult.getTurn() + gameSettingsConfig.getTurnSlack() +
                            (taskResult.isHealingPotionPurchased() ? 1 : 0);
                    AdventureResult updatedResult = new AdventureResult(
                            taskResult.taskResult().lives(),
                            taskResult.taskResult().score(),
                            nextTurns // Update the turns in AdventureResult
                    );
                    return solveTasks(gameId, updatedResult, tasks, shopItems);
                })
                .orElse(adventureResult);
    }


    private record TaskResult(
            TaskResultDto taskResult,
            boolean isHealingPotionPurchased
    ) { }

    private TaskResult solveTask(String gameId, Task bestTask, List<ShopItemDto> shopItems) {
        TaskResultDto taskResult = client.solveTask(gameId, bestTask.getTask().adId());
        bestTask.setTriedToSolve(true);
        sseService.send("Task result: " + taskResult);
        boolean isHealingPotionPurchased = taskResult.lives() < 3
                && shopService.purchaseHealingPotion(gameId, taskResult, shopItems).isPresent();

        return new TaskResult(taskResult, isHealingPotionPurchased);
    }

    private Comparator<Task> compareByDifficulty() {
        return Comparator.comparingInt(task -> task.getTaskDifficulty().getDifficulty());
    }

    private Comparator<Task> compareByReward() {
        return Comparator.comparingInt((Task task) -> task.getTask().reward()).reversed();
    }

}
