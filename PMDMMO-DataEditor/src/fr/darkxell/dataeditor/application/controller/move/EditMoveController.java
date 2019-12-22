package fr.darkxell.dataeditor.application.controller.move;

import java.net.URL;
import java.util.ResourceBundle;

import com.darkxell.common.move.Move;
import com.darkxell.common.move.Move.MoveCategory;
import com.darkxell.common.move.Move.MoveRange;
import com.darkxell.common.move.Move.MoveTarget;
import com.darkxell.common.move.MoveBuilder;
import com.darkxell.common.pokemon.PokemonType;

import fr.darkxell.dataeditor.application.util.FXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class EditMoveController implements Initializable {

    @FXML
    public TextField accuracyTextfield;
    @FXML
    public ComboBox<MoveCategory> categoryCombobox;
    @FXML
    public TextField criticalTextfield;
    @FXML
    public CheckBox damageCheckbox;
    @FXML
    public TextField effectTextfield;
    @FXML
    public CheckBox freezeCheckbox;
    @FXML
    public CheckBox ginsengableCheckbox;
    @FXML
    public TextField idTextfield;
    @FXML
    public ImageView imageView;
    @FXML
    public TextField powerTextfield;
    @FXML
    public TextField ppTextfield;
    @FXML
    public ComboBox<MoveRange> rangeCombobox;
    @FXML
    public CheckBox reflectableCheckbox;
    @FXML
    public CheckBox snatchableCheckbox;
    @FXML
    public CheckBox soundCheckbox;
    @FXML
    public ComboBox<MoveTarget> targetCombobox;
    @FXML
    public ComboBox<PokemonType> typeCombobox;

    private Move generate() {
        int id, pp, power, accuracy, critical, effectID;

        if (this.idTextfield.getText().matches("-?\\d+"))
            id = Integer.parseInt(this.idTextfield.getText());
        else {
            FXUtils.showAlert("Wrong ID: " + this.idTextfield.getText());
            return null;
        }

        if (this.ppTextfield.getText().matches("\\d+"))
            pp = Integer.parseInt(this.ppTextfield.getText());
        else {
            FXUtils.showAlert("Wrong PP: " + this.ppTextfield.getText());
            return null;
        }

        if (this.powerTextfield.getText().matches("\\d+"))
            power = Integer.parseInt(this.powerTextfield.getText());
        else {
            FXUtils.showAlert("Wrong Power: " + this.powerTextfield.getText());
            return null;
        }

        if (this.accuracyTextfield.getText().matches("\\d+"))
            accuracy = Integer.parseInt(this.accuracyTextfield.getText());
        else {
            FXUtils.showAlert("Wrong Accuracy: " + this.accuracyTextfield.getText());
            return null;
        }

        if (this.criticalTextfield.getText().matches("\\d+"))
            critical = Integer.parseInt(this.criticalTextfield.getText());
        else {
            FXUtils.showAlert("Wrong Critical hit ratio: " + this.criticalTextfield.getText());
            return null;
        }

        if (this.effectTextfield.getText().matches("\\d+"))
            effectID = Integer.parseInt(this.effectTextfield.getText());
        else {
            FXUtils.showAlert("Wrong effect ID: " + this.effectTextfield.getText());
            return null;
        }

        MoveBuilder builder = new MoveBuilder().withID(id).withType(this.typeCombobox.getValue())
                .withCategory(this.categoryCombobox.getValue()).withPP(pp).withPower(power).withAccuracy(accuracy)
                .withRange(this.rangeCombobox.getValue()).withTargets(this.targetCombobox.getValue())
                .withCritical(critical).withEffectID(effectID);
        if (!this.reflectableCheckbox.isSelected())
            builder.withoutReflectable();
        if (!this.snatchableCheckbox.isSelected())
            builder.withoutSnatchable();
        if (this.soundCheckbox.isSelected())
            builder.withSound();
        if (this.freezeCheckbox.isSelected())
            builder.withFreezePiercing();
        if (!this.damageCheckbox.isSelected())
            builder.withoutDamage();
        if (!this.ginsengableCheckbox.isSelected())
            builder.withoutGinsengable();

        return builder.build();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.typeCombobox.getItems().addAll(PokemonType.values());
        this.categoryCombobox.getItems().addAll(MoveCategory.values());
        this.rangeCombobox.getItems().addAll(MoveRange.values());
        this.targetCombobox.getItems().addAll(MoveTarget.values());
    }

    public void onCancelChanges() {
        MovesTabController.instance.onEdit(MovesTabController.instance.currentMove);
    }

    public void onSaveMove() {
        Move move = this.generate();
        if (move != null)
            MovesTabController.instance.onEdited(move);
    }

    public void setupFor(Move move) {
        this.idTextfield.setText(String.valueOf(move.id));
        this.typeCombobox.getSelectionModel().select(move.getType());
        this.categoryCombobox.getSelectionModel().select(move.category);
        this.ppTextfield.setText(String.valueOf(move.pp));
        this.powerTextfield.setText(String.valueOf(move.power));
        this.accuracyTextfield.setText(String.valueOf(move.accuracy));
        this.rangeCombobox.getSelectionModel().select(move.range);
        this.targetCombobox.getSelectionModel().select(move.targets);
        this.criticalTextfield.setText(String.valueOf(move.critical));
        this.reflectableCheckbox.setSelected(move.reflectable);
        this.snatchableCheckbox.setSelected(move.snatchable);
        this.soundCheckbox.setSelected(move.sound);
        this.freezeCheckbox.setSelected(move.piercesFreeze);
        this.damageCheckbox.setSelected(move.dealsDamage);
        this.ginsengableCheckbox.setSelected(move.ginsengable);
        this.effectTextfield.setText(String.valueOf(move.effectID));
    }

}
