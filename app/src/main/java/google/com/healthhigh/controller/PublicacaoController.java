package google.com.healthhigh.controller;

import android.content.Context;

import java.util.List;

import google.com.healthhigh.dao.PublicacaoDAO;
import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.domain.InteracaoAtividade;
import google.com.healthhigh.domain.Publicacao;
import google.com.healthhigh.utils.DataHelper;

public class PublicacaoController {
    final PublicacaoDAO p_dao;
    final Context context;

    public PublicacaoController(Context context) {
        this.context = context;
        this.p_dao = new PublicacaoDAO(context);
    }

    public void setPublicacao(InteracaoAtividade i_a) {
        if(i_a != null){
            Publicacao p = p_dao.get(i_a);
            i_a.setPublicacao(p);
        }
    }

    public Publicacao getPublicacaoVigente(Desafio d) {
        Publicacao p = null;
        if(d != null){
            p = p_dao.get(d);
        }
        return null;
    }

    public void getPublicacaoAnterior(Desafio d) {

    }

    public void inserirPublicacoes(Desafio d, List<Publicacao> publicacoes) {
        for(Publicacao p : publicacoes){
            p.setDesafio(d);
            if(p_dao.inserePublicacao(p)){

            }
        }
    }

    public void inserirPublicacaoTeste(Desafio d) {
        Publicacao p = new Publicacao();
        p.setData_criacao(DataHelper.now());
        p.setData_inicio(DataHelper.now());
        p.setData_fim(DataHelper.now() + (DataHelper.milliMin() * 10));
        p.setDesafio(d);
        p_dao.inserePublicacao(p);
    }
}
