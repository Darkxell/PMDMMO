/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.model.ejb;

import com.darkxell.common.dbobject.DBPokemon;
import com.darkxell.common.dbobject.DatabaseIdentifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.sql.DataSource;

/**
 *
 * @author Darkxell
 */
@Stateless
@LocalBean
public class PokemonDAO {

    @Resource(mappedName = "jdbc/pmdmmodatabase")
    private DataSource ds;

    public long create(DBPokemon pokemon) {
        try {
            // ID AUTOINCREMENT CODE
            Connection cx = ds.getConnection();
            ResultSet result = cx
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT MAX(id) as id FROM pokemon"
                    );
            cx.close();
            // ADD THE POKEMON
            if (result.first()) {
                long newid = result.getLong("id") + 1;
                Connection cn = ds.getConnection();
                PreparedStatement prepare
                        = cn.prepareStatement(
                                "INSERT INTO pokemon (id, specieid, formid, abilityid, gender, nickname, level, experience, stat_atk, stat_def, stat_spatk, stat_spdef, stat_hp, isshiny, iq) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                        );
                prepare.setLong(1, newid);
                prepare.setInt(2, pokemon.specieid);
                prepare.setInt(3, pokemon.formid);
                prepare.setInt(4, pokemon.abilityid);
                prepare.setInt(5, pokemon.gender);
                prepare.setString(6, pokemon.nickname);
                prepare.setInt(7, pokemon.level);
                prepare.setLong(8, pokemon.experience);
                prepare.setInt(9, pokemon.stat_atk);
                prepare.setInt(10, pokemon.stat_def);
                prepare.setInt(11, pokemon.stat_speatk);
                prepare.setInt(12, pokemon.stat_spedef);
                prepare.setInt(13, pokemon.stat_hp);
                prepare.setBoolean(14, pokemon.isshiny);
                prepare.setInt(15, pokemon.iq);
                prepare.executeUpdate();
                cn.close();

                return newid;
            } else {
                System.err.println("Could not autoincrement properly.");
            }
        } catch (Exception e) {
            System.out.println("Error while trying to add a new  pokemon the the database.");
            e.printStackTrace();
        }
        return 0;
    }

    public void delete(DBPokemon pokemon) {
        try {
            Connection cn = ds.getConnection();
            cn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            ).executeUpdate(
                    "DELETE FROM pokemon WHERE id = " + pokemon.id
            );
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DBPokemon find(long id) {
        DBPokemon toreturn = null;
        try {
            Connection cn = ds.getConnection();
            ResultSet result = cn
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT * FROM pokemon WHERE id = " + id
                    );
            if (result.first()) {
                toreturn = new DBPokemon(id, result.getInt("specieid"), result.getInt("formid"), result.getInt("abilityid"), result.getInt("gender"), result.getString("nickname"),
                        result.getInt("level"), result.getLong("experience"),result.getInt("iq"),result.getBoolean("isshiny"), result.getInt("stat_atk"), result.getInt("stat_def"), result.getInt("stat_spatk"), result.getInt("stat_spdef"),
                        result.getInt("stat_hp"), null, null);
            }
            cn.close();
            // Select held inventory id
            cn = ds.getConnection();
            String selectSQL = "SELECT * FROM holdeditem_ WHERE pokemonid = ?";
            PreparedStatement preparedStatement = cn.prepareStatement(selectSQL);
            preparedStatement.setLong(1, toreturn.id);
            result = preparedStatement.executeQuery();
            if (result.next()) {
                toreturn.holdeditem = new DatabaseIdentifier(result.getLong("stackid"));
            }
            cn.close();
            // Select moves id
            cn = ds.getConnection();
            selectSQL = "SELECT * FROM learnedmove_ WHERE pokemonid = ?";
            preparedStatement = cn.prepareStatement(selectSQL);
            preparedStatement.setLong(1, toreturn.id);
            result = preparedStatement.executeQuery();
            while (result.next()) {
                if (toreturn.learnedmoves == null) {
                    toreturn.learnedmoves = new ArrayList<>(4);
                }
                toreturn.learnedmoves.add(new DatabaseIdentifier(result.getLong("moveid")));
            }
            cn.close();
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toreturn;
    }

    public void update(DBPokemon pokemon) {
        try {
            Connection cn = ds.getConnection();
            PreparedStatement prepare
                    = cn.prepareStatement(
                            "UPDATE pokemon SET specieid = ?, formid = ?, abilityid = ?, gender = ?, nickname = ?, level = ?, experience = ?, stat_atk = ?, stat_def = ?, stat_spatk = ?, stat_spdef = ?, stat_hp = ?, iq = ?, isshiny = ? WHERE id = ?"
                    );
            prepare.setInt(1, pokemon.specieid);
            prepare.setInt(2, pokemon.formid);
            prepare.setInt(3, pokemon.abilityid);
            prepare.setInt(4, pokemon.gender);
            prepare.setString(5, pokemon.nickname);
            prepare.setInt(6, pokemon.level);
            prepare.setLong(7, pokemon.experience);
            prepare.setInt(8, pokemon.stat_atk);
            prepare.setInt(9, pokemon.stat_def);
            prepare.setInt(10, pokemon.stat_speatk);
            prepare.setInt(11, pokemon.stat_spedef);
            prepare.setInt(12, pokemon.stat_hp);
            prepare.setInt(13, pokemon.iq);
            prepare.setBoolean(14, pokemon.isshiny);
            prepare.setLong(15, pokemon.id);
            prepare.executeUpdate();
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
