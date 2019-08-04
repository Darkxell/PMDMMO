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
                    "DELETE FROM teammember_ WHERE playerid = " + playerid + " AND pokemonid = " + pokemonid
            );
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(long playerid, long pokemonid, short location) {
        try {
            Connection cn = ds.getConnection();
            PreparedStatement prepare
                    = cn.prepareStatement(
                            "UPDATE teammember_ SET location = ? WHERE playerid = ? AND pokemonid = ?"
                    );
            prepare.setShort(1, location);
            prepare.setLong(2, playerid);
            prepare.setLong(3, pokemonid);
            prepare.executeUpdate();
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

    /**
     * Returns the location of the first row containing this pokemonid. Returns
     * -1 if the pokemon isn't found.
     */
    public long findLocation(long pokemonid) {
        long toreturn = -1;
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
                toreturn = result.getLong("location");
            }
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toreturn;
    }

    public ArrayList<Long> findPokemonsID(long playerid) {
        ArrayList<Long> toreturn = new ArrayList<Long>(10);
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
                while (result.next()) {
                    toreturn.add(result.getLong("pokemonid"));
                }
            }
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toreturn;
    }

    public ArrayList<Long> findPokemonsIDinTeam(long playerid) {
        ArrayList<Long> toreturn = new ArrayList<Long>(4);
        try {
            Connection cn = ds.getConnection();
            ResultSet result = cn
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT * FROM teammember_ WHERE playerid = " + playerid + " AND location > 0 ORDER BY location"
                    );
            while (result.next()) {
                toreturn.add(result.getLong("pokemonid"));
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
                            "SELECT * FROM teammember_ WHERE playerid = " + playerid + " AND location = " + location
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
