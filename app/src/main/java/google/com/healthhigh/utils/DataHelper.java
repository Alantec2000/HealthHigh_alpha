package google.com.healthhigh.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/*
* Classe utilitária, contendo métodos auxiliáres para manipulação de datas;
* SimpleDateFormat lêtras:
* d - Dia do mês com padding de 2 números; Ex.: 01, 02, ... , 09, 10....
* M - Mês do ano com padding de 2 números: Ex.: 01, 02, ... , 09, 10....
* y - Ano de quatro dígitos
* H - Hora do dia no padrão/formato militar e vai de 00 à 23 horas;
* m - Minuto em uma hora;
* s - segundos em uma hora
* Ex.: "dd/MM/yy HH:mm:ss"
* */
public abstract class DataHelper {

    public static String parseUT(long tempo, String format){
        Date d = new Date((tempo)*1000);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(d);
    }

    public static long now(){
        return System.currentTimeMillis();
    }

    public static Calendar dateToCalendar(String time){
        Date d = new Date(Long.parseLong(time));
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c;
    }
}
