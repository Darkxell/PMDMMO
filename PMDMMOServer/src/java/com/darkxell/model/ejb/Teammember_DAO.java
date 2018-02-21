/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.model.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class Teammember_DAO {

    @Resource(mappedName = "jdbc/pmdmmodatabase")
    private DataSource ds;

    public void create(long playerid, long pokemonid, byte location) {
        try {
            Connection cn = ds.getConnection();
            PreparedStatement prepare
                    = cn.prepareStatement(
                            "INSERT INTO teammember_ (playerid, pokemonid, location) VALUES (?, ?, ?)"
                    );
            prepare.setLong(1, playerid);
            prepare.setLong(2, pokemonid);
            prepare.setByte(3, location);
            prepare.executeUpdate();
            cn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(long playerid, long pokemonid) {
        try {
            Connection cn = ds.getConnection();
            cn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            ).executeUpdate(
                    "DELETE FROM teammember_ WHERE stackid = " + playerid + " AND inventoryid = " + pokemonid
            );
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long findPlayerID(long pokemonid) {
        long toreturn = 0;
        try {
            Connection cn = ds.getConnection();
            ResultSet result = cn
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT * FROM teammember_ WHERE pokemonid = " + pokemonid
                    );
            if (result.first()) {
                toreturn = result.getLong("playerid");
            }
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toreturn;
    }

    public long findPokemonID(long playerid) {
        long toreturn = 0;
        try {
            Connection cn = ds.getConnection();
            ResultSet result = cn
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT * FROM teammember_ WHERE playerid = " + playerid
                    );
            if (result.first()) {
                toreturn = result.getLong("pokemonid");
            }
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toreturn;
    }

    public long findPokemonID(long playerid, byte location) {
        long toreturn = 0;
        try {
            Connection cn = ds.getConnection();
            ResultSet result = cn
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT * FROM teammember_ WHERE playerid = " + playerid + "AND location = " + location
                    );
            if (result.first()) {
                toreturn = result.getLong("pokemonid");
            }
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toreturn;
    }
}
