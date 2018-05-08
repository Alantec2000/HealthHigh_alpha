package google.com.healthhigh.dao;

import android.content.Context;

import google.com.healthhigh.domain.QuestaoOpinativa;
import google.com.healthhigh.domain.QuestaoOptativa;
import google.com.healthhigh.domain.RespostaOpinativa;
import google.com.healthhigh.domain.RespostaOptativa;
import google.com.healthhigh.domain.TipoQuestao;

/**
 * Created by Alan on 24/04/2018.
 */

public class TipoQuestaoDAO extends DAO {
    private AlternativaDAO a_dao;
    private RespostaAlternativaDAO ra_dao;
    private RespostaOptativaDAO ropt_dao;
    private RespostaOpinativaDAO ropn_dao;

    public TipoQuestaoDAO(Context context) {
        super(context);
        ra_dao = new RespostaAlternativaDAO(context);
        ropt_dao = new RespostaOptativaDAO(context);
        ropn_dao = new RespostaOpinativaDAO(context);
        a_dao = new AlternativaDAO(context);
    }

    @Override
    protected void prepareContentReceiver() {

    }

    public boolean inserirResposta(TipoQuestao qo){
        switch (qo.getTipoQuestao()){
            case QuestaoOpinativa.QUESTAO_OPINATIVA:
                ropn_dao.inserirResposta((RespostaOpinativa) qo.getResposta());
            break;
            case QuestaoOptativa.QUESTAO_OPTATIVA:
                ropt_dao.inserirResposta((RespostaOptativa) qo.getResposta());
            break;
        }

        return (qo != null && qo.getId() > 0);
    }

    public boolean atualizarResposta(TipoQuestao qo){
        boolean updated = false;
        switch (qo.getTipoQuestao()){
            case QuestaoOpinativa.QUESTAO_OPINATIVA:
                updated = ropn_dao.atualizarResposta((RespostaOpinativa) qo.getResposta());
                break;
            case QuestaoOptativa.QUESTAO_OPTATIVA:
                updated = ropt_dao.atualizarResposta((RespostaOptativa) qo.getResposta());
                break;
        }

        return updated;
    }
}
