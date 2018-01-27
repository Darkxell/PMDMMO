/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.model.ejb.dbobjects;

import java.util.ArrayList;

/**
 *
 * @author Darkxell
 */
public class DBPokemon {

    public long id;
    public int specieid;
    public int formid;
    public int abilityid;
    public int gender;
    public String nickname;
    public int level;
    public long experience;
    public int stat_atk;
    public int stat_def;
    public int stat_speatk;
    public int stat_spedef;
    public int stat_hp;
    public DatabaseIdentifier holdeditem;
    public ArrayList<DatabaseIdentifier> learnedmoves;

    public DBPokemon(long id, int specieid, int formid, int abilityid, int gender, 
            String nickname, int level, long experience, int stat_atk, int stat_def, 
            int stat_speatk, int stat_spedef, int stat_hp, DatabaseIdentifier holdeditem, 
            ArrayList<DatabaseIdentifier> learnedmoves) {
        this.id = id;
        this.specieid = specieid;
        this.formid = formid;
        this.abilityid = abilityid;
        this.gender = gender;
        this.nickname = nickname;
        this.level = level;
        this.experience = experience;
        this.stat_atk = stat_atk;
        this.stat_def = stat_def;
        this.stat_speatk = stat_speatk;
        this.stat_spedef = stat_spedef;
        this.stat_hp = stat_hp;
        this.holdeditem = holdeditem;
        this.learnedmoves = learnedmoves;
    }

}
