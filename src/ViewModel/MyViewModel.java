package ViewModel;

import Model.IModel;
import Model.MovementDirection;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.InvalidationListener;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
//import javafx.beans.Observable;
import java.util.Observable;
import java.util.Observer;
import Model.MovementDirection;

public class MyViewModel extends Observable implements Observer {
    private IModel model;

    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this); //Observe the Model for it's changes
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o ==model){
            setChanged();
            notifyObservers(arg);
        }
    }

    public Maze getMaze(){
        return model.getMaze();
    }

    public int getPlayerRow(){
        return model.getPlayerRow();
    }

    public int getPlayerCol(){
        return model.getPlayerCol();
    }

    public Solution getSolution(){
        return model.getSolution();
    }

    public void generateMaze(int rows, int cols){
        model.generateMaze(rows, cols);
    }

    public void movePlayer(KeyEvent keyEvent){
        MovementDirection direction;
        switch (keyEvent.getCode()){
            case NUMPAD8,UP -> direction = MovementDirection.UP; //NUMPAD8
            case NUMPAD2,DOWN -> direction = MovementDirection.DOWN;
            case NUMPAD4,LEFT -> direction = MovementDirection.LEFT;
            case NUMPAD6,RIGHT -> direction = MovementDirection.RIGHT;
            case NUMPAD9 -> direction = MovementDirection.UPRIGHT;
            case NUMPAD7 -> direction = MovementDirection.UPLEFT;
            case NUMPAD3 -> direction = MovementDirection.DOWNRIGHT;
            case NUMPAD1 -> direction = MovementDirection.DOWNLEFT;
            default -> {
                // no need to move the player...
                return;
            }
        }
        model.updatePlayerLocation(direction);
    }
    public void setMaze(Maze maze){model.SetMaze(maze);}

    public void solveMaze(){
        model.solveMaze();
    }

    public void PlayerMovement(double a,double b) {
        int r= model.getPlayerRow();
        int c=model.getPlayerCol();
        //left
        if (r > a && b >= r && c>a && c<a+1){
            MovementDirection direction = MovementDirection.LEFT;
            model.updatePlayerLocation(direction);
        }
        //right
        if (r > b && b+1 >= r && c+1<a && c>a-1) {
            MovementDirection direction = MovementDirection.RIGHT;
            model.updatePlayerLocation(direction);
        }

        //down
        if (r+1 < b && b - 1 < r && c<=a && c+1>=a) {
            MovementDirection direction = MovementDirection.DOWN;
            model.updatePlayerLocation(direction);

        }
        //up
        if (r > b && b + 1 >= r && c<=a && c+1>=a){
            MovementDirection direction = MovementDirection.UP;
            model.updatePlayerLocation(direction);
        }
    }
}
