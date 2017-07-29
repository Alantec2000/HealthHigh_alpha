package google.com.healthhigh.webservice;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class CallSoap {
    public final String SOAP_ACTION = "http://gestaosaudeumc.96.lt/pages/webservice/wsgestaosaudeserver.php";
    public  final String OPERATION_NAME = "wsgestaosaude.validaUsu";
    public  final String WSDL_TARGET_NAMESPACE = "http://gestaosaudeumc.96.it/pages/webservice/wsgestaosaude";
    public  final String SOAP_ADDRESS = "http://gestaosaudeumc.96.lt/pages/webservice/wsgestaosaudeserver.php";

    public CallSoap() {}

    public String Call(String a) {
        Object response=null;

        try {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("a_b");
        pi.setValue(a);
        pi.setType(Integer.class);
        request.addProperty(pi);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);

            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        } catch (Exception exception) {
            response=exception.toString();
        }
        return response.toString();
    }
}
