/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.model.ejb;

import com.darkxell.model.ejb.dbobjects.DBInventory;
import com.darkxell.model.ejb.dbobjects.DBPlayer;
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
public class InventoryDAO {

    @Resource(mappedName = "jdbc/pmdmmodatabase")
    private DataSource ds;

    public void create(DBInventory inventory) {
        try {
            // ID AUTOINCREMENT CODE
            Connection cx = ds.getConnection();
            ResultSet result = cx
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT MAX(id) as id FROM inventory"
                    );
            cx.close();
            // ADD THE INVENTORY
            if (result.first()) {
                long newid = result.getLong("id") + 1;
                Connection cn = ds.getConnection();
                PreparedStatement prepare
                        = cn.prepareStatement(
                                "INSERT INTO inventory (id maxsize) VALUES(?, ?)"
                        );
                prepare.setLong(1, newid);
                prepare.setInt(2, inventory.maxsize);
                prepare.executeUpdate();
                cn.close();
            } else {
                System.err.println("Could not autoincrement properly.");
            }
        } catch (Exception e) {
            System.out.println("Error while trying to add a new  inventory the the database.");
            e.printStackTrace();
        }
    }

    public void delete(DBInventory inventory) {
        try {
            Connection cn = ds.getConnection();
            cn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            ).executeUpdate(
                    "DELETE FROM inventory WHERE id = " + inventory.id
            );
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DBInventory find(long id) {
        DBInventory toreturn = null;
        try {
            Connection cn = ds.getConnection();
            ResultSet result = cn
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT * FROM inventory WHERE id = " + id
                    );
            if (result.first()) {
                toreturn = new DBInventory(id, result.getInt("maxsize"), null);
            }
            cn.close();
            //TODO: add the references here
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toreturn;
    }

    public void update(DBPlayer player) {
        try {
            Connection cn = ds.getConnection();
            PreparedStatement prepare
                    = cn.prepareStatement(
                            "UPDATE player SET moneyinbank = ?, moneyinbag = ?, name = ?, passhash = ? WHERE id = ?"
                    );
            prepare.setLong(1, player.moneyinbank);
            prepare.setLong(2, player.moneyinbag);
            prepare.setString(3, player.name);
            prepare.setLong(4, player.passhash);
            prepare.executeUpdate();
            cn.close();
            //TODO: add the references here too
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
