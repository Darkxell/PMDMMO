package fr.darkxell.dataeditor.application.data.floortable;

import com.darkxell.common.pokemon.PokemonType;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.Callback;

public class FloorTablePokemonType extends FloorTableItem<PokemonType, PokemonType> {

    public FloorTablePokemonType(int floor, PokemonType value) {
        super(floor, value);
    }

    @Override
    public Callback<TableColumn<FloorTableItem<PokemonType, PokemonType>, PokemonType>, TableCell<FloorTableItem<PokemonType, PokemonType>, PokemonType>> cellFactory() {
        return ComboBoxTableCell.forTableColumn(PokemonType.values());
    }

    @Override
    public FloorTableItem<PokemonType, PokemonType> copy() {
        return new FloorTablePokemonType(this.floor, this.value);
    }

    @Override
    public PokemonType getValue() {
        return this.value;
    }

    @Override
    public void onValueEdited(PokemonType newValue) {
        this.value = newValue;
    }

}
