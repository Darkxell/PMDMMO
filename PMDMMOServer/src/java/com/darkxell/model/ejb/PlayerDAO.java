package com.darkxell.model.ejb;

import com.darkxell.model.ejb.dbobjects.DBPlayer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            // ADD THE PLAYER
            if (result.first()) {
                long newid = result.getLong("id") + 1;
                Connection cn = ds.getConnection();
                PreparedStatement prepare
                        = cn.prepareStatement(
                                "INSERT INTO player (moneyinbank, moneyinbag, name, passhash, id) VALUES(?, ?, ?, ?, ?)"
                        );
                prepare.setLong(1, 1337);
                prepare.setLong(2, 999);
                prepare.setString(3, "Test Account");
                prepare.setLong(4, 1546l);
                prepare.setLong(5, newid);

                prepare.executeUpdate();
                cn.close();
            } else {
                System.err.println("Could not autoincrement properly.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(DBPlayer obj) {
    }

    public DBPlayer find(long id) {
        return new DBPlayer(1, "", 1, 0, 0, null, null, null, null, null);
    }

    public void update(DBPlayer obj) {
    }
}
