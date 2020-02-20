package fr.darkxell.dataeditor.application.controller.animation;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.mechanics.animation.SpritesetAnimation.BackSpriteUsage;
import com.darkxell.client.model.animation.AnimationModel;
import com.darkxell.client.model.animation.AnimationVariantModel;
import com.darkxell.client.resources.image.pokemon.body.PokemonSpriteState;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.language.StringUtil;

import fr.darkxell.dataeditor.application.DataEditor;
import fr.darkxell.dataeditor.application.util.AnimationListItem;
import fr.darkxell.dataeditor.application.util.FXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class EditAnimationController implements Initializable {

    public static EditAnimationController instance;
    public static Stage variantPopup;

    @FXML
    public TextField alsoplayDelayTextfield;
    @FXML
    public TextField alsoplayTextfield;
    @FXML
    public ComboBox<String> animMovementCombobox;
    @FXML
    public ComboBox<BackSpriteUsage> backspritesCombobox;
    @FXML
    public TextField clonesTextfield;
    @FXML
    public TextField customAnimationIDTextfield;
    @FXML
    public TextField delayTextfield;
    @FXML
    public TextField loopTextfield;
    @FXML
    public RadioButton noSpritesRadio, defaultSpritesRadio, customSpritesRadio;
    @FXML
    public TextField orderTextfield;
    @FXML
    public TextField overlayTextfield;
    @FXML
    public CheckBox playforeachtargetCheckbox;
    @FXML
    public ComboBox<String> pokemonMovementCombobox;
    @FXML
    public TextField soundDelayTextfield;
    @FXML
    public TextField soundTextfield;
    @FXML
    public TextField spriteDurationTextfield;
    @FXML
    public TextField spritesTextfield;
    @FXML
    public CheckBox stateCheckbox;
    @FXML
    public ComboBox<PokemonSpriteState> stateCombobox;
    @FXML
    public TextField stateDelayTextfield;
    @FXML
    public TextField widthTextfield, heightTextfield;
    @FXML
    public TextField xTextfield, yTextfield;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;

        ToggleGroup spritesGroup = new ToggleGroup();
        this.noSpritesRadio.setToggleGroup(spritesGroup);
        this.defaultSpritesRadio.setToggleGroup(spritesGroup);
        this.customSpritesRadio.setToggleGroup(spritesGroup);

        spritesGroup.selectedToggleProperty().addListener((o, ov, nv) -> onSpritesChange());

        this.noSpritesRadio.setSelected(true);
        this.spritesTextfield.setDisable(true);

        this.backspritesCombobox.getItems().addAll(BackSpriteUsage.values());
        this.backspritesCombobox.getSelectionModel().select(0);

        this.stateCombobox.getItems().addAll(PokemonSpriteState.values());
        this.stateCombobox.getSelectionModel().select(0);

        this.pokemonMovementCombobox.getItems().addAll("none", "dash", "2tiles", "smalljump");
        this.pokemonMovementCombobox.getSelectionModel().select(0);

        this.animMovementCombobox.getItems().addAll("none", "1tilefacing", "diagonal", "upanddown", "straight", "arc");
        this.animMovementCombobox.getSelectionModel().select(0);

        this.stateCheckbox.selectedProperty().addListener((v, nv, ov) -> this.onStateChange());
        this.stateCombobox.setDisable(true);

        Pattern pattern = Pattern.compile("\\d*");
        Pattern pattern3 = Pattern.compile("-?\\d*");
        this.delayTextfield.setTextFormatter(new TextFormatter<>(change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        }));
        this.loopTextfield.setTextFormatter(new TextFormatter<>(change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        }));
        this.overlayTextfield.setTextFormatter(new TextFormatter<>(change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        }));
        this.soundDelayTextfield.setTextFormatter(new TextFormatter<>(change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        }));
        this.spriteDurationTextfield.setTextFormatter(new TextFormatter<>(change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        }));
        this.stateDelayTextfield.setTextFormatter(new TextFormatter<>(change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        }));
        this.widthTextfield.setTextFormatter(new TextFormatter<>(change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        }));
        this.heightTextfield.setTextFormatter(new TextFormatter<>(change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        }));
        this.xTextfield.setTextFormatter(new TextFormatter<>(change -> {
            return pattern3.matcher(change.getControlNewText()).matches() ? change : null;
        }));
        this.yTextfield.setTextFormatter(new TextFormatter<>(change -> {
            return pattern3.matcher(change.getControlNewText()).matches() ? change : null;
        }));

        Pattern p2 = Pattern.compile("(\\d+,)*(\\d+)?");
        this.alsoplayDelayTextfield.setTextFormatter(new TextFormatter<>(change -> {
            return p2.matcher(change.getControlNewText()).matches() ? change : null;
        }));
        Pattern p3 = Pattern.compile("(-?\\d+,)*(-?\\d+)?");
        this.orderTextfield.setTextFormatter(new TextFormatter<>(change -> {
            return p3.matcher(change.getControlNewText()).matches() ? change : null;
        }));
    }

    public void onCancelChanges() {
        this.setupFor(AnimationsTabController.instance.editing);
    }

    public void onChangeID() {
        AnimationsTabController.instance.onChangeID();
    }

    public void onEditVariant() {
        FXMLLoader loader = new FXMLLoader(DataEditor.class.getResource("/layouts/animations/select_variant.fxml"));
        try {
            Parent root = loader.load();
            variantPopup = FXUtils.showPopup(root, "Choose variant to edit");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEditVariant(Direction direction) {
        try {
            FXMLLoader loader = new FXMLLoader(DataEditor.class.getResource("/layouts/animations/edit_variant.fxml"));
            Parent root = loader.load();
            if (AnimationsTabController.instance.editing != null) {
                AnimationModel data = Animations.getData(AnimationsTabController.instance.editing.id,
                        AnimationsTabController.instance.editing.group);
                EditVariantController controller = loader.getController();
                controller.data = data;
                controller.variant = direction;
                controller.setup(data, data.getVariant(direction));
                variantPopup = FXUtils.showPopup(root, "Edit Variant: " + direction);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSave() {
        AnimationModel anim = Animations.getData(AnimationsTabController.instance.editing.id,
                AnimationsTabController.instance.editing.group);
        AnimationModel previous = new AnimationModel(anim);
        anim = this.update(anim, anim.getDefaultModel());
        if (anim != null) {
            this.updateVariants(anim, previous);
            AnimationsTabController.instance.onEdited(anim);
        }
    }

    public void onSpritesChange() {
        this.spritesTextfield.setDisable(!this.customSpritesRadio.isSelected());
    }

    public void onStateChange() {
        this.stateCombobox.setDisable(!this.stateCheckbox.isSelected());
    }

    protected void setupFor(AnimationModel parent, AnimationVariantModel model) {
        String apdelay = "";
        for (int i = 0; i < model.getAlsoPlayDelay().length; ++i) {
            if (i != 0)
                apdelay += ",";
            apdelay += model.getAlsoPlayDelay()[i];
        }
        this.alsoplayDelayTextfield.setText(apdelay);
        String ap = "";
        for (int i = 0; i < model.getAlsoPlay().length; ++i) {
            if (i != 0)
                ap += ",";
            ap += model.getAlsoPlay()[i];
        }
        this.alsoplayTextfield.setText(ap);
        this.animMovementCombobox.getSelectionModel().select(0);
        if (model.getAnimationMovement() != null)
            this.animMovementCombobox.setValue(model.getAnimationMovement());
        else
            this.animMovementCombobox.setValue("none");
        this.backspritesCombobox.setValue(model.getBackSpriteUsage());
        if (parent.getClones() != null)
            this.clonesTextfield.setText(parent.getClones());
        else
            this.clonesTextfield.setText("");
        this.delayTextfield.setText(String.valueOf(model.getDelayTime()));
        this.loopTextfield.setText(String.valueOf(model.getLoopsFrom()));
        if (model.getSpriteOrder() == null)
            this.orderTextfield.setText("");
        else {
            String order = "";
            for (int i = 0; i < model.getSpriteOrder().length; ++i) {
                if (i != 0)
                    order += ",";
                order += model.getSpriteOrder()[i];
            }
            this.orderTextfield.setText(order);
        }
        this.playforeachtargetCheckbox.setSelected(model.isPlaysForEachTarget());
        this.pokemonMovementCombobox.getSelectionModel().select(0);
        if (model.getPokemonMovement() != null)
            this.pokemonMovementCombobox.setValue(model.getPokemonMovement());
        else
            this.pokemonMovementCombobox.setValue("none");
        this.soundDelayTextfield.setText(String.valueOf(model.getSoundDelay()));
        if (model.getSound() != null)
            this.soundTextfield.setText(model.getSound());
        else
            this.soundTextfield.setText("");
        this.spriteDurationTextfield.setText(String.valueOf(model.getSpriteDuration()));
        this.stateCheckbox.setSelected(model.getPokemonState() != null);
        if (model.getPokemonState() != null)
            this.stateCombobox.setValue(model.getPokemonState());
        this.stateDelayTextfield.setText(String.valueOf(model.getPokemonStateDelay()));
        this.widthTextfield.setText(String.valueOf(model.getWidth()));
        this.heightTextfield.setText(String.valueOf(model.getHeight()));
        this.xTextfield.setText(String.valueOf(model.getGravityX()));
        this.yTextfield.setText(String.valueOf(model.getGravityY()));
        this.overlayTextfield.setText(model.getOverlay().equals(-1) ? "" : String.valueOf(model.getOverlay()));
        this.customAnimationIDTextfield.setText(model.getCustomAnimation() == null ? "" : model.getCustomAnimation());

        if (model.getSprites() == null) {
            this.noSpritesRadio.setSelected(true);
            this.spritesTextfield.setText("");
        } else if (model.getSprites().equals("" + parent.getID())) {
            this.defaultSpritesRadio.setSelected(true);
            this.spritesTextfield.setText("");
        } else {
            this.customSpritesRadio.setSelected(true);
            this.spritesTextfield.setText(model.getSprites());
        }
    }

    public void setupFor(AnimationListItem item) {
        AnimationModel data = Animations.getData(item.id, item.group);
        this.setupFor(data, data.getDefaultModel());
    }

    protected AnimationModel update(AnimationModel parent, AnimationVariantModel model) {
        String in = this.alsoplayTextfield.getText();
        if (!in.equals("")) {
            String[] ids = in.split(",");
            model.setAlsoPlay(new String[ids.length]);
            System.arraycopy(ids, 0, model.getAlsoPlay(), 0, ids.length);
        } else
            model.setAlsoPlay(new String[0]);

        in = this.alsoplayDelayTextfield.getText();
        if (in.equals("")) {
            model.setAlsoPlayDelay(new Integer[model.getAlsoPlay().length]);
            for (int i = 0; i < model.getAlsoPlayDelay().length; ++i)
                model.getAlsoPlayDelay()[i] = 0;
        } else {
            String[] delays = in.split(",");
            model.setAlsoPlayDelay(new Integer[delays.length]);
            for (int i = 0; i < delays.length; ++i)
                try {
                    model.getAlsoPlayDelay()[i] = Integer.parseInt(delays[i]);
                } catch (NumberFormatException e) {
                    FXUtils.showAlert("Wrong delay: " + delays[i]);
                    return null;
                }
        }

        if (!this.animMovementCombobox.getValue().equals("none"))
            model.setAnimationMovement(this.animMovementCombobox.getValue());
        else
            model.setAnimationMovement(null);
        model.setBackSpriteUsage(this.backspritesCombobox.getValue());
        if (!this.clonesTextfield.getText().equals(""))
            parent.setClones(this.clonesTextfield.getText());
        else
            parent.setClones(null);
        if (!this.delayTextfield.getText().equals(""))
            model.setDelayTime(Integer.parseInt(this.delayTextfield.getText()));
        else
            model.setDelayTime(0);
        if (!this.loopTextfield.getText().equals(""))
            model.setLoopsFrom(Integer.parseInt(this.loopTextfield.getText()));
        else
            model.setLoopsFrom(0);

        in = this.orderTextfield.getText();
        if (!in.equals("")) {
            String[] indexes = in.split(",");
            model.setSpriteOrder(new Integer[indexes.length]);
            for (int i = 0; i < indexes.length; ++i)
                try {
                    model.getSpriteOrder()[i] = Integer.parseInt(indexes[i]);
                } catch (NumberFormatException e) {
                    FXUtils.showAlert("Wrong sprite index: " + indexes[i]);
                    return null;
                }
        } else
            model.setSpriteOrder(new Integer[0]);

        if (!this.overlayTextfield.getText().equals(""))
            model.setOverlay(Integer.parseInt(this.overlayTextfield.getText()));
        else
            model.setOverlay(-1);
        model.setPlaysForEachTarget(this.playforeachtargetCheckbox.isSelected());
        if (!this.pokemonMovementCombobox.getValue().equals("none"))
            model.setPokemonMovement(this.pokemonMovementCombobox.getValue());
        else
            model.setPokemonMovement(null);
        if (!this.soundDelayTextfield.getText().equals(""))
            model.setSoundDelay(Integer.parseInt(this.soundDelayTextfield.getText()));
        else
            model.setSoundDelay(0);
        if (!this.soundTextfield.getText().equals(""))
            model.setSound(this.soundTextfield.getText());
        else
            model.setSound(null);
        if (!this.spriteDurationTextfield.getText().equals(""))
            model.setSpriteDuration(Integer.parseInt(this.spriteDurationTextfield.getText()));
        else
            model.setSpriteDuration(2);
        if (this.stateCheckbox.isSelected())
            model.setPokemonState(this.stateCombobox.getValue());
        else
            model.setPokemonState(null);
        if (!this.stateDelayTextfield.getText().equals(""))
            model.setPokemonStateDelay(Integer.parseInt(this.stateDelayTextfield.getText()));
        else
            model.setPokemonStateDelay(0);
        if (!this.widthTextfield.getText().equals(""))
            model.setWidth(Integer.parseInt(this.widthTextfield.getText()));
        else
            model.setWidth(0);
        if (!this.heightTextfield.getText().equals(""))
            model.setHeight(Integer.parseInt(this.heightTextfield.getText()));
        else
            model.setHeight(0);
        if (!this.xTextfield.getText().equals(""))
            model.setGravityX(Integer.parseInt(this.xTextfield.getText()));
        else
            model.setGravityX(-1);
        if (!this.yTextfield.getText().equals(""))
            model.setGravityY(Integer.parseInt(this.yTextfield.getText()));
        else
            model.setGravityY(-1);
        if (!this.customAnimationIDTextfield.getText().equals(""))
            model.setCustomAnimation(this.customAnimationIDTextfield.getText());
        else
            model.setCustomAnimation(null);

        if (this.noSpritesRadio.isSelected())
            model.setSprites(null);
        else if (this.defaultSpritesRadio.isSelected())
            model.setSprites("" + parent.getID());
        else
            model.setSprites(this.spritesTextfield.getText());

        return parent;
    }

    private void updateVariant(AnimationVariantModel defaultModel, AnimationVariantModel previous,
            AnimationVariantModel toupdate) {
        if (Arrays.equals(toupdate.getAlsoPlay(), previous.getAlsoPlay()))
            toupdate.setAlsoPlay(defaultModel.getAlsoPlay());
        if (Arrays.equals(toupdate.getAlsoPlayDelay(), previous.getAlsoPlayDelay()))
            toupdate.setAlsoPlayDelay(defaultModel.getAlsoPlayDelay());
        if (StringUtil.equals(toupdate.getAnimationMovement(), previous.getAnimationMovement()))
            toupdate.setAnimationMovement(defaultModel.getAnimationMovement());
        if (toupdate.getBackSpriteUsage() == previous.getBackSpriteUsage())
            toupdate.setBackSpriteUsage(defaultModel.getBackSpriteUsage());
        if (toupdate.getDelayTime().equals(previous.getDelayTime()))
            toupdate.setDelayTime(defaultModel.getDelayTime());
        if (toupdate.getLoopsFrom().equals(previous.getLoopsFrom()))
            toupdate.setLoopsFrom(defaultModel.getLoopsFrom());
        if (Arrays.equals(toupdate.getSpriteOrder(), previous.getSpriteOrder()))
            toupdate.setSpriteOrder(defaultModel.getSpriteOrder());
        if (toupdate.getOverlay().equals(previous.getOverlay()))
            toupdate.setOverlay(defaultModel.getOverlay());
        if (toupdate.isPlaysForEachTarget().equals(previous.isPlaysForEachTarget()))
            toupdate.setPlaysForEachTarget(defaultModel.isPlaysForEachTarget());
        if (StringUtil.equals(toupdate.getPokemonMovement(), previous.getPokemonMovement()))
            toupdate.setPokemonMovement(defaultModel.getPokemonMovement());
        if (StringUtil.equals(toupdate.getSound(), previous.getSound()))
            toupdate.setSound(defaultModel.getSound());
        if (toupdate.getSoundDelay().equals(previous.getSoundDelay()))
            toupdate.setSoundDelay(defaultModel.getSoundDelay());
        if (toupdate.getSpriteDuration().equals(previous.getSpriteDuration()))
            toupdate.setSpriteDuration(defaultModel.getSpriteDuration());
        if (StringUtil.equals(toupdate.getSprites(), previous.getSprites()))
            toupdate.setSprites(defaultModel.getSprites());
        if (toupdate.getPokemonState() == previous.getPokemonState())
            toupdate.setPokemonState(defaultModel.getPokemonState());
        if (toupdate.getPokemonStateDelay().equals(previous.getPokemonStateDelay()))
            toupdate.setPokemonStateDelay(defaultModel.getPokemonStateDelay());
        if (toupdate.getWidth().equals(previous.getWidth()))
            toupdate.setWidth(defaultModel.getWidth());
        if (toupdate.getHeight().equals(previous.getHeight()))
            toupdate.setHeight(defaultModel.getHeight());
        if (toupdate.getGravityX().equals(previous.getGravityX()))
            toupdate.setGravityX(defaultModel.getGravityX());
        if (toupdate.getGravityY().equals(previous.getGravityY()))
            toupdate.setGravityY(defaultModel.getGravityY());
        if (toupdate.getCustomAnimation().equals(previous.getCustomAnimation()))
            toupdate.setCustomAnimation(defaultModel.getCustomAnimation());
    }

    private void updateVariants(AnimationModel anim, AnimationModel previous) {
        for (Direction d : Direction.values())
            this.updateVariant(anim.getDefaultModel(), previous.getDefaultModel(), anim.getVariant(d));
    }

}
