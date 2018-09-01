package google.com.healthhigh.controller;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import google.com.healthhigh.dao.PremiacaoDAO;
import google.com.healthhigh.domain.Premiacao;

public class PremiacaoController {
    final private PremiacaoDAO p_dao;
    final private Context context;

    public PremiacaoController(Context context) {
        this.context = context;
        this.p_dao = new PremiacaoDAO(context);
    }

    public Set<Long> getIdsPremiacoes() {
        Set<Long> id_premiacoes = p_dao.getIdPremiacoes();
        return id_premiacoes;
    }

    public int inserirListaPremiacoes(List<Premiacao> premiacoes) {
        int result = 0;
        try {
            result = p_dao.inserirPremiacoes(premiacoes);
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public Map<Long, Premiacao> getPremiacoes() {
        return p_dao.getPremiacoes();
    }

    public List<Premiacao> getListaPremiacoes() {
        return new ArrayList<>(p_dao.getPremiacoes().values());
    }


    public boolean inserirPremiacao(Premiacao premiacao) {
        return p_dao.inserir(premiacao);
    }

    public Premiacao getPremiacao(long id) {
        return p_dao.getPremiacaoId(id);
    }
}
