package fr.darkxell.dataeditor.application.util;

public class TreeCategory extends CustomTreeItem {

    public final String name;

    public TreeCategory(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
