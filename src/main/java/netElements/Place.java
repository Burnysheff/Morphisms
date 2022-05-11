package netElements;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class Place extends Circle {
    public Color exColor = Color.LIGHTCORAL;

    public boolean isSequential = false;

    public boolean marking = false;
    public Label marks;

    public void setLabel() {
        if (marking) {
            this.marks.setText("*");
        } else {
            this.marks.setText("");
        }
    }

    public Label label;
    public TextField textArea;

    public void setElements () {
        this.marks = new Label();
        marks.setFont(Font.font(25));
        marks.setVisible(true);
        marks.setTranslateX(this.getCenterX());
        marks.setTranslateY(this.getCenterY() + 15);
        marks.setPrefSize(10, 10);

        this.textArea = new TextField();
        this.textArea.setFont(Font.font(7));
        this.textArea.setVisible(false);
        textArea.setTranslateX(this.getCenterX() + 20);
        textArea.setTranslateY(this.getCenterY() - 20);
        textArea.setPrefSize(45, 7.5);

        this.label = new Label();
        label.setFont(Font.font(7));
        label.setVisible(true);
        label.setTranslateX(this.getCenterX() + 10);
        label.setTranslateY(this.getCenterY() - 20);
        label.setPrefSize(45, 7.5);
    }

    public List<Transition> parentList = new ArrayList<>();
    public List<Arc> parentLine = new ArrayList<>();
    public List<Transition> childrenList = new ArrayList<>();
    public List<Arc> childrenLine = new ArrayList<>();
}
