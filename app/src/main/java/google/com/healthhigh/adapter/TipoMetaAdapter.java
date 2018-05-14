package google.com.healthhigh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.healthhigh.R;

import java.util.List;

import google.com.healthhigh.domain.Meta;
import google.com.healthhigh.domain.TipoMeta;
import google.com.healthhigh.utils.DataHelper;
import google.com.healthhigh.viewholders.MetaViewHolder;
import google.com.healthhigh.viewholders.TipoMetaViewHolder;

/**
 * Created by Alan on 26/07/2017.
 */

public class TipoMetaAdapter extends RecyclerView.Adapter {
    private String E_TAG = "MetaListAdapter";
    private List<TipoMeta> metas;
    private Context c;
    public TipoMetaAdapter(Context c, List<TipoMeta> m){
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
        TipoMetaViewHolder tm_vh = (TipoMetaViewHolder) holder;

    }

    @Override
    public int getItemCount() {
        return this.metas.size();
    }
}
