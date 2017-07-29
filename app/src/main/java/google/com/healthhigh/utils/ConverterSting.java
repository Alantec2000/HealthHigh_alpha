package google.com.healthhigh.utils;

/**
 * Created by Alexandre on 27/05/2017.
 */

public abstract class ConverterSting {

    /*esta classe abstrata é para tratar o retorno do web Service, pois vou usar em todos os retornos
    como o método é abstrato posso chamar ele em qualquer classe:
    ConvererString.substring(O começo que é 13, e o retorno que é rslt);*/

    public static String substring(int beginIndex, String retorno){

        return retorno.substring(beginIndex);
    }
}
