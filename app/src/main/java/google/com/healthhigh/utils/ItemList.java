package google.com.healthhigh.utils;

import android.content.Context;

import java.util.List;

import google.com.healthhigh.dao.ItemDAO;
import google.com.healthhigh.domain.Item;

public class ItemList {
    private ItemDAO itemDAO = null;
    public ItemList(Context act){
        itemDAO = new ItemDAO(act);
    }

    public List<Item> getItensList(){
        return itemDAO.getItens("SELECT * FROM " + ItemDAO.TABLE_NAME +
                                " WHERE " + ItemDAO.TIPO + " = 0 OR " +
                                ItemDAO.XP + " < 1000");
    }
}
