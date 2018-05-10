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
        foto = (ImageView) v.findViewById(R.id.img_icone);
        titulo = (TextView) v.findViewById(R.id.txt_titulo);
        tentativas = (TextView) v.findViewById(R.id.txt_data_criacao);
        descricao = (TextView) v.findViewById(R.id.txt_descricao);
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
    public TextView setData_criacao() {
        return tentativas;
    }
    public void setDesafio(Desafio d) {
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
