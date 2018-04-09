/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.model.ejb;

import com.darkxell.model.ejb.dbobjects.DBInventory;
import com.darkxell.model.ejb.dbobjects.DatabaseIdentifier;
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
public class InventoryDAO {

    @Resource(mappedName = "jdbc/pmdmmodatabase")
    private DataSource ds;

    public long create(DBInventory inventory) {
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
                                "INSERT INTO inventory (id, maxsize) VALUES(?, ?)"
                        );
                prepare.setLong(1, newid);
                prepare.setInt(2, inventory.maxsize);
                prepare.executeUpdate();
                cn.close();
                return newid;
            } else {
                System.err.println("Could not autoincrement properly.");
            }
        } catch (Exception e) {
            System.out.println("Error while trying to add a new  inventory the the database.");
            e.printStackTrace();
        }
        return 0;
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
            // match the inventory content
            cn = ds.getConnection();
            String selectSQL = "SELECT * FROM inventorycontains_ WHERE inventoryid = ?";
            PreparedStatement preparedStatement = cn.prepareStatement(selectSQL);
            preparedStatement.setLong(1, toreturn.id);
            result = preparedStatement.executeQuery();
            while (result.next()) {
                if(toreturn.content == null)
                    toreturn.content = new ArrayList<>(20);
                toreturn.content.add(new DatabaseIdentifier(result.getLong("stackid")));
            }
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toreturn;
    }

    public void update(DBInventory inventory) {
        try {
            Connection cn = ds.getConnection();
            PreparedStatement prepare
                    = cn.prepareStatement(
                            "UPDATE inventory SET maxsize = ? WHERE id = ?"
                    );
            prepare.setLong(1, inventory.maxsize);
            prepare.setLong(2, inventory.id);
            prepare.executeUpdate();
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
