package google.com.healthhigh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.healthhigh.R;

import java.util.List;

import google.com.healthhigh.domain.Item;
import google.com.healthhigh.domain.Premiacao;
import google.com.healthhigh.utils.BitmapUtil;
import google.com.healthhigh.utils.DataHelper;
import google.com.healthhigh.viewholders.MedalhaViewHolder;

public class ItemGridAdapter extends RecyclerView.Adapter{
    private List<Premiacao> itens;
    private String E_TAG = "ITEM_GRID_ADAPTER_ERROR";
    private Context c;
    public ItemGridAdapter(List<Premiacao> itens, Context c){
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
        Premiacao i = null;
        try {
            i = itens.get(position);
        } catch (Exception e) {
            Log.e(E_TAG, e.getMessage());
        }
        if(i != null){
            medalhaVHolder.getNome().setText(i.getNome());
            String xp = "Experiência: " + Integer.toString(i.getExperiencia());
            String data = DataHelper.toDateString(i.getData_criacao());
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
