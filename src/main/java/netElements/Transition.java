package netElements;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class Transition extends Rectangle {
    public Color exColor = Color.LIGHTGREEN;

    public boolean isParent = false;
    public boolean isChild = false;

    public Label label;
    public TextField textArea;

    public void setElements () {
        this.textArea = new TextField();
        this.textArea.setFont(Font.font(7));
        this.textArea.setVisible(false);
        textArea.setTranslateX(this.getX() + 20);
        textArea.setTranslateY(this.getY() + 20);
        textArea.setPrefSize(45, 7.5);

        this.label = new Label();
        label.setFont(Font.font(10));
        label.setVisible(true);
        label.setTranslateX(this.getX() + 20);
        label.setTranslateY(this.getY() + 20);
        label.setPrefSize(45, 7.5);
    }

    public List<Place> parentList = new ArrayList<>();
    public List<Arc> parentLine = new ArrayList<>();
    public List<Place> childrenList = new ArrayList<>();
    public List<Arc> childrenLine = new ArrayList<>();
}
