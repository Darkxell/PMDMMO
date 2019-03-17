package fr.darkxell.dataeditor.application.data.floortable;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import com.darkxell.common.Registries;
import com.darkxell.common.pokemon.PokemonType;

/**
 * @param <T> - Value type
 * @param <D> - Value display type
 */
public abstract class FloorTableItem<T, D> implements Comparable<FloorTableItem<T, D>> {
    public static final FloorTableBoolean defaultBoolean = new FloorTableBoolean(1, false);
    public static final FloorTableInteger defaultInteger = new FloorTableInteger(1, 1);
    public static final FloorTableMove defaultMove = new FloorTableMove(1, Registries.moves().find(1));
    public static final FloorTablePokemonType defaultPokemonType = new FloorTablePokemonType(1, PokemonType.Unknown);
    public static final FloorTableString defaultString = new FloorTableString(1, "");

    public int floor;
    public T value;

    public FloorTableItem(int floor, T value) {
        this.floor = floor;
        this.value = value;
    }

    public abstract Callback<TableColumn<FloorTableItem<T, D>, D>, TableCell<FloorTableItem<T, D>, D>> cellFactory();

    @Override
    public int compareTo(FloorTableItem<T, D> o) {
        return Integer.compare(this.floor, o.floor);
    }

    public abstract FloorTableItem<T, D> copy();

    public String getFloor() {
        return String.valueOf(this.floor);
    }

    public abstract D getValue();

    public abstract void onValueEdited(D newValue);

    @Override
    public String toString() {
        return this.floor + ": " + this.value;
    }

}
