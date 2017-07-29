package google.com.healthhigh.webservice;


import google.com.healthhigh.activities.LoginActivity;

public class Caller  extends Thread
{
    public CallSoap cs;
    public String a;

    public void run(){
        try{
            cs=new CallSoap();
            String resp=cs.Call(a);
            LoginActivity.rslt=resp;
        }catch(Exception ex)
        {LoginActivity.rslt=ex.toString();}
    }
}