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
public class FriendAreas_DAO {
    
    @Resource(mappedName = "jdbc/pmdmmodatabase")
    private DataSource ds;
    
    public void create(long playerid, String areaid) {
        try {
            Connection cn = ds.getConnection();
            PreparedStatement prepare
                    = cn.prepareStatement(
                            "INSERT INTO friendareas_ (playerid, areaid) VALUES (?, ?)"
                    );
            prepare.setLong(1, playerid);
            prepare.setString(2, areaid);
            prepare.executeUpdate();
            cn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(long playerid, String areaid) {
        try {
            Connection cn = ds.getConnection();
            PreparedStatement prepare
                    = cn.prepareStatement(
                            "DELETE FROM friendareas_ WHERE playerid = ? AND areaid = ?"
                    );
            prepare.setLong(1, playerid);
            prepare.setString(2, areaid);
            prepare.executeUpdate();
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> findAreas(long playerid) {
        ArrayList<String> toreturn = new ArrayList<String>();
        try {
            Connection cn = ds.getConnection();
            ResultSet result = cn
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT * FROM friendareas_ WHERE playerid = " + playerid
                    );

            while (result.next()) {
                toreturn.add(result.getString("areaid"));
            }
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toreturn;
    }
    
}
