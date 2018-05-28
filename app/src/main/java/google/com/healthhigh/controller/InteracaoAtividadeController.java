package google.com.healthhigh.controller;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import google.com.healthhigh.dao.InteracaoAtividadeDAO;
import google.com.healthhigh.domain.Atividade;
import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.domain.InteracaoAtividade;
import google.com.healthhigh.domain.Publicacao;
import google.com.healthhigh.utils.DataHelper;

public class InteracaoAtividadeController {
    final InteracaoAtividadeDAO i_a_dao;
    final Context context;

    public InteracaoAtividadeController(Context context) {
        this.context = context;
        i_a_dao = new InteracaoAtividadeDAO(context);
    }

    public InteracaoAtividade getInteracaoAtividade(Atividade a) {
        InteracaoAtividade i_a = null;
        if(a != null){
            AtividadeController a_c = new AtividadeController(context);
            Desafio d = a_c.getDesafioAtual(a);
            if(d != null && d.getPublicacao() != null) {
                i_a = i_a_dao.get(a, d.getPublicacao());
                if(i_a == null){
                    i_a = inserirNovaInteracao(a, d.getPublicacao());
                }
            }
        }
        return i_a;
    }

    private InteracaoAtividade inserirNovaInteracao(Atividade a, Publicacao p) {
        InteracaoAtividade i_a = null;
        if((a != null && a.getId() > 0) && (p != null && p.getId() > 0)){
            i_a = new InteracaoAtividade(null);
            i_a.setPublicacao(p);
            i_a.setAtividade(a);
            i_a.setData_criacao(DataHelper.now());
            try {
                i_a_dao.inserir(i_a);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i_a;
    }

    public void getInteracaoAtividade(Map<Long, Atividade> atividades, Publicacao p) {
        if((atividades != null && atividades.size() > 0) && (p != null && p.getId() > 0)){
            i_a_dao.getInteracaoAtividade(atividades, p);
        }
    }

    public boolean finalizarInteracaoAtividade(InteracaoAtividade i_a) throws Exception {
        boolean conclusao = false;
        if(i_a != null && (i_a.getIdPublicacao() > 0 && i_a.getIdMeta() > 0)){
            i_a.setData_conclusao(DataHelper.now());
            if(!i_a_dao.atualizar(i_a)){
                throw new Exception("Erro ao finalizar atividade!");
            }
        }
        return conclusao;
    }
}