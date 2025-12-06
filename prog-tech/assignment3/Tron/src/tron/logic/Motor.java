package tron.logic;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a player's light cycle (motor) in the game.
 * Manages position, direction, color, and trail.
 */
public class Motor {
    private final String name;
    private final Color color;
    private Position currentPosition;
    private Direction currentDirection;
    private final List<Position> trail;
    private boolean crashed;

    public Motor(String name, Color color, Position startPosition, Direction startDirection) {
        this.name = name;
        this.color = color;
        this.currentPosition = startPosition;
        this.currentDirection = startDirection;
        this.trail = new ArrayList<>();
        this.trail.add(startPosition);
        this.crashed = false;
    }

    public void move() {
        if (crashed)
            return;

        int dx = 0;
        int dy = 0;
        switch (currentDirection) {
            case UP -> dy = -1;
            case DOWN -> dy = 1;
            case LEFT -> dx = -1;
            case RIGHT -> dx = 1;
        }

        currentPosition = new Position(currentPosition.x() + dx, currentPosition.y() + dy);
        trail.add(currentPosition);
    }

    public void setDirection(Direction newDirection) {
        if (currentDirection == Direction.UP && newDirection == Direction.DOWN)
            return;
        if (currentDirection == Direction.DOWN && newDirection == Direction.UP)
            return;
        if (currentDirection == Direction.LEFT && newDirection == Direction.RIGHT)
            return;
        if (currentDirection == Direction.RIGHT && newDirection == Direction.LEFT)
            return;

        this.currentDirection = newDirection;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public Position getPosition() {
        return currentPosition;
    }

    public List<Position> getTrail() {
        return Collections.unmodifiableList(trail);
    }

    public boolean isCrashed() {
        return crashed;
    }

    public void setCrashed(boolean crashed) {
        this.crashed = crashed;
    }
}
