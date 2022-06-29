package Model;

import Client.Client;
import algorithms.mazeGenerators.*;
import algorithms.search.Solution;
import IO.MyDecompressorInputStream;
import Server.*;
import Client.*;
import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.Solution;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observer;
import java.util.Observable;
import algorithms.mazeGenerators.MyMazeGenerator;
public class MyModel extends Observable implements IModel {
    private Maze maze;
    private int[][] mazeGrid;
    private int playerRow;
    private int playerCol;
    private Solution solution;
    private MyMazeGenerator generator;
    private Position playerPosition;

    public MyModel() {
        generator = new MyMazeGenerator();
        mazeGrid = maze.getMaze();

    }

    @Override
    public void generateMaze(int rows, int cols) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {

                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);

                        toServer.flush();
                        int[] mazeDimensions = new int[]{rows, cols};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[rows * cols + 12]; //allocating byte[] for the decompressedmaze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        Maze maze = new Maze(decompressedMaze);
                        setMaze(new Maze(decompressedMaze));
                        maze.print();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        
    }
    public Position getPlayerPosition() {
        return this.playerPosition;
    }
    private void setMaze(Maze maze) {
        this.maze = maze;
        this.playerPosition = this.maze.getStartPosition();
    }

    @Override
    public Maze getMaze() {
        return maze;
    }

    @Override
    public void updatePlayerLocation(MovementDirection direction) {
        switch (direction) {
            case UP -> {
                if (playerRow > 0 && mazeGrid[playerRow - 1][playerCol] != 1)
                    movePlayer(playerRow - 1, playerCol);
            }
            case DOWN -> {
                if (playerRow < maze.getMaze().length - 1 && mazeGrid[playerRow + 1][playerCol] != 1)
                    movePlayer(playerRow + 1, playerCol);
            }
            case LEFT -> {
                if (playerCol > 0 && mazeGrid[playerRow][playerCol - 1] != 1)
                    movePlayer(playerRow, playerCol - 1);
            }
            case RIGHT -> {
                if (playerCol < maze.getMaze()[0].length - 1 && mazeGrid[playerRow][playerCol + 1] != 1)
                    movePlayer(playerRow, playerCol + 1);
            }
            case UPRIGHT -> {
                if (playerRow > 0 && playerCol < maze.getMaze()[0].length - 1 && mazeGrid[playerRow - 1][playerCol + 1] != 1)
                    movePlayer(playerRow - 1, playerCol + 1);
            }
            case UPLEFT -> {
                if (playerRow > 0 && playerCol > 0 && mazeGrid[playerRow - 1][playerCol - 1] != 1)
                    movePlayer(playerRow - 1, playerCol - 1);
            }
            case DOWNRIGHT -> {
                if (playerRow < maze.getMaze().length - 1 && playerCol < maze.getMaze()[0].length - 1 && mazeGrid[playerRow + 1][playerCol + 1] != 1)
                    movePlayer(playerRow + 1, playerCol + 1);
            }
            case DOWNLEFT -> {
                if (playerRow < maze.getMaze().length - 1 && playerCol > 0 && mazeGrid[playerRow + 1][playerCol - 1] != 1)
                    movePlayer(playerRow + 1, playerCol - 1);

            }
            default -> {
            }
        }

    }

    private void movePlayer(int row, int col) {
        this.playerRow = row;
        this.playerCol = col;
        setChanged();
        notifyObservers("player moved");
    }

    @Override
    public int getPlayerRow() {
        return playerRow;
    }

    @Override
    public int getPlayerCol() {
        return playerCol;
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void solveMaze() {
        //solve the maze
        //solution = new Solution();
        setChanged();
        notifyObservers("maze solved");
    }

    @Override
    public Solution getSolution() {
        return solution;
    }

    @Override
    public void SetMaze(Maze m) {
        maze=m;
        mazeGrid=m.getMaze();
        playerRow=maze.getStartPosition().getRowIndex();
        playerCol=maze.getStartPosition().getColumnIndex();
        solution=null;
    }

}
