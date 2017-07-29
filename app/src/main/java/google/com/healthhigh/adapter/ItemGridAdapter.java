package google.com.healthhigh.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import google.com.healthhigh.domain.Item;
import google.com.healthhigh.utils.BitmapUtil;
import google.com.healthhigh.utils.DataHelper;
import google.com.healthhigh.viewholders.MedalhaViewHolder;
import com.google.healthhigh.R;

public class ItemGridAdapter extends RecyclerView.Adapter{
    private List<Item> itens;
    private String E_TAG = "ITEM_GRID_ADAPTER_ERROR";
    private Context c;
    public ItemGridAdapter(List<Item> itens, Context c){
        this.itens = itens;
        this.c = c;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.layout_entrada_medalha, parent, false);
        return new MedalhaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MedalhaViewHolder medalhaVHolder = (MedalhaViewHolder) holder;
        Item i = null;
        try {
            i = itens.get(position);
        } catch (Exception e) {
            Log.e(E_TAG, e.getMessage());
        }
        if(i != null){
            medalhaVHolder.getNome().setText(i.getNome());
            medalhaVHolder.getImg().setImageBitmap(BitmapUtil.getImage(i.getImg()));
            String xp = "Experiência: " + Integer.toString(i.getXp());
            String data = DataHelper.parseUT(i.getData()/1000, "dd/MM/y");
            data = "Criada em: " + data;
            medalhaVHolder.getXP().setText(xp);
            medalhaVHolder.getData().setText(data);
            medalhaVHolder.setItem(i);
        }
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }
}
