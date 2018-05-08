package google.com.healthhigh.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.healthhigh.R;

import google.com.healthhigh.domain.Meta;
import google.com.healthhigh.utils.Toaster;

/**
 * Created by Alan on 26/07/2017.
 */

public class MetaViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
    private TextView numero;
    private TextView titulo;
    private TextView descricao;
    private TextView tipo;
    private TextView quantidade;
    private TextView tempo;
    private Meta meta;
    private Context c;
    public MetaViewHolder(View view) {
        super(view);
        numero = (TextView) view.findViewById(R.id.numeroMeta);
        titulo = (TextView) view.findViewById(R.id.tituloMeta);
        descricao = (TextView) view.findViewById(R.id.descricaoMeta);
        tipo = (TextView) view.findViewById(R.id.tipoMeta);
        quantidade = (TextView) view.findViewById(R.id.quantidadeTipoMeta);
        tempo = (TextView) view.findViewById(R.id.tempoMeta);
    }

    public TextView getNumero() {
        return numero;
    }
    public TextView getTitulo() {
        return titulo;
    }
    public TextView getDescricao() {
        return descricao;
    }
    public TextView getTipo() {
        return tipo;
    }
    public TextView getQuantidade() {
        return quantidade;
    }
    public TextView getTempo() {
        return tempo;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    @Override
    public boolean onLongClick(View v) {
        Toaster.toastShortMessage(v.getContext(), meta.getDescricao());
        return false;
    }
}
