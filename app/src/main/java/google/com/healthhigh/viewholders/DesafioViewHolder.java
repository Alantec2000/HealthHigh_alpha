package google.com.healthhigh.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.healthhigh.R;

import google.com.healthhigh.activities.DetalhesDesafios;
import google.com.healthhigh.activities.DetalhesDesafiosNew;
import google.com.healthhigh.domain.Desafio;

/**
 * Created by Alan on 09/07/2017.
 */

public class DesafioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    final TextView titulo;
    final TextView descricao;
    final ImageView foto;
    private TextView data_criacao;
    private TextView status_visualizacao;
    private TextView status_publicacao;
    TextView tentativas;
    private Desafio d;
    private Context c;

    public DesafioViewHolder(View v){
        super(v);
        v.setOnClickListener(this);
        foto = (ImageView) v.findViewById(R.id.img_icone_desafio);
        titulo = (TextView) v.findViewById(R.id.txt_titulo_desafio);
        data_criacao = (TextView) v.findViewById(R.id.txt_data_criacao_desafio);
        descricao = (TextView) v.findViewById(R.id.txt_descricao_desafio);
        status_visualizacao = (TextView) v.findViewById(R.id.txt_status_visualizacao_desafio);
        status_publicacao = (TextView) v.findViewById(R.id.txt_status_publicacao_desafio);
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
            Intent i = new Intent(c, DetalhesDesafiosNew.class);
            i.putExtra(DetalhesDesafiosNew.DESAFIO_ACTION, d.getId());
            c.startActivity(i);
        }
    }

    public void setData_criacao(TextView data_criacao) {
        this.data_criacao = data_criacao;
    }

    public TextView getStatus_visualizacao() {
        return status_visualizacao;
    }

    public void setStatus_visualizacao(TextView status_visualizacao) {
        this.status_visualizacao = status_visualizacao;
    }

    public TextView getData_criacao() {
        return data_criacao;
    }

    public TextView getStatus_publicacao() {
        return status_publicacao;
    }

    public void setStatus_publicacao(TextView status_publicacao) {
        this.status_publicacao = status_publicacao;
    }
}
