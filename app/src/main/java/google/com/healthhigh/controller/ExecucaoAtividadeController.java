package google.com.healthhigh.controller;

import android.content.ContentValues;
import android.content.Context;

import java.util.Map;

import google.com.healthhigh.dao.ExecucaoAtividadeDAO;
import google.com.healthhigh.domain.ExecucaoAtividade;
import google.com.healthhigh.domain.SessaoAtividade;

public class ExecucaoAtividadeController {
    final ExecucaoAtividadeDAO e_a_dao;
    final Context context;

    public ExecucaoAtividadeController(Context context) {
        this.context = context;
        this.e_a_dao = new ExecucaoAtividadeDAO(context);
    }

    public Map<Long, ExecucaoAtividade> getExecucao(SessaoAtividade s_a) {
        Map<Long, ExecucaoAtividade> atividades = e_a_dao.get(s_a);
        return atividades;
    }

    public void inserirNovaExecucao(ExecucaoAtividade execucao) throws Exception {
        if(execucao != null){
            if(execucao.getSessao_atividade() != null){
                if(execucao.getId() == 0) {
                    e_a_dao.insert(execucao);
                } else {
                    e_a_dao.update(execucao);
                }
            } else {
                // Mais pra frente eu faço um método para adicionar uma nova quando não tiver
                throw new Exception("Sessão da execução não informada!");
            }
        } else {
            // Mais pra frente eu faço um método para adicionar uma nova quando não tiver
            throw new Exception("Sessão da execução não informada!");
        }
    }
}
