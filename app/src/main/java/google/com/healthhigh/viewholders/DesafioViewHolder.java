package google.com.healthhigh.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.healthhigh.R;

import google.com.healthhigh.activities.DetalhesDesafios;
import google.com.healthhigh.domain.Desafio;

/**
 * Created by Alan on 09/07/2017.
 */

public class DesafioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    final TextView titulo;
    final TextView descricao;
    final ImageView foto;
    final TextView tentativas;
    private Desafio d;
    private Context c;

    public DesafioViewHolder(View v){
        super(v);
        v.setOnClickListener(this);
        foto = (ImageView) v.findViewById(R.id.iconeDesafio);
        titulo = (TextView) v.findViewById(R.id.tituloDesafio);
        tentativas = (TextView) v.findViewById(R.id.contadorTentativas);
        descricao = (TextView) v.findViewById(R.id.descricaoDesafio);
        c = v.getContext();
    }
    public TextView getTitulo() {
        return titulo;
    }
    public TextView getDescricao() {
        return descricao;
    }
    public ImageView getFoto() {
        return foto;
    }
    public TextView getTentativas() {
        return tentativas;
    }
    public void setD(Desafio d) {
        this.d = d;
    }

    @Override
    public void onClick(View v) {
        if(this.d != null){
            Intent i = new Intent(c, DetalhesDesafios.class);
            i.putExtra(DetalhesDesafios.DESAFIO_ACTION, d.getId());
            c.startActivity(i);
        }
    }
}
