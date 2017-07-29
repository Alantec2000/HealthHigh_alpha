package google.com.healthhigh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.healthhigh.R;

import java.util.List;
import java.util.TreeMap;

import google.com.healthhigh.domain.Meta;
import google.com.healthhigh.utils.BitmapUtil;
import google.com.healthhigh.utils.DataHelper;
import google.com.healthhigh.viewholders.MedalhaViewHolder;
import google.com.healthhigh.viewholders.MetaViewHolder;

/**
 * Created by Alan on 26/07/2017.
 */

public class MetaListAdapter extends RecyclerView.Adapter {
    private String E_TAG = "MetaListAdapter";
    private TreeMap<Integer, Meta> metas;
    private Context c;
    public MetaListAdapter(Context c, TreeMap<Integer, Meta> m){
        this.c = c;
        this.metas = m;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.layout_entrada_meta, parent, false);
        return new MetaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MetaViewHolder mVH = (MetaViewHolder) holder;
        Meta m = null;
        try {
            m = metas.get(position);
        } catch (Exception e) {
            Log.e(E_TAG, e.getMessage());
        }
        if(m != null){
            mVH.getNumero().setText("Meta Nº" + position + 1);
            mVH.getTitulo().setText(m.getNome());
            mVH.getDescricao().setText(m.getDescricao());
            mVH.getTipo().setText(Meta.getTipoMeta(m.getTipo()));
            mVH.getQuantidade().setText(Integer.toString(m.getQtde()));
            String data = DataHelper.parseUT(m.getTempo()/1000, "dd/MM/y");
            data = "Tempo Limite: " + data + "";
            mVH.getTempo().setText(data);
        }
    }

    @Override
    public int getItemCount() {
        return this.metas.size();
    }
}
