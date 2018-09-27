package localhost.services.hrmservice;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class was generated by Apache CXF 2.6.2
 * 2018-05-08T16:04:42.657+08:00
 * Generated source version: 2.6.2
 * 
 */
@WebServiceClient(name = "HrmService", 
                  wsdlLocation = "https://oa.seedland.cc//services/HrmService?wsdl",
                  targetNamespace = "http://localhost/services/HrmService") 
public class HrmService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://localhost/services/HrmService", "HrmService");
    public final static QName HrmServiceHttpPort = new QName("http://localhost/services/HrmService", "HrmServiceHttpPort");
    static {
        URL url = null;
        try {
            url = new URL("https://oa.seedland.cc//services/HrmService?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(HrmService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "https://oa.seedland.cc//services/HrmService?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public HrmService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public HrmService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public HrmService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public HrmService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public HrmService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public HrmService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns HrmServicePortType
     */
    @WebEndpoint(name = "HrmServiceHttpPort")
    public HrmServicePortType getHrmServiceHttpPort() {
        return super.getPort(HrmServiceHttpPort, HrmServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns HrmServicePortType
     */
    @WebEndpoint(name = "HrmServiceHttpPort")
    public HrmServicePortType getHrmServiceHttpPort(WebServiceFeature... features) {
        return super.getPort(HrmServiceHttpPort, HrmServicePortType.class, features);
    }

}
