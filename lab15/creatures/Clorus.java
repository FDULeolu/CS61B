package creatures;

import huglife.*;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Clorus extends Creature {

    public Clorus(double e) {
        super("clorus");
        energy = e;
    }

    @Override
    public void move() {
        energy -= 0.03;
    }

    @Override
    public void attack(Creature c) {
        energy += c.energy();
    }

    @Override
    public Clorus replicate() {
        Clorus cbaby = new Clorus(energy / 2);
        energy /= 2;
        return cbaby;
    }

    @Override
    public void stay() {
        energy -= 0.01;
    }

    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        List<Direction> plips = getNeighborsOfType(neighbors, "plip");
        if (empties.isEmpty()) {
            return new Action(Action.ActionType.STAY);
        } else if (!plips.isEmpty()) {
            return new Action(Action.ActionType.ATTACK, plips.get(HugLifeUtils.randomInt(plips.size() - 1)));
        } else if (this.energy >= 1) {
            return new Action(Action.ActionType.REPLICATE, empties.get(HugLifeUtils.randomInt(empties.size() - 1)));
        } else {
            return new Action(Action.ActionType.MOVE, empties.get(HugLifeUtils.randomInt(empties.size() - 1)));
        }
    }

    @Override
    public Color color() {
        return color(34, 0, 231);
    }
}
