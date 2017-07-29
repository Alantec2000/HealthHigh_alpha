package google.com.healthhigh.utils;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.google.healthhigh.R;

import java.util.ArrayList;
import java.util.List;

import google.com.healthhigh.dao.ItemDAO;
import google.com.healthhigh.dao.ItemXMetaDAO;
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
