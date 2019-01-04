/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.model.ejb;

import com.darkxell.common.util.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
public class TablesCreator {

    @Resource(mappedName = "jdbc/pmdmmodatabase")
    private DataSource ds;

    /**
     * Creates all the tables in the database.
     */
    public boolean createTables() {
        boolean toreturn = true;
        String[] tablescripts = new String[]{"create table DEPLOYKEY\n"
            + "(\n"
            + "	KEYVALUE VARCHAR(64) not null\n"
            + "		primary key,\n"
            + "	TIMESTAMP BIGINT,\n"
            + "	ISUSED BOOLEAN,\n"
            + "	PLAYERID BIGINT\n"
            + ")", "create table HOLDEDITEM_\n"
            + "(\n"
            + "	STACKID BIGINT not null,\n"
            + "	POKEMONID BIGINT not null\n"
            + ")", "create table INVENTORY\n"
            + "(\n"
            + "	ID BIGINT not null\n"
            + "		primary key,\n"
            + "	MAXSIZE SMALLINT not null\n"
            + ")", "create table INVENTORYCONTAINS_\n"
            + "(\n"
            + "	INVENTORYID BIGINT not null,\n"
            + "	STACKID BIGINT not null\n"
            + ")", "create table ITEMSTACK\n"
            + "(\n"
            + "	QUANTITY BIGINT not null,\n"
            + "	ITEMID SMALLINT default 1 not null,\n"
            + "	ID BIGINT default 0 not null\n"
            + "		primary key\n"
            + ")", "create table LEARNEDMOVE\n"
            + "(\n"
            + "	ID BIGINT not null\n"
            + "		primary key,\n"
            + "	SLOT SMALLINT default 0 not null,\n"
            + "	MOVEID SMALLINT default 0 not null,\n"
            + "	PPMAX SMALLINT default 5 not null,\n"
            + "	ISLINKED BOOLEAN default false not null,\n"
            + "	ADDEDLEVEL SMALLINT default 0 not null,\n"
            + "	ISENABLED BOOLEAN default true not null\n"
            + ")", "create table LEARNEDMOVE_\n"
            + "(\n"
            + "	POKEMONID BIGINT not null,\n"
            + "	MOVEID BIGINT not null\n"
            + ")", "create table MISSIONS_\n"
            + "(\n"
            + "	PLAYERID BIGINT not null,\n"
            + "	MISSIONID VARCHAR(256) not null\n"
            + ")", "create table PLAYER\n"
            + "(\n"
            + "	MONEYINBANK BIGINT default 0 not null,\n"
            + "	MONEYINBAG BIGINT default 0 not null,\n"
            + "	NAME VARCHAR(64) not null,\n"
            + "	ID BIGINT default 1 not null\n"
            + "		primary key,\n"
            + "	PASSHASH LONG VARCHAR,\n"
            + "	STORYPOSITION SMALLINT default 0,\n"
            + "	POINTS INTEGER default 0 not null,\n"
            + "	ISOP BOOLEAN default false,\n"
            + "	ISBANNED BOOLEAN default false\n"
            + ")", "create table PLAYERSTORAGE_\n"
            + "(\n"
            + "	PLAYERID BIGINT not null,\n"
            + "	INVENTORYID BIGINT not null\n"
            + ")", "create table POKEMON\n"
            + "(\n"
            + "	ID BIGINT not null\n"
            + "		primary key,\n"
            + "	SPECIEID SMALLINT not null,\n"
            + "	FORMID SMALLINT default 0 not null,\n"
            + "	ABILITYID SMALLINT default 0 not null,\n"
            + "	GENDER SMALLINT default 0 not null,\n"
            + "	NICKNAME VARCHAR(64),\n"
            + "	LEVEL SMALLINT default 1 not null,\n"
            + "	EXPERIENCE BIGINT default 0 not null,\n"
            + "	STAT_ATK SMALLINT default 1 not null,\n"
            + "	STAT_DEF SMALLINT default 1 not null,\n"
            + "	STAT_SPATK SMALLINT default 1 not null,\n"
            + "	STAT_SPDEF SMALLINT default 1 not null,\n"
            + "	STAT_HP SMALLINT default 1 not null,\n"
            + "	IQ BIGINT default 0 not null,\n"
            + "	ISSHINY BOOLEAN default false not null\n"
            + ")", "create table TEAMMEMBER_\n"
            + "(\n"
            + "	PLAYERID BIGINT not null,\n"
            + "	POKEMONID BIGINT not null,\n"
            + "	LOCATION SMALLINT not null\n"
            + ")", "create table TOOLBOX_\n"
            + "(\n"
            + "	PLAYERID BIGINT not null,\n"
            + "	INVENTORYID BIGINT not null\n"
            + ")"};
        for (int i = 0; i < tablescripts.length; i++) {
            try {
                Connection cn = ds.getConnection();
                PreparedStatement prepare
                        = cn.prepareStatement(
                                tablescripts[i]
                        );
                prepare.executeUpdate();
                cn.close();
            } catch (Exception e) {
                Logger.e("Could not create table index : " + i);
                e.printStackTrace();
                toreturn = false;
            }
        }
        return toreturn;
    }

    private static boolean flag = false;

    public boolean tablesExist() {
        if (flag) {
            return flag;
        }
        try {
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
                flag = true;
                return true;
            }
        } catch (Exception e) {
            Logger.e("Tables do not exist.");
            e.printStackTrace();
        }
        return false;
    }
}
