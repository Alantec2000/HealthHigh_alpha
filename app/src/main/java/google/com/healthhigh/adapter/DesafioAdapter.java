package google.com.healthhigh.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.viewholders.DesafioViewHolder;
import google.com.healthhigh.viewholders.MedalhaViewHolder;

import com.google.healthhigh.R;

/**
 * Created by Alan on 16/05/2017.
 */

public class DesafioAdapter extends RecyclerView.Adapter {
    private final List<Desafio> desafios;
    private final String E_TAG = "DESAFIO_LIST_ERROR";
    private final Context c;
    public DesafioAdapter(List<Desafio> desafios, Context c) {
        this.desafios = desafios;
        this.c = c;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.layout_linha_desafio, parent, false);
        return new DesafioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DesafioViewHolder dHolder = (DesafioViewHolder) holder;
        Desafio d = desafios.get(position);
        dHolder.getTitulo().setText(d.getTitulo());
        dHolder.getDescricao().setText(d.getDescricao());
        dHolder.getFoto().setImageResource(R.drawable.taekwondo);
        dHolder.setD(d);
    }

    @Override
    public int getItemCount() {
        return this.desafios.size();
    }
}
