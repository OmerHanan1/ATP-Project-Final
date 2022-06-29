package Model;
//import org.apache.logging.log4j.Logger;
import algorithms.search.Solution;
import algorithms.mazeGenerators.Maze;
import java.util.Observer;
import java.util.Observable;
public interface IModel {
    void generateMaze(int rows, int cols);
    Maze getMaze();
    void updatePlayerLocation(MovementDirection direction);
    int getPlayerRow();
    int getPlayerCol();
    void solveMaze();
    Solution getSolution();
    //////
    void SetMaze(Maze m);

    void assignObserver(Observer o);

}
