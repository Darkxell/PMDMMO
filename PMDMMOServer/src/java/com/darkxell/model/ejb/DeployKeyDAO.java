/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.model.ejb;

import com.darkxell.common.dbobject.DBDeployKey;
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
public class DeployKeyDAO {

    @Resource(mappedName = "jdbc/pmdmmodatabase")
    private DataSource ds;

    /**
     * Stores the DBdeployKey object in database.
     *
     * @return true if the key has bees stored, false otherwise.
     */
    public boolean create(DBDeployKey key) {
        try {
            Connection cn = ds.getConnection();
            PreparedStatement prepare
                    = cn.prepareStatement(
                            "INSERT INTO deploykey (keyvalue, timestamp, isused, playerid) VALUES (?, ?, ?, ?)"
                    );
            prepare.setString(1, key.keyvalue);
            prepare.setLong(2, key.timestamp);
            prepare.setBoolean(3, key.isused);
            prepare.setLong(4, key.playerid);
            prepare.executeUpdate();
            cn.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void delete(DBDeployKey key) {
        System.err.println("Tried to delete key:" + key.keyvalue + " via DAO. Deletion refused.");
    }

    public DBDeployKey find(String keystring) {
        DBDeployKey toreturn = null;
        if (keystring == null || keystring.equals("")) {
            return toreturn;
        }
        try {
            Connection cn = ds.getConnection();
            String selectSQL = "SELECT * FROM deploykey WHERE keyvalue = ?";
            PreparedStatement preparedStatement = cn.prepareStatement(selectSQL);
            preparedStatement.setString(1, keystring);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                toreturn = new DBDeployKey(result.getString("keyvalue"), result.getLong("timestamp"), result.getBoolean("isused"), result.getLong("playerid"));
            }
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toreturn;
    }
    
    public void update(DBDeployKey key) {
        try {
            Connection cn = ds.getConnection();
            PreparedStatement prepare
                    = cn.prepareStatement(
                            "UPDATE deploykey SET timestamp = ?, isused = ?, playerid = ? WHERE keyvalue = ?"
                    );
            prepare.setLong(1, key.timestamp);
            prepare.setBoolean(2, key.isused);
            prepare.setLong(3, key.playerid);
            prepare.setString(4, key.keyvalue);
            prepare.executeUpdate();
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
