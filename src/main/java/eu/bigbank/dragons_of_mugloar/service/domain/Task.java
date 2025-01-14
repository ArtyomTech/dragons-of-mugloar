package eu.bigbank.dragons_of_mugloar.service.domain;

import eu.bigbank.dragons_of_mugloar.client.dto.TaskDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Task {

    private TaskDto task;
    private TaskDifficulty taskDifficulty;
    private boolean isTriedToSolve;

}
