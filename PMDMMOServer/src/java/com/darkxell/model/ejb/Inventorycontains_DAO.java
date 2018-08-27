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
public class Inventorycontains_DAO {

    @Resource(mappedName = "jdbc/pmdmmodatabase")
    private DataSource ds;

    public void create(long stackid, long inventoryid) {
        try {
            Connection cn = ds.getConnection();
            PreparedStatement prepare
                    = cn.prepareStatement(
                            "INSERT INTO inventorycontains_ (stackid, inventoryid) VALUES (?, ?)"
                    );
            prepare.setLong(1, stackid);
            prepare.setLong(2, inventoryid);
            prepare.executeUpdate();
            cn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(long stackid, long inventoryid) {
        try {
            Connection cn = ds.getConnection();
            cn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            ).executeUpdate(
                    "DELETE FROM inventorycontains_ WHERE stackid = " + stackid + " AND inventoryid = " + inventoryid
            );
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long findInventoryID(long stackid) {
        long toreturn = 0;
        try {
            Connection cn = ds.getConnection();
            ResultSet result = cn
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT * FROM inventorycontains_ WHERE stackid = " + stackid
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

    public ArrayList<Long> findStacksID(long inventoryid) {
        ArrayList<Long> toreturn = new ArrayList<>(20);
        try {
            Connection cn = ds.getConnection();
            ResultSet result = cn
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT * FROM inventorycontains_ WHERE inventoryid = " + inventoryid
                    );
            while (result.next()) {
                toreturn.add(result.getLong("stackid"));
            }
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toreturn;
    }
}
