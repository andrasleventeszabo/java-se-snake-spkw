package com.codecool.snake.entities.enemies;

import com.codecool.snake.Globals;
import com.codecool.snake.Utils;
import com.codecool.snake.entities.Animatable;
import com.codecool.snake.entities.GameEntity;
import com.codecool.snake.entities.Interactable;
import com.codecool.snake.entities.snakes.SnakeHead;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

import java.security.SecureRandom;

public class SamuraiRat extends GameEntity implements Animatable, Interactable {

    private static final int damage = 20;
    private final int RIGHT_MARGIN = 100, BOTTOM_MARGIN = 100;
    int speed;
    SecureRandom rnd;
    private Point2D heading;
    private double direction;
    private boolean dashHappened;
    private int dashCounter = 240;

    public SamuraiRat(Pane pane) {
        super(pane);
        this.speed = 1;
        this.rnd = new SecureRandom();
        setImage(Globals.samuraiRatEnemy);
        pane.getChildren().add(this);
        setX(rnd.nextDouble() * (Globals.WINDOW_WIDTH - RIGHT_MARGIN));
        setY(rnd.nextDouble() * (Globals.WINDOW_HEIGHT - BOTTOM_MARGIN));
        this.direction = rnd.nextDouble() * 360;
        setRotate(direction);
        this.heading = Utils.directionToVector(direction, speed);

    }

    @Override
    public void step() {
        int chance = rnd.nextInt(100);

        // Check for collision with margin
        if (isOutOfBounds(0, RIGHT_MARGIN, 0, BOTTOM_MARGIN)) {
            direction += 180;
            heading = Utils.directionToVector(direction, speed);
        }
        // Chance for dash to snake head
        else if (!dashHappened && chance == 50) {
            heading = Utils.directionToVector(heading.angle(SnakeHead.getXc(), SnakeHead.getYc()), 5);
            dashHappened = true;
            dashCounter = 240;
        }

        // If we didn't roll, we still need to know if we are in an existing dash animation.
        // Random chance to change direction.
        else if (!dashHappened && chance == 0) {
            direction = Utils.randomizeDirection(direction, 60);
            heading = Utils.directionToVector(direction, speed);
        }

        // Here we know we are in dash animation. Check for end of the animation.
        else if (dashCounter == 0) {
            dashHappened = false;
        }

        // Continue dash animation
        else {
            dashCounter--;
        }

        setX(getX() + heading.getX());
        setY(getY() + heading.getY());
    }

    @Override
    public void apply(SnakeHead player) {
        player.changeHealth(-damage);
        destroy();
    }

    @Override
    public String getMessage() {
        return damage + " damage from " + SamuraiRat.class.getSimpleName();
    }
}
