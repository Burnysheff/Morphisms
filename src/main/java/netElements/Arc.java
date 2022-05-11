package netElements;

import javafx.animation.KeyValue;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Arc extends Line {

    public Line firstArrow = new Line();
    public Line secondArrow = new Line();

    public void setArrows() {
        firstArrow.getTransforms().clear();
        secondArrow.getTransforms().clear();
        firstArrow.setStartX(this.getStartX());
        firstArrow.setStartY(this.getStartY());
        firstArrow.setEndX(this.getEndX());
        firstArrow.setEndY(this.getEndY());
        firstArrow.setRotate(0);
        secondArrow.setStartX(this.getStartX());
        secondArrow.setStartY(this.getStartY());
        secondArrow.setEndX(this.getEndX());
        secondArrow.setEndY(this.getEndY());
        secondArrow.setRotate(0);
        double middleX = this.getStartX() + ((this.getEndX() - this.getStartX()) / 2);
        double middleY = this.getStartY() + ((this.getEndY() - this.getStartY()) / 2);
        double diffFirstX = firstArrow.getStartX() - middleX;
        double diffFirstY = firstArrow.getStartY() - middleY;
        double diffSecondX = secondArrow.getStartX() - middleX;
        double diffSecondY = firstArrow.getStartY() - middleY;
        firstArrow.setStartX(middleX);
        secondArrow.setStartX(middleX);
        firstArrow.setStartY(middleY);
        secondArrow.setStartY(middleY);
        firstArrow.setEndX(firstArrow.getEndX() - diffFirstX);
        secondArrow.setEndX(secondArrow.getEndX() - diffSecondX);
        firstArrow.setEndY(firstArrow.getEndY() - diffFirstY);
        secondArrow.setEndY(secondArrow.getEndY() - diffSecondY);
        double realFirstX = firstArrow.getStartX() + (firstArrow.getEndX() - firstArrow.getStartX()) / 10;
        double realFirstY = firstArrow.getStartY() + (firstArrow.getEndY() - firstArrow.getStartY()) / 10;
        double realSecondX = secondArrow.getStartX() + (secondArrow.getEndX() - secondArrow.getStartX()) / 10;
        double realSecondY = secondArrow.getStartY() + (secondArrow.getEndY() - secondArrow.getStartY()) / 10;
        firstArrow.setEndX(realFirstX);
        secondArrow.setEndX(realSecondX);
        firstArrow.setEndY(realFirstY);
        secondArrow.setEndY(realSecondY);
        Rotate rotationFirst = new Rotate();
        rotationFirst.pivotXProperty().bind(firstArrow.startXProperty());
        rotationFirst.pivotYProperty().bind(firstArrow.startYProperty());
        firstArrow.getTransforms().add(rotationFirst);
        rotationFirst.setAngle(235);
        Rotate rotationSecond = new Rotate();
        rotationSecond.pivotXProperty().bind(secondArrow.startXProperty());
        rotationSecond.pivotYProperty().bind(secondArrow.startYProperty());
        secondArrow.getTransforms().add(rotationSecond);
        rotationSecond.setAngle(135);
    }
}
