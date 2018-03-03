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
public class Toolbox_DAO {

    @Resource(mappedName = "jdbc/pmdmmodatabase")
    private DataSource ds;

    public void create(long playerid, long inventoryid) {
        try {
            Connection cn = ds.getConnection();
            PreparedStatement prepare
                    = cn.prepareStatement(
                            "INSERT INTO toolbox_ (playerid, inventoryid) VALUES (?, ?)"
                    );
            prepare.setLong(1, playerid);
            prepare.setLong(2, inventoryid);
            prepare.executeUpdate();
            cn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(long playerid, long inventoryid) {
        try {
            Connection cn = ds.getConnection();
            cn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            ).executeUpdate(
                    "DELETE FROM toolbox_ WHERE playerid = " + playerid + " AND inventoryid = " + inventoryid
            );
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long findInventoryID(long playerid) {
        long toreturn = 0;
        try {
            Connection cn = ds.getConnection();
            ResultSet result = cn
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT * FROM toolbox_ WHERE playerid = " + playerid
                    );
            if (result.first()) {
                toreturn = result.getLong("inventoryid");
            }
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toreturn;
    }

    public long findPlayerID(long inventoryid) {
        long toreturn = 0;
        try {
            Connection cn = ds.getConnection();
            ResultSet result = cn
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT * FROM toolbox_ WHERE inventoryid = " + inventoryid
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
}
