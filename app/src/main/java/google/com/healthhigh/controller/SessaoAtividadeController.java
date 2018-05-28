package google.com.healthhigh.controller;

import android.content.Context;

import java.util.Map;
import java.util.TreeMap;

import google.com.healthhigh.dao.ExecucaoAtividadeDAO;
import google.com.healthhigh.dao.SessaoAtividadeDAO;
import google.com.healthhigh.domain.Atividade;
import google.com.healthhigh.domain.ExecucaoAtividade;
import google.com.healthhigh.domain.InteracaoAtividade;
import google.com.healthhigh.domain.Publicacao;
import google.com.healthhigh.domain.SessaoAtividade;
import google.com.healthhigh.utils.DataHelper;

public class SessaoAtividadeController {
    private final SessaoAtividadeDAO s_a_dao;
    private final Context context;

    public SessaoAtividadeController(Context context) {
        this.s_a_dao = new SessaoAtividadeDAO(context);
        this.context = context;
    }

    public SessaoAtividade getSessaoAberta(Atividade a, boolean criar_nova) throws Exception {
        SessaoAtividade s_a;

        InteracaoAtividadeController i_a_c = new InteracaoAtividadeController(context);
        InteracaoAtividade i_a = i_a_c.getInteracaoAtividade(a);
        s_a = s_a_dao.getSessaoAberta(i_a);

        //se não tiver uma sessão aberta, criar uma nova sessão
        if(s_a == null && criar_nova) {
            s_a = iniciarNovaSessao(i_a);
        }

        // Se ainda assim não foi possível criar a sessão, então, retornar um erro
        if(s_a != null){
            if(i_a != null){
                s_a.setInteracao_atividade(i_a);
            }
            ExecucaoAtividadeController e_a_c = new ExecucaoAtividadeController(context);
            Map<Long, ExecucaoAtividade> atividades = e_a_c.getExecucao(s_a);
            s_a.setAtividades(atividades);
        } else if(criar_nova){
            throw new Exception("Erro ao criar um nova sessão!");
        }
        return s_a;
    }

    private SessaoAtividade iniciarNovaSessao(InteracaoAtividade i_a) {
        SessaoAtividade s_a = new SessaoAtividade();
        s_a.setData_inicio(DataHelper.now());
        if(i_a != null){
            s_a.setInteracao_atividade(i_a);
        }
        s_a_dao.put(s_a);
        return s_a;
    }

    public void adicionarExecucao(ExecucaoAtividade execucao) {
        ExecucaoAtividadeController e_a_c = new ExecucaoAtividadeController(context);
        try {
            e_a_c.inserirNovaExecucao(execucao);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void encerrarSessao(SessaoAtividade sessao) {
        sessao.setData_fim(DataHelper.now());
        if(sessao.getId() == 0){
            s_a_dao.put(sessao);
        } else {
            if(!s_a_dao.update(sessao)){
                sessao.setData_fim(0);
            }
        }
    }
}
