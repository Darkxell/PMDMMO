package fr.darkxell.dataeditor.application.controller.item;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemRegistry;
import com.darkxell.common.model.item.ItemCategory;
import com.darkxell.common.model.item.ItemModel;
import com.darkxell.common.registry.Registries;

import fr.darkxell.dataeditor.application.controls.CustomListCell.ListCellParent;
import fr.darkxell.dataeditor.application.data.ItemListItem;
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

public class ItemsTabController implements Initializable, ListCellParent<ItemListItem> {

    public static ItemsTabController instance;

    private TreeItem<CustomTreeItem>[] categories;
    /** Currently edited Item. */
    public ItemListItem currentItem;
    @FXML
    public EditItemController editItemController;
    @FXML
    private TitledPane editItemPane;
    @FXML
    private TreeView<CustomTreeItem> itemsTreeView;

    Item defaultItem(int id) {
        return new Item(new ItemModel(id, ItemCategory.OTHERS, 0, 0, 0, 0, false, false, null));
    }

    @Override
    public Node graphicFor(ItemListItem item) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        TreeItem<CustomTreeItem> root = new TreeItem<>(new TreeCategory("Items"));
        root.setExpanded(true);
        this.itemsTreeView.setRoot(root);
        List<TreeItem<CustomTreeItem>> categories = this.itemsTreeView.getRoot().getChildren();
        categories.add(new TreeItem<>(new TreeCategory("Key Items")));
        categories.add(new TreeItem<>(new TreeCategory("Equipable")));
        categories.add(new TreeItem<>(new TreeCategory("Throwable")));
        categories.add(new TreeItem<>(new TreeCategory("Food")));
        categories.add(new TreeItem<>(new TreeCategory("Berries")));
        categories.add(new TreeItem<>(new TreeCategory("Drinks")));
        categories.add(new TreeItem<>(new TreeCategory("Gummis")));
        categories.add(new TreeItem<>(new TreeCategory("Seeds")));
        categories.add(new TreeItem<>(new TreeCategory("Other Usables")));
        categories.add(new TreeItem<>(new TreeCategory("Orbs")));
        categories.add(new TreeItem<>(new TreeCategory("TMs")));
        categories.add(new TreeItem<>(new TreeCategory("HMs")));
        categories.add(new TreeItem<>(new TreeCategory("Evolutionary")));
        categories.add(new TreeItem<>(new TreeCategory("Others")));
        this.categories = categories.toArray(new TreeItem[0]);

        /*
         * this.itemsList.setCellFactory(param -> { return new CustomListCell<>(AnimationsTabController.instance,
         * "Animation").setCanOrder(false).setCanCreate(false).setCanDelete(false) .setCanRename(false); });
         */
        this.itemsTreeView.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2) {
                TreeItem<CustomTreeItem> item = itemsTreeView.getSelectionModel().getSelectedItem();
                if (item.getValue() instanceof TreeCategory)
                    item.setExpanded(!item.isExpanded());
                else
                    onEdit((ItemListItem) item.getValue());
            }
        });

        this.reloadList();/*
                           * CustomList.setup(this, this.itemsList, "Item", true, false, true, true, false);
                           * this.itemsList.getItems().addAll(ItemRegistry.list());
                           * this.itemsList.getItems().sort(Comparator.naturalOrder());
                           */

        this.editItemPane.setVisible(false);
    }

    @Override
    public void onCreate(ItemListItem nullItem) {
        this.onCreateItem();
    }

    public void onCreateItem() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("New Item");
        dialog.setHeaderText(null);
        dialog.setContentText("Type in the ID of the new Item.");
        Optional<String> name = dialog.showAndWait();
        if (name.isPresent())
            if (name.get().matches("\\d+")) {
                ItemRegistry items = Registries.items();
                Item i = this.defaultItem(Integer.parseInt(name.get()));
                if (items.find(i.getID()) != null)
                    new Alert(AlertType.ERROR, "There is already an Item with ID " + i.getID(), ButtonType.OK).showAndWait();
                else {
                    items.register(i);
                    this.reloadList();
                }
            } else
                new Alert(AlertType.ERROR, "Wrong ID: " + name.get(), ButtonType.OK);
    }

    @Override
    public void onDelete(ItemListItem item) {
        if (item == this.currentItem) {
            this.currentItem = null;
            this.editItemPane.setVisible(false);
        }
        Registries.items().unregister(item.item.getID());
        this.reloadList();
    }

    public void onDeleteItem() {
        TreeItem<CustomTreeItem> selected = this.itemsTreeView.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;
        if (selected.getValue() instanceof ItemListItem)
            this.onDelete((ItemListItem) selected.getValue());
    }

    @Override
    public void onEdit(ItemListItem item) {
        this.currentItem = item;
        this.editItemPane.setVisible(true);
        this.editItemPane.setText(this.currentItem.item.name().toString());
        this.editItemController.setupFor(this.currentItem.item);
    }

    public void onEdited(Item item) {
        ItemRegistry items = Registries.items();
        boolean idChanged = this.currentItem.item.getID() != item.getID();
        if (idChanged && items.find(item.getID()) != null)
            new Alert(AlertType.ERROR, "Cannot save: There is already another Item with ID " + item.getID(), ButtonType.OK)
                    .showAndWait();
        else {
            items.unregister(this.currentItem.item.getID());
            items.register(item);
            this.reloadList();
            // this.onEdit((ItemListItem) this.itemsTreeView.getSelectionModel().getSelectedItem().getValue());
            /*
             * this.itemsList.getItems().remove(this.currentItem); this.itemsList.getItems().add(item);
             * this.itemsList.getItems().sort(Comparator.naturalOrder());
             * this.itemsList.getSelectionModel().select(item); this.onEdit(item);
             */
        }
    }

    @Override
    public void onMove(ItemListItem item, int newIndex) {
    }

    @Override
    public void onRename(ItemListItem item, String name) {
    }

    public void onSaveAllItems() {
        Registries.items().save(new File("../PMDMMO-common/resources/data/items.xml"));
    }

    private void reloadList() {
        for (TreeItem<CustomTreeItem> item : this.categories)
            item.getChildren().clear();
        for (Item i : Registries.items().toList())
            this.categories[i.getCategory().order].getChildren().add(new TreeItem<>(new ItemListItem(i)));
    }

}
