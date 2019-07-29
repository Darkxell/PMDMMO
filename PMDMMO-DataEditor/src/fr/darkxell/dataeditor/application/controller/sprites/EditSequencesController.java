package fr.darkxell.dataeditor.application.controller.sprites;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.darkxell.client.resources.image.pokemon.body.PSDFrame;
import com.darkxell.client.resources.image.pokemon.body.PSDSequence;
import com.darkxell.client.resources.image.pokemon.body.PokemonSpritesetData;
import com.darkxell.client.resources.image.spritefactory.PMDRegularSpriteset;

import fr.darkxell.dataeditor.application.DataEditor;
import fr.darkxell.dataeditor.application.controls.CustomList;
import fr.darkxell.dataeditor.application.controls.CustomListCell.ListCellParent;
import fr.darkxell.dataeditor.application.util.FXUtils;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class EditSequencesController implements Initializable, ListCellParent<PSDFrame> {

    public static EditSequencesController instance;
    public static Stage popup;

    private int editing;
    @FXML
    public ListView<PSDFrame> framesList;
    @FXML
    public TextField hitTextfield;
    public SpritesTabController parent;
    @FXML
    public TextField returnTextfield;
    @FXML
    public TextField rushTextfield;
    @FXML
    public ComboBox<Integer> sequenceCombobox;
    @FXML
    public GridPane sequenceProperties;

    private HashMap<Integer, PSDSequence> sequences = new HashMap<>();

    public HashMap<Integer, PSDSequence> generateSequences(HashSet<Integer> existing) {
        HashMap<Integer, PSDSequence> sequences = new HashMap<>(this.sequences);
        sequences.entrySet().removeIf(entry -> !existing.contains(entry.getKey()));
        return sequences;
    }

    private PSDSequence getSequence(int id) {
        return this.sequences.get(id);
    }

    @Override
    public Node graphicFor(PSDFrame item) {
        if (this.parent == null || item == null)
            return null;
        PMDRegularSpriteset spriteset = this.parent.generalDataController.spriteset;
        if (spriteset == null)
            return null;
        if (spriteset.getSprite(item.frameID) == null)
            return null;
        return new ImageView(SwingFXUtils.toFXImage(spriteset.getSprite(item.frameID), null));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;

        this.sequenceProperties.setDisable(true);

        Pattern p = Pattern.compile("\\d*");
        this.rushTextfield.setTextFormatter(new TextFormatter<>(change -> {
            return p.matcher(change.getControlNewText()).matches() ? change : null;
        }));
        this.hitTextfield.setTextFormatter(new TextFormatter<>(change -> {
            return p.matcher(change.getControlNewText()).matches() ? change : null;
        }));
        this.returnTextfield.setTextFormatter(new TextFormatter<>(change -> {
            return p.matcher(change.getControlNewText()).matches() ? change : null;
        }));

        this.sequenceCombobox.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> this.onSequenceChanged(oldValue, newValue));

        CustomList.setup(this, this.framesList, "Frame", true, false, true, true, true);
    }

    @Override
    public void onCreate(PSDFrame nullItem) {
        PSDFrame frame = new PSDFrame();
        this.framesList.getItems().add(frame);
        this.onEdit(frame);
    }

    @Override
    public void onDelete(PSDFrame item) {
        this.framesList.getItems().remove(item);
    }

    public void onDimensionsChanged() {
        this.framesList.refresh();
    }

    @Override
    public void onEdit(PSDFrame item) {
        this.editing = this.framesList.getItems().indexOf(item);
        try {
            FXMLLoader loader = new FXMLLoader(DataEditor.class.getResource("/layouts/sprites/edit_frame.fxml"));
            Parent root = loader.load();
            EditFrameController controller = loader.getController();
            controller.setSpriteset(this.parent.generalDataController.spriteset);
            controller.setup(item);
            popup = FXUtils.showPopup(root, "Edit Frame");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onExistingSequencesChanged(HashSet<Integer> existing) {
        for (Integer id : existing)
            if (id != -1 && !this.sequences.containsKey(id)) {
                this.sequences.put(id, new PSDSequence(id));
                this.sequenceCombobox.getItems().add(id);
                this.sequenceCombobox.getItems().sort(Comparator.naturalOrder());
            }
    }

    public void onFrameEdited(PSDFrame frame) {
        if (this.editing != -1)
            this.framesList.getItems().set(this.editing, frame);
    }

    @Override
    public void onMove(PSDFrame item, int newIndex) {
    }

    @Override
    public void onRename(PSDFrame item, String name) {
    }

    private void onSequenceChanged(Integer oldValue, Integer newValue) {
        if (oldValue != null)
            this.saveSequence(oldValue);
        this.framesList.getItems().clear();
        if (newValue != null) {
            PSDSequence s = this.getSequence(newValue);
            this.rushTextfield.setText(String.valueOf(s.rushPoint));
            this.hitTextfield.setText(String.valueOf(s.hitPoint));
            this.returnTextfield.setText(String.valueOf(s.returnPoint));
            this.framesList.getItems().addAll(s.frames());
        }
    }

    @Override
    public double prefWidth(PSDFrame item) {
        if (this.parent == null || item == null)
            return 30;
        PMDRegularSpriteset spriteset = this.parent.generalDataController.spriteset;
        if (spriteset == null)
            return 30;
        if (spriteset.getSprite(item.frameID) == null)
            return 30;
        return Math.max(30, spriteset.getSprite(item.frameID).getHeight());
    }

    public void saveSequence(Integer oldValue) {
        int rush, hit, ret;

        if (this.rushTextfield.getText().equals(""))
            rush = 0;
        else
            rush = Integer.parseInt(this.rushTextfield.getText());
        if (this.hitTextfield.getText().equals(""))
            hit = 0;
        else
            hit = Integer.parseInt(this.hitTextfield.getText());
        if (this.returnTextfield.getText().equals(""))
            ret = 0;
        else
            ret = Integer.parseInt(this.returnTextfield.getText());

        PSDSequence s = new PSDSequence(oldValue, rush, hit, ret, this.framesList.getItems());
        this.sequences.put(oldValue, s);
    }

    public void setupFor(PokemonSpritesetData item) {
        this.sequenceProperties.setDisable(item == null);

        this.sequenceCombobox.getItems().clear();
        for (PSDSequence s : item.sequences()) {
            this.sequences.put(s.id, s);
            this.sequenceCombobox.getItems().addAll(s.id);
        }
    }

}
