/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.model.ejb;

import com.darkxell.model.ejb.dbobjects.DBLearnedmove;
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
public class LearnedmoveDAO {

    @Resource(mappedName = "jdbc/pmdmmodatabase")
    private DataSource ds;

    public void create(DBLearnedmove move) {
        try {
            // ID AUTOINCREMENT CODE
            Connection cx = ds.getConnection();
            ResultSet result = cx
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT MAX(id) as id FROM learnedmove"
                    );
            cx.close();
            // ADD THE PLAYER
            if (result.first()) {
                long newid = result.getLong("id") + 1;
                Connection cn = ds.getConnection();
                PreparedStatement prepare
                        = cn.prepareStatement(
                                "INSERT INTO learnedmove (id, slot, moveid, ppmax, islinked, addedlevel) VALUES(?, ?, ?, ?, ?, ?)"
                        );
                prepare.setLong(1, newid);
                prepare.setInt(2, move.slot);
                prepare.setInt(3, move.moveid);
                prepare.setInt(4, move.ppmax);
                prepare.setBoolean(5, move.islinked);
                prepare.setInt(6, move.addedlevel);
                prepare.executeUpdate();
                cn.close();
            } else {
                System.err.println("Could not autoincrement properly.");
            }
        } catch (Exception e) {
            System.out.println("Error while trying to add a new move the the database.");
            e.printStackTrace();
        }
    }

    public void delete(DBLearnedmove move) {
        try {
            Connection cn = ds.getConnection();
            cn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            ).executeUpdate(
                    "DELETE FROM learnedmove WHERE id = " + move.id
            );
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DBLearnedmove find(long id) {
        DBLearnedmove toreturn = null;
        try {
            Connection cn = ds.getConnection();
            ResultSet result = cn
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT * FROM learnedmove WHERE id = " + id
                    );
            if (result.first()) {
                toreturn = new DBLearnedmove(id, result.getInt("slot"), result.getInt("moveid"),
                        result.getInt("ppmax"), result.getBoolean("islinked"), result.getInt("addedlevel"));
            }
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toreturn;
    }

    public void update(DBLearnedmove move) {
        try {
            Connection cn = ds.getConnection();
            PreparedStatement prepare
                    = cn.prepareStatement(
                            "UPDATE learnedmove SET slot = ?, moveid = ?, ppmax = ?, islinked = ?, addedlevel = ? WHERE id = ?"
                    );
            prepare.setInt(1, move.slot);
            prepare.setInt(2, move.moveid);
            prepare.setInt(3, move.ppmax);
            prepare.setBoolean(4, move.islinked);
            prepare.setInt(5, move.addedlevel);
            prepare.setLong(6, move.id);
            prepare.executeUpdate();
            cn.close();
            //TODO: add the references here too
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
