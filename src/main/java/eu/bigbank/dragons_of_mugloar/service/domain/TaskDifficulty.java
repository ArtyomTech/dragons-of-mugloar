package eu.bigbank.dragons_of_mugloar.service.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum TaskDifficulty {

    DIFFICULTY_1("Sure thing", 1),
    DIFFICULTY_2("Piece of cake", 1),
    DIFFICULTY_3("Quite likely", 2),
    DIFFICULTY_4("Walk in the park", 2),
    DIFFICULTY_5("Gamble", 3),
    DIFFICULTY_6("Hmmm....", 3),
    DIFFICULTY_7("Risky", 4),
    DIFFICULTY_8("Rather detrimental", 4),
    DIFFICULTY_9("Playing with fire", 5),
    DIFFICULTY_10("Suicide mission", 5),
    UNKNOWN_DIFFICULTY("Unknown", 6);

    private final String probability;
    private final int difficulty;

    public static TaskDifficulty fromProbability(String probability) {
        return Arrays.stream(TaskDifficulty.values())
                .filter(difficulty -> difficulty.probability.equals(probability))
                .findAny()
                .orElse(UNKNOWN_DIFFICULTY);
    }

}
