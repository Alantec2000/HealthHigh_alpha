package google.com.healthhigh.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import google.com.healthhigh.dao.AtividadeDAO;
import google.com.healthhigh.dao.DesafioDAO;
import google.com.healthhigh.dao.ExecucaoAtividadeDAO;
import google.com.healthhigh.dao.InteracaoAtividadeDAO;
import google.com.healthhigh.domain.Atividade;
import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.domain.Publicacao;

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
        if(d != null){
            atividades = a_dao.getAtividades(d);
            if(d.getPublicacao() != null) {
                InteracaoAtividadeController a_c = new InteracaoAtividadeController(context);
                a_c.getInteracaoAtividade(atividades, d.getPublicacao());
            }
        }
        return atividades;
    }

    // Função: obter as atividades, e todos os seus dados, do desafio atual
    // Obs: a função considera que você não sabe se a publicação está
    // vigente ou não ou se o desafio foi aceito ou não. O select vai
    // validar tudo isso.
    public Map<Long, Atividade> getAtividadesDesafioAtual(Desafio d){
        return a_dao.getAtividadesDesafioAtual(d);
    }

    public Map<Long, Atividade> getAtividadesDesafio(Desafio d){
        return a_dao.getAtividadesDesafioAtual(d);
    }


    public Desafio getDesafioAtual(Atividade a) {
        Desafio d = null;
        if(a != null){
            DesafioController d_c = new DesafioController(context);
            a.setDesafios_associados(d_c.getDesafiosAssociados(a));

            if(a.getDesafio_atual() == null) {
                d = d_c.getDesafioAtual();
                if(d != null && a.getDesafios_associados().containsKey(d.getId())) {
                    a.setDesafio_atual(d);
                }
            } else {
                d = a.getDesafio_atual();
            }

            Publicacao p = null;
            if(d != null && d.getPublicacao() == null) {
                p = d.getPublicacao();
                PublicacaoController p_c = new PublicacaoController(context);
                p = p_c.getPublicacaoVigente(d);
                if(p == null){
                    p_c.getPublicacaoAnterior(d);
                }
            }
        }
        return d;
    }
}
