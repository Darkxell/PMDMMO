/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.model.ejb;

import com.darkxell.model.ejb.dbobjects.DBItemstack;
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
public class ItemstackDAO {
    
    @Resource(mappedName = "jdbc/pmdmmodatabase")
    private DataSource ds;
    
    public void create(DBItemstack stack) {
        try {
            // ID AUTOINCREMENT CODE
            Connection cx = ds.getConnection();
            ResultSet result = cx
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT MAX(id) as id FROM itemstack"
                    );
            cx.close();
            // ADD THE INVENTORY
            if (result.first()) {
                long newid = result.getLong("id") + 1;
                Connection cn = ds.getConnection();
                PreparedStatement prepare
                        = cn.prepareStatement(
                                "INSERT INTO itemstack (id, itemid, quantity) VALUES(?, ?, ?)"
                        );
                prepare.setLong(1, newid);
                prepare.setInt(2, stack.itemid);
                prepare.setLong(3, stack.quantity);
                prepare.executeUpdate();
                cn.close();
            } else {
                System.err.println("Could not autoincrement properly.");
            }
        } catch (Exception e) {
            System.out.println("Error while trying to add a new itemstack in the database.");
            e.printStackTrace();
        }
    }
    
    public void delete(DBItemstack stack) {
        try {
            Connection cn = ds.getConnection();
            cn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            ).executeUpdate(
                    "DELETE FROM itemstack WHERE id = " + stack.id
            );
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public DBItemstack find(long id) {
        DBItemstack toreturn = null;
        try {
            Connection cn = ds.getConnection();
            ResultSet result = cn
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT * FROM itemstack WHERE id = " + id
                    );
            if (result.first()) {
                toreturn = new DBItemstack(id, result.getInt("itemid"), result.getLong("quantity"));
            }
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toreturn;
    }
    
    public void update(DBItemstack stack) {
        try {
            Connection cn = ds.getConnection();
            PreparedStatement prepare
                    = cn.prepareStatement(
                            "UPDATE itemstack SET itemid = ?, quantity = ? WHERE id = ?"
                    );
            prepare.setInt(1, stack.itemid);
            prepare.setLong(2, stack.quantity);
            prepare.setLong(3, stack.id);
            prepare.executeUpdate();
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
