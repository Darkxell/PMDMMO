package com.darkxell.model.ejb;

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
public class PlayerDAO {

    @Resource(mappedName = "jdbc/pmdmmodatabase")
    private DataSource ds;

    public void create(DBPlayer player) {
        try {
            // ID AUTOINCREMENT CODE
            Connection cx = ds.getConnection();
            ResultSet result = cx
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT MAX(id) as id FROM player"
                    );
            cx.close();
            if (result.first()) {
                // Check if there isn't a player already nammed like this
                try {
                    Connection cn = ds.getConnection();
                    ResultSet nametest = cn
                            .createStatement(
                                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                                    ResultSet.CONCUR_UPDATABLE
                            ).executeQuery(
                                    "SELECT * FROM player WHERE name = " + player.name
                            );
                    if (nametest.first()) {
                        System.out.println("Refused player creation: " + player.name + " is already a used name in the database.");
                        return;
                    }
                    cn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                // ADD THE PLAYER
                long newid = result.getLong("id") + 1;
                Connection cn = ds.getConnection();
                PreparedStatement prepare
                        = cn.prepareStatement(
                                "INSERT INTO player (moneyinbank, moneyinbag, name, passhash, id) VALUES(?, ?, ?, ?, ?)"
                        );
                prepare.setLong(1, player.moneyinbank);
                prepare.setLong(2, player.moneyinbag);
                prepare.setString(3, player.name);
                prepare.setString(4, player.passhash);
                prepare.setLong(5, newid);
                prepare.executeUpdate();
                cn.close();
            } else {
                System.err.println("Could not autoincrement properly.");
            }
        } catch (Exception e) {
            System.err.println("Error while trying to add a new  player the the database.");
            e.printStackTrace();
        }
    }

    public void delete(DBPlayer player) {
        try {
            Connection cn = ds.getConnection();
            cn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            ).executeUpdate(
                    "DELETE FROM player WHERE id = " + player.id
            );
            cn.close();
            System.out.print("Player " + player.name + " has been deleted from the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DBPlayer find(long id) {
        DBPlayer toreturn = null;
        try {
            Connection cn = ds.getConnection();
            ResultSet result = cn
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT * FROM player WHERE id = " + id
                    );
            if (result.first()) {
                toreturn = new DBPlayer(id, result.getString("name"), result.getString("passhash"),
                        result.getLong("moneyinbank"), result.getLong("moneyinbag"),
                        null, null, null, null, null);
            }
            cn.close();
            //TODO: add the references here
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toreturn;
    }

    public DBPlayer find(String name) {
        DBPlayer toreturn = null;
        if (name == null || name.equals("")) 
            return toreturn;
        try {
            Connection cn = ds.getConnection();
            ResultSet result = cn
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT * FROM player WHERE name = " + name
                    );
            if (result.first()) {
                toreturn = new DBPlayer(result.getLong("id"), result.getString("name"), result.getString("passhash"),
                        result.getLong("moneyinbank"), result.getLong("moneyinbag"),
                        null, null, null, null, null);
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
            prepare.setString(4, player.passhash);
            prepare.setLong(5, player.id);
            prepare.executeUpdate();
            cn.close();
            //TODO: add the references here too
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
