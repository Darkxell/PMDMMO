/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver.servermechanics;

import com.darkxell.common.dbobject.DBInventory;
import com.darkxell.common.dbobject.DBItemstack;
import com.darkxell.common.item.ItemRegistry;
import com.darkxell.gameserver.GameServer;
import com.darkxell.gameserver.GameSessionInfo;
import java.util.ArrayList;

/**
 *
 * @author Darkxell
 */
public class GiveManager {

    public static boolean giveItem(int itemID, int quantity, GameSessionInfo si, GameServer endpoint, boolean directstorage) {
        return giveItem(itemID, quantity, si.serverid, endpoint, directstorage);
    }

    /**
     * Gives an item stack to a player. This will try to stack items and might
     * not create a new DBItemstack tuple in the database.
     *
     * @return true if the inventory was full and the give overflowed into the
     * storage
     */
    public static boolean giveItem(int itemID, int quantity, long serverid, GameServer endpoint, boolean directstorage) {
        if (quantity == 0) {
            return false;
        }
        boolean full = false;
        DBInventory storage = endpoint.getInventoryDAO().find(endpoint.getPlayerStorage_DAO().findInventoryID(serverid));
        DBInventory toolbox = endpoint.getInventoryDAO().find(endpoint.getToolbox_DAO().findInventoryID(serverid));
        //Puts as many items as it can in the toolbox
        if (!directstorage) {
            ArrayList<Long> itemsintoolbox = endpoint.getInventoryContains_DAO().findStacksID(toolbox.id);
            int space = toolbox.maxsize - itemsintoolbox.size();

            //If the item is stackable, tries to stack it
            if (ItemRegistry.find(itemID).isStackable) {
                for (Long stackid : itemsintoolbox) {
                    DBItemstack stack = endpoint.getItemstackDAO().find(stackid);
                    if (stack.itemid == itemID) {
                        stack.quantity += quantity;
                        endpoint.getItemstackDAO().update(stack);
                        return false;
                    }
                }
                if (space >= 1) {
                    DBItemstack stack = new DBItemstack(0, itemID, quantity);
                    long newid = endpoint.getItemstackDAO().create(stack);
                    endpoint.getInventoryContains_DAO().create(newid, toolbox.id);
                    return false;
                }
            }
            //Puts what it can in the toolbox
            while (space > 0 && quantity > 0) {
                DBItemstack toput = new DBItemstack(0, itemID, 1);
                long createdid = endpoint.getItemstackDAO().create(toput);
                endpoint.getInventoryContains_DAO().create(createdid, toolbox.id);
                quantity--;
                space--;
            }
            if (space == 0 && quantity > 0) {
                full = true;
            }
        }
        //Puts what's left to put in the storage
        ArrayList<Long> itemsinstorage = endpoint.getInventoryContains_DAO().findStacksID(storage.id);
        for (Long stackid : itemsinstorage) {
            DBItemstack stack = endpoint.getItemstackDAO().find(stackid);
            if (stack.itemid == itemID) {
                stack.quantity += quantity;
                endpoint.getItemstackDAO().update(stack);
                return directstorage ? false : full;
            }
        }
        if (quantity > 0) {
            DBItemstack toput = new DBItemstack(0, itemID, quantity);
            long createdid = endpoint.getItemstackDAO().create(toput);
            endpoint.getInventoryContains_DAO().create(createdid, storage.id);
        }
        return directstorage ? false : full;
    }

}
