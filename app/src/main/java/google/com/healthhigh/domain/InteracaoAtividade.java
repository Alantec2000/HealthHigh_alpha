package google.com.healthhigh.domain;

import java.util.Map;

public class InteracaoAtividade extends Interacao {
    private long data_conclusao, data_inicio;
    private Map<Long, SessaoAtividade> sessoes_atividade;
    private final Usuario usuario;

    public Atividade getAtividade(){
        return (Atividade) this.meta;
    }

    public void setAtividade(Atividade a){
        this.meta = a;
    }

    public Map<Long, SessaoAtividade> getSessoes_atividade() {
        return sessoes_atividade;
    }

    public long getData_inicio() {
        return data_inicio;
    }

    public void setData_inicio(long data_inicio) {
        this.data_inicio = data_inicio;
    }

    public void setSessoes_atividade(Map<Long, SessaoAtividade> sessoes_atividade) {
        this.sessoes_atividade = sessoes_atividade;
    }

    public InteracaoAtividade(Usuario usuario) {
        this.usuario = usuario;
    }

    public long getData_conclusao() {
        return data_conclusao;
    }

    public void setData_conclusao(long data_conclusao) {
        this.data_conclusao = data_conclusao;
    }

    public final static int NOVA = 1, PENDENTE = 2, INICIADA = 3, FINALIZADA = 4, SEM_PUBLICACAO = 5, NAO_CONCLUIDA = 6;
    private int status;

    public int getStatus(){
        atualizarStatus();
        return status;
    }

    public static String statusToText(int codigo){
        String status;
        switch (codigo){
            case NOVA:
                status = "Nova";
                break;
            case PENDENTE:
                status = "Pendente";
                break;
            case INICIADA:
                status = "Pendente";
                break;
            case FINALIZADA:
                status = "Finalizada";
                break;
            case SEM_PUBLICACAO:
                status = "Sem Publicação";
                break;
            case NAO_CONCLUIDA:
                status = "Não concluída";
                break;
            default:
                status = "Indefinido";
        }

        return status;
    }

    public String getStatusText(){
        String status;
        atualizarStatus();
        return statusToText(this.status);
    }

    public boolean isNova(){
        return getData_visualizacao() > 0;
    }

    public boolean isConcluida(){
        boolean resposta = false;
        Atividade a = (Atividade) getMeta();
        if(a != null){
            if(sessoes_atividade.size() > 0){
                int total_passos = 0;
                for (SessaoAtividade s_a : sessoes_atividade.values()) {
                    total_passos += s_a.getTotalPassosDados();
                }
                resposta = total_passos >= a.getTotal_passos_dados();
            }
        }
        return resposta;
    }

    public void atualizarStatus() {
        if(publicacao != null) {
            if(publicacao.isVigente()){
                if(isNova()){
                    status = NOVA;
                } else {
                    if(!isConcluida()){
                        if(data_inicio > 0){
                            status = INICIADA;
                        } else {
                            status = PENDENTE;
                        }
                    } else {
                        status = FINALIZADA;
                    }
                }
            } else {
                if(isConcluida()){
                    status = FINALIZADA;
                } else {
                    status = NAO_CONCLUIDA;
                }
            }
        } else {
            status = SEM_PUBLICACAO;
        }
    }
}
