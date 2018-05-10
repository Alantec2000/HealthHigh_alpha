package google.com.healthhigh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.healthhigh.R;

import java.util.List;

import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.utils.DataHelper;
import google.com.healthhigh.viewholders.DesafioViewHolder;

/**
 * Created by Alan on 16/05/2017.
 */

public class DesafioAdapter extends RecyclerView.Adapter {
    private final List<Desafio> desafios;
    private final String E_TAG = "DESAFIO_LIST_ERROR";
    private final Context context;
    public DesafioAdapter(List<Desafio> d, Context c) {
        desafios = d;
        this.context = c;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_linha_desafio, parent, false);
        return new DesafioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DesafioViewHolder dHolder = (DesafioViewHolder) holder;
        Desafio d = desafios.get(position);
        dHolder.getTitulo().setText(d.getTitulo());
        dHolder.getDescricao().setText(d.getDescricao());
        dHolder.setData_criacao().setText(DataHelper.toDateString(d.getData_criacao()));
        dHolder.setDesafio(d);
    }

    @Override
    public int getItemCount() {
        return this.desafios.size();
    }
}
