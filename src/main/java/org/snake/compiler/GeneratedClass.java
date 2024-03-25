package org.snake.compiler;
import org.snake.model.SnakeModel;
public class GeneratedClass {
public static void left(SnakeModel snake, int size){
snake.addPosX(-size);
}
public static void right(SnakeModel snake, int size){
snake.addPosX(size);
}
}