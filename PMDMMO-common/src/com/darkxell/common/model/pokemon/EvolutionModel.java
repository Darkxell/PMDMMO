package com.darkxell.common.model.pokemon;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "evolution")
@XmlAccessorType(XmlAccessType.FIELD)
public class EvolutionModel {
    /**
     * Evolution methods.<br />
     * <ul>
     * <li>LEVEL = 0</li>
     * <li>ITEM = 1</li>
     * <li>IQ = 2</li>
     * </ul>
     */
    public static final byte LEVEL = 0, ITEM = 1, IQ = 2;

    /** How this Evolution occurs. See {@link EvolutionModel#LEVEL}. */
    @XmlAttribute
    private Byte method;

    /** The ID of the species this Evolution leads to. */
    @XmlAttribute
    private int species;

    /** The ID of the species form this Evolution leads to. */
    @XmlAttribute
    private Integer speciesForm;

    /**
     * If method is LEVEL, this is the minimum level of the Pokemon.<br />
     * If method is ITEM, this is the ID of the Item to be used on.<br />
     * If method is IQ, this is the minimum IQ of the Pokemon.
     */
    @XmlAttribute
    private int value;
    
    public EvolutionModel() {}

    public EvolutionModel(int species, Integer speciesForm, byte method, int value) {
        this.species = species;
        this.speciesForm = speciesForm;
        this.method = method;
        this.value = value;
    }

    public EvolutionModel copy() {
        return new EvolutionModel(species, speciesForm, method, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EvolutionModel))
            return false;
        EvolutionModel o = (EvolutionModel) obj;
        return this.method.equals(o.method) && this.species == o.species && this.speciesForm.equals(o.speciesForm)
                && this.value == o.value;
    }

    public Byte getMethod() {
        return method;
    }

    public int getSpecies() {
        return species;
    }

    public Integer getSpeciesForm() {
        return speciesForm;
    }

    public int getValue() {
        return value;
    }

    public void setMethod(Byte method) {
        this.method = method;
    }

    public void setSpecies(int species) {
        this.species = species;
    }

    public void setSpeciesForm(Integer speciesForm) {
        this.speciesForm = speciesForm;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
