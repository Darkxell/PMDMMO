package fr.darkxell.dataeditor.application.controller.dungeon;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;
import org.controlsfx.control.RangeSlider;

import com.darkxell.common.util.Pair;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class FloorsPart extends HBox {

    private Label label;
    public final EditFloorsetController parent;
    private Pair<Integer, Integer> part, current;
    private PopOver popover;
    private Button showSliderButton, removeButton;
    private RangeSlider slider;

    public FloorsPart(EditFloorsetController parent, Pair<Integer, Integer> part) {
        this.parent = parent;
        this.current = this.part = part;

        this.getChildren().add(this.label = new Label());
        this.getChildren().add(this.showSliderButton = new Button("Change"));
        this.getChildren().add(this.removeButton = new Button("Delete"));
        this.removeButton.setOnAction(event -> this.parent.onRemovePart(this.part));
        this.slider = new RangeSlider(1,
                Math.max(this.part.second, EditDungeonDataController.instance.currentFloorCount()) + 1, this.part.first,
                this.part.second);
        this.slider.setShowTickLabels(true);
        this.slider.setShowTickMarks(true);
        this.slider.setMajorTickUnit(5);
        this.slider.setSnapToTicks(true);
        this.slider.setPrefSize(this.slider.getMax() * 10, 20);

        HBox box = new HBox(this.slider);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER);

        this.popover = new PopOver(box);
        this.popover.setArrowLocation(ArrowLocation.BOTTOM_CENTER);
        this.popover.setOnHiding(event -> this.onValueChanged());
        this.showSliderButton.setOnAction(event -> this.popover.show(this.showSliderButton));

        this.slider.lowValueProperty().addListener((observable, oldValue, newValue) -> {
            this.current = new Pair<>(newValue.intValue(), this.current.second);
            this.updateLabel();
        });
        this.slider.highValueProperty().addListener((observable, oldValue, newValue) -> {
            this.current = new Pair<>(this.current.first, newValue.intValue());
            this.updateLabel();
        });
        this.updateLabel();

        this.setSpacing(10);
    }

    private void onValueChanged() {
        this.parent.onPartChanged(this.part, this.current);
    }

    private void updateLabel() {
        this.label.setText(this.current.first + " - " + this.current.second);
    }

}
