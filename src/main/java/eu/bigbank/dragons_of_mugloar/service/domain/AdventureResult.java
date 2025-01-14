package eu.bigbank.dragons_of_mugloar.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AdventureResult {

    private int lives;
    private int score;
    private int turn;

}
