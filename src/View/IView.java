package View;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Observable;

public interface IView {
    public void generateMaze(ActionEvent actionEvent);
    public void keyPressed(KeyEvent keyEvent);
    public void mouseClicked(MouseEvent mouseEvent);


}
