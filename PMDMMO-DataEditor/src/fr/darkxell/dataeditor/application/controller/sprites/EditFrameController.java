package fr.darkxell.dataeditor.application.controller.sprites;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.darkxell.client.resources.image.pokemon.body.PSDFrame;
import com.darkxell.client.resources.images.RegularSpriteSet;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;

public class EditFrameController implements Initializable {

    @FXML
    private TextField durationTextfield;
    @FXML
    private CheckBox flippedCheckbox;
    @FXML
    private ImageView frameImage;
    @FXML
    private Spinner<Integer> frameSpinner;
    public RegularSpriteSet spriteset;
    @FXML
    private TextField sxTextfield;
    @FXML
    private TextField syTextfield;
    @FXML
    private TextField xTextfield;
    @FXML
    private TextField yTextfield;

    private PSDFrame generateFrame() {
        int duration, x, y, sx, sy;

        if (this.durationTextfield.getText().matches("-?"))
            duration = 2;
        else
            duration = Integer.parseInt(this.durationTextfield.getText());

        if (this.xTextfield.getText().matches("-?"))
            x = 0;
        else
            x = Integer.parseInt(this.xTextfield.getText());

        if (this.yTextfield.getText().matches("-?"))
            y = 0;
        else
            y = Integer.parseInt(this.yTextfield.getText());

        if (this.sxTextfield.getText().matches("-?"))
            sx = 0;
        else
            sx = Integer.parseInt(this.sxTextfield.getText());

        if (this.syTextfield.getText().matches("-?"))
            sy = 0;
        else
            sy = Integer.parseInt(this.syTextfield.getText());

        return new PSDFrame(null, this.frameSpinner.getValue(), duration, x, y, sx, sy,
                this.flippedCheckbox.isSelected());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Pattern p = Pattern.compile("-?\\d*");
        this.durationTextfield.setTextFormatter(new TextFormatter<>(change -> {
            return p.matcher(change.getControlNewText()).matches() ? change : null;
        }));
        this.xTextfield.setTextFormatter(new TextFormatter<>(change -> {
            return p.matcher(change.getControlNewText()).matches() ? change : null;
        }));
        this.yTextfield.setTextFormatter(new TextFormatter<>(change -> {
            return p.matcher(change.getControlNewText()).matches() ? change : null;
        }));
        this.sxTextfield.setTextFormatter(new TextFormatter<>(change -> {
            return p.matcher(change.getControlNewText()).matches() ? change : null;
        }));
        this.syTextfield.setTextFormatter(new TextFormatter<>(change -> {
            return p.matcher(change.getControlNewText()).matches() ? change : null;
        }));

        this.frameSpinner.setEditable(true);
    }

    public void onCancel() {
        EditSequencesController.popup.close();
    }

    public void onSave() {
        PSDFrame f = this.generateFrame();
        EditSequencesController.instance.onFrameEdited(f);
        this.onCancel();
    }

    public void setSpriteset(RegularSpriteSet spriteset) {
        this.spriteset = spriteset;
        this.frameSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
                this.spriteset.rows() * this.spriteset.columns() - 1, 0) {
            @Override
            public void decrement(int steps) {
                super.decrement(steps);
                updateImage();
            }

            @Override
            public void increment(int steps) {
                super.increment(steps);
                updateImage();
            }
        });
        this.frameSpinner.getValueFactory().setWrapAround(true);
    }

    public void setup(PSDFrame item) {
        this.frameSpinner.getValueFactory().setValue(item.frameID);
        this.durationTextfield.setText(String.valueOf(item.duration));
        this.xTextfield.setText(String.valueOf(item.spriteX));
        this.yTextfield.setText(String.valueOf(item.spriteY));
        this.sxTextfield.setText(String.valueOf(item.shadowX));
        this.syTextfield.setText(String.valueOf(item.shadowY));
        this.flippedCheckbox.setSelected(item.isFlipped);
        this.updateImage();
    }

    private void updateImage() {
        this.frameImage
                .setImage(SwingFXUtils.toFXImage(this.spriteset.get(this.frameSpinner.getValue()).image(), null));
    }

}
