package fr.darkxell.dataeditor.application.controls;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;

public class CustomListCell<T> extends ListCell<T> {

    public static interface ListCellParent<T> {

        Node graphicFor(T item);

        void onCreate(T nullItem);

        void onDelete(T item);

        void onEdit(T item);

        void onMove(T item, int newIndex);

        void onRename(T item, String name);

        default double prefWidth(T item) {
            return Region.USE_COMPUTED_SIZE;
        }

    }

    public final ListCellParent<T> parent;

    public CustomListCell(ListCellParent<T> parent, boolean orderable) {
        this.parent = parent;

        if (orderable) {
            this.setOnDragDetected(event -> {
                if (this.getItem() == null)
                    return;
                Dragboard dragboard = this.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(String.valueOf(this.getListView().getSelectionModel().getSelectedIndex()));
                dragboard.setContent(content);

                event.consume();
            });

            this.setOnDragOver(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasString())
                    event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
            });

            this.setOnDragEntered(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasString())
                    this.setOpacity(0.3);
            });

            this.setOnDragExited(event -> {
                if (event.getGestureSource() != this && event.getDragboard().hasString())
                    this.setOpacity(1);
            });

            this.setOnDragDropped(event -> {
                if (this.getItem() == null)
                    return;

                Dragboard db = event.getDragboard();
                boolean success = false;
                T moved = null;
                ObservableList<T> items = this.getListView().getItems();

                if (db.hasString()) {
                    int draggedIdx = Integer.parseInt(db.getString());
                    int thisIdx = items.indexOf(this.getItem());
                    if (draggedIdx < thisIdx - 1)
                        --thisIdx;

                    T removed = items.remove(draggedIdx);
                    items.add(thisIdx, removed);
                    this.getListView().getSelectionModel().select(thisIdx);

                    success = true;
                    moved = removed;
                }
                event.setDropCompleted(success);

                event.consume();

                parent.onMove(moved, items.indexOf(moved));
            });

            this.setOnDragDone(DragEvent::consume);
        }
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        this.setText(empty ? "" : item.toString());
        this.setGraphic(this.parent.graphicFor(item));
        this.setPrefHeight(this.parent.prefWidth(item));
    }

}
