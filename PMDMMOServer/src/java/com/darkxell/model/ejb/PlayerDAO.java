package com.darkxell.model.ejb;

import com.darkxell.model.ejb.dbobjects.DBPlayer;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

/**
 *
 * @author Darkxell
 */
@Stateless
public class PlayerDAO {

    @Resource(mappedName = "jdbc/pmdmmodatabase")
    private DataSource ds;

    public void create(DBPlayer player) {
        //TODO
    }

    public void delete(DBPlayer obj) {
    }

    public DBPlayer find(long id) {
        return new DBPlayer(1, "", 1, 0, 0, null,null, null,null,null);
    }

    public void update(DBPlayer obj) {
    }
}
