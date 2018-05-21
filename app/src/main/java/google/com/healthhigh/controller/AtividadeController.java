package google.com.healthhigh.controller;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import google.com.healthhigh.dao.AtividadeDAO;
import google.com.healthhigh.dao.ExecucaoAtividadeDAO;
import google.com.healthhigh.dao.InteracaoAtividadeDAO;
import google.com.healthhigh.domain.Atividade;
import google.com.healthhigh.domain.Desafio;

public class AtividadeController {
    final AtividadeDAO a_dao;
    final InteracaoAtividadeDAO i_a_dao;
    final ExecucaoAtividadeDAO e_a_dao;
    final Context context;

    public AtividadeController(Context c) {
        this.context = c;
        this.a_dao = new AtividadeDAO(c);
        this.i_a_dao = new InteracaoAtividadeDAO(c);
        this.e_a_dao = new ExecucaoAtividadeDAO(c);
    }

    // Função: obter uma lista de atividades a partir de um desafio
    public Map<Long, Atividade> getAtividades(Desafio d){
        Map<Long, Atividade> atividades = new TreeMap<>();
        if(d.getPublicacao() != null && d.getPublicacao().isVigente()){
            atividades = a_dao.getAtividades(d);
        } else {
            atividades = null;
        }
        return atividades;
    }
}
