package fr.darkxell.dataeditor.application.controller.move;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveBuilder;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.registry.Registries;

import fr.darkxell.dataeditor.application.controls.CustomListCell.ListCellParent;
import fr.darkxell.dataeditor.application.data.MoveListItem;
import fr.darkxell.dataeditor.application.util.CustomTreeItem;
import fr.darkxell.dataeditor.application.util.TreeCategory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class MovesTabController implements Initializable, ListCellParent<MoveListItem> {

    public static MovesTabController instance;

    private TreeItem<CustomTreeItem>[] categories;
    /** Currently edited Move. */
    public MoveListItem currentMove;
    @FXML
    public EditMoveController editMoveController;
    @FXML
    private TitledPane editMovePane;
    @FXML
    private TreeView<CustomTreeItem> movesTreeView;

    Move defaultMove(int id) {
        return new MoveBuilder().withID(id).build();
    }

    @Override
    public Node graphicFor(MoveListItem move) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        TreeItem<CustomTreeItem> root = new TreeItem<>(new TreeCategory("Moves"));
        root.setExpanded(true);
        this.movesTreeView.setRoot(root);
        List<TreeItem<CustomTreeItem>> categories = this.movesTreeView.getRoot().getChildren();
        for (PokemonType type : PokemonType.values())
            categories.add(new TreeItem<>(
                    new TreeCategory(type.getName().toString().replaceAll("<.*?>", "") + "-type Moves")));
        categories.add(new TreeItem<>(new TreeCategory("Orb Moves")));
        categories.add(new TreeItem<>(new TreeCategory("Hidden Moves")));
        this.categories = categories.toArray(new TreeItem[0]);

        /*
         * this.movesList.setCellFactory(param -> { return new CustomListCell<>(AnimationsTabController.instance,
         * "Animation").setCanOrder(false).setCanCreate(false).setCanDelete(false) .setCanRename(false); });
         */
        this.movesTreeView.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2) {
                TreeItem<CustomTreeItem> move = movesTreeView.getSelectionModel().getSelectedItem();
                if (move == null)
                    return;
                if (move.getValue() instanceof TreeCategory)
                    move.setExpanded(!move.isExpanded());
                else
                    onEdit((MoveListItem) move.getValue());
            }
        });

        this.reloadList();/*
                           * CustomList.setup(this, this.movesList, "Move", true, false, true, true, false);
                           * this.movesList.getMoves().addAll(MoveRegistry.list());
                           * this.movesList.getMoves().sort(Comparator.naturalOrder());
                           */

        this.editMovePane.setVisible(false);
    }

    @Override
    public void onCreate(MoveListItem nullMove) {
        this.onCreateMove();
    }

    public void onCreateMove() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("New Move");
        dialog.setHeaderText(null);
        dialog.setContentText("Type in the ID of the new Move.");
        Optional<String> name = dialog.showAndWait();
        if (name.isPresent())
            if (name.get().matches("-?\\d+")) {
                MoveRegistry moves = Registries.moves();
                Move i = this.defaultMove(Integer.parseInt(name.get()));
                if (moves.find(i.id) != null)
                    new Alert(AlertType.ERROR, "There is already an Move with ID " + i.id, ButtonType.OK).showAndWait();
                else {
                    moves.register(i);
                    this.reloadList();
                }
            } else
                new Alert(AlertType.ERROR, "Wrong ID: " + name.get(), ButtonType.OK).showAndWait();
    }

    @Override
    public void onDelete(MoveListItem move) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText("Delete Animation");
        alert.setContentText("Are you sure you want to delete move '" + move.move.name() + "'?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (move == this.currentMove) {
                this.currentMove = null;
                this.editMovePane.setVisible(false);
            }
            Registries.moves().unregister(move.move.id);
            this.reloadList();
        }
    }

    public void onDeleteMove() {
        TreeItem<CustomTreeItem> selected = this.movesTreeView.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;
        if (selected.getValue() instanceof MoveListItem)
            this.onDelete((MoveListItem) selected.getValue());
    }

    @Override
    public void onEdit(MoveListItem move) {
        this.currentMove = move;
        this.editMovePane.setVisible(true);
        this.editMovePane.setText(this.currentMove.move.name().toString());
        this.editMoveController.setupFor(this.currentMove.move);
    }

    public void onEdited(Move move) {
        boolean idChanged = this.currentMove.move.id != move.id;
        MoveRegistry moves = Registries.moves();
        if (idChanged && moves.find(move.id) != null)
            new Alert(AlertType.ERROR, "Cannot save: There is already another Move with ID " + move.id, ButtonType.OK)
                    .showAndWait();
        else {
            moves.unregister(this.currentMove.move.id);
            moves.register(move);
            this.reloadList();
            // this.onEdit((MoveListMove) this.movesTreeView.getSelectionModel().getSelectedMove().getValue());
            /*
             * this.movesList.getMoves().remove(this.currentMove); this.movesList.getMoves().add(move);
             * this.movesList.getMoves().sort(Comparator.naturalOrder());
             * this.movesList.getSelectionModel().select(move); this.onEdit(move);
             */
        }
    }

    @Override
    public void onMove(MoveListItem move, int newIndex) {
    }

    @Override
    public void onRename(MoveListItem move, String name) {
    }

    public void onSaveAllMoves() {
        Registries.moves().save(new File("../PMDMMO-common/resources/data/moves.xml"));
    }

    private void reloadList() {
        for (TreeItem<CustomTreeItem> move : this.categories)
            move.getChildren().clear();
        for (Move i : Registries.moves().toList()) {
            TreeItem<CustomTreeItem> tree;
            if (i.id <= 0)
                tree = this.categories[this.categories.length - 1];
            else if (i.id >= 2500)
                tree = this.categories[this.categories.length - 2];
            else if (i.getType() == PokemonType.Unknown)
                tree = this.categories[this.categories.length - 3];
            else
                tree = this.categories[i.getType().id];
            tree.getChildren().add(new TreeItem<>(new MoveListItem(i)));
        }
    }

}
