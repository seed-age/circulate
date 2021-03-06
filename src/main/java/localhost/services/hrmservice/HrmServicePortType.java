package localhost.services.hrmservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.6.2
 * 2018-05-08T16:04:42.639+08:00
 * Generated source version: 2.6.2
 * 
 */
@WebService(targetNamespace = "http://localhost/services/HrmService", name = "HrmServicePortType")
@XmlSeeAlso({weaver.hrm.webservice.ObjectFactory.class, ObjectFactory.class})
public interface HrmServicePortType {

    @WebMethod(operationName = "SynHrmResource", action = "urn:weaver.hrm.webservice.HrmService.SynHrmResource")
    @RequestWrapper(localName = "SynHrmResource", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.SynHrmResource")
    @ResponseWrapper(localName = "SynHrmResourceResponse", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.SynHrmResourceResponse")
    @WebResult(name = "out", targetNamespace = "http://localhost/services/HrmService")
    public String synHrmResource(
            @WebParam(name = "in0", targetNamespace = "http://localhost/services/HrmService")
                    String in0,
            @WebParam(name = "in1", targetNamespace = "http://localhost/services/HrmService")
                    String in1
    );

    @WebMethod(action = "urn:weaver.hrm.webservice.HrmService.checkUser")
    @RequestWrapper(localName = "checkUser", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.CheckUser")
    @ResponseWrapper(localName = "checkUserResponse", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.CheckUserResponse")
    @WebResult(name = "out", targetNamespace = "http://localhost/services/HrmService")
    public boolean checkUser(
            @WebParam(name = "ipaddress", targetNamespace = "http://localhost/services/HrmService")
                    String ipaddress,
            @WebParam(name = "loginid", targetNamespace = "http://localhost/services/HrmService")
                    String loginid,
            @WebParam(name = "password", targetNamespace = "http://localhost/services/HrmService")
                    String password
    );

    @WebMethod(action = "urn:weaver.hrm.webservice.HrmService.getHrmDepartmentInfo")
    @RequestWrapper(localName = "getHrmDepartmentInfo", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.GetHrmDepartmentInfo")
    @ResponseWrapper(localName = "getHrmDepartmentInfoResponse", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.GetHrmDepartmentInfoResponse")
    @WebResult(name = "out", targetNamespace = "http://localhost/services/HrmService")
    public weaver.hrm.webservice.ArrayOfDepartmentBean getHrmDepartmentInfo(
            @WebParam(name = "ipaddress", targetNamespace = "http://localhost/services/HrmService")
                    String ipaddress,
            @WebParam(name = "subcompanyId", targetNamespace = "http://localhost/services/HrmService")
                    String subcompanyId
    );

    @WebMethod(action = "urn:weaver.hrm.webservice.HrmService.getHrmSubcompanyInfoXML")
    @RequestWrapper(localName = "getHrmSubcompanyInfoXML", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.GetHrmSubcompanyInfoXML")
    @ResponseWrapper(localName = "getHrmSubcompanyInfoXMLResponse", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.GetHrmSubcompanyInfoXMLResponse")
    @WebResult(name = "out", targetNamespace = "http://localhost/services/HrmService")
    public String getHrmSubcompanyInfoXML(
            @WebParam(name = "in0", targetNamespace = "http://localhost/services/HrmService")
                    String in0
    );

    @WebMethod(action = "urn:weaver.hrm.webservice.HrmService.getHrmUserInfoXML")
    @RequestWrapper(localName = "getHrmUserInfoXML", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.GetHrmUserInfoXML")
    @ResponseWrapper(localName = "getHrmUserInfoXMLResponse", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.GetHrmUserInfoXMLResponse")
    @WebResult(name = "out", targetNamespace = "http://localhost/services/HrmService")
    public String getHrmUserInfoXML(
            @WebParam(name = "in0", targetNamespace = "http://localhost/services/HrmService")
                    String in0,
            @WebParam(name = "in1", targetNamespace = "http://localhost/services/HrmService")
                    String in1,
            @WebParam(name = "in2", targetNamespace = "http://localhost/services/HrmService")
                    String in2,
            @WebParam(name = "in3", targetNamespace = "http://localhost/services/HrmService")
                    String in3,
            @WebParam(name = "in4", targetNamespace = "http://localhost/services/HrmService")
                    String in4,
            @WebParam(name = "in5", targetNamespace = "http://localhost/services/HrmService")
                    String in5
    );

    @WebMethod(operationName = "SynJobtitle", action = "urn:weaver.hrm.webservice.HrmService.SynJobtitle")
    @RequestWrapper(localName = "SynJobtitle", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.SynJobtitle")
    @ResponseWrapper(localName = "SynJobtitleResponse", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.SynJobtitleResponse")
    @WebResult(name = "out", targetNamespace = "http://localhost/services/HrmService")
    public String synJobtitle(
            @WebParam(name = "in0", targetNamespace = "http://localhost/services/HrmService")
                    String in0,
            @WebParam(name = "in1", targetNamespace = "http://localhost/services/HrmService")
                    String in1
    );

    @WebMethod(action = "urn:weaver.hrm.webservice.HrmService.getHrmDepartmentInfoXML")
    @RequestWrapper(localName = "getHrmDepartmentInfoXML", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.GetHrmDepartmentInfoXML")
    @ResponseWrapper(localName = "getHrmDepartmentInfoXMLResponse", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.GetHrmDepartmentInfoXMLResponse")
    @WebResult(name = "out", targetNamespace = "http://localhost/services/HrmService")
    public String getHrmDepartmentInfoXML(
            @WebParam(name = "in0", targetNamespace = "http://localhost/services/HrmService")
                    String in0,
            @WebParam(name = "in1", targetNamespace = "http://localhost/services/HrmService")
                    String in1
    );

    @WebMethod(action = "urn:weaver.hrm.webservice.HrmService.getHrmJobTitleInfoXML")
    @RequestWrapper(localName = "getHrmJobTitleInfoXML", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.GetHrmJobTitleInfoXML")
    @ResponseWrapper(localName = "getHrmJobTitleInfoXMLResponse", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.GetHrmJobTitleInfoXMLResponse")
    @WebResult(name = "out", targetNamespace = "http://localhost/services/HrmService")
    public String getHrmJobTitleInfoXML(
            @WebParam(name = "in0", targetNamespace = "http://localhost/services/HrmService")
                    String in0,
            @WebParam(name = "in1", targetNamespace = "http://localhost/services/HrmService")
                    String in1,
            @WebParam(name = "in2", targetNamespace = "http://localhost/services/HrmService")
                    String in2
    );

    @WebMethod(action = "urn:weaver.hrm.webservice.HrmService.changeUserPassword")
    @RequestWrapper(localName = "changeUserPassword", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.ChangeUserPassword")
    @ResponseWrapper(localName = "changeUserPasswordResponse", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.ChangeUserPasswordResponse")
    @WebResult(name = "out", targetNamespace = "http://localhost/services/HrmService")
    public boolean changeUserPassword(
            @WebParam(name = "in0", targetNamespace = "http://localhost/services/HrmService")
                    String in0,
            @WebParam(name = "in1", targetNamespace = "http://localhost/services/HrmService")
                    String in1,
            @WebParam(name = "in2", targetNamespace = "http://localhost/services/HrmService")
                    String in2
    );

    @WebMethod(operationName = "SynSubCompany", action = "urn:weaver.hrm.webservice.HrmService.SynSubCompany")
    @RequestWrapper(localName = "SynSubCompany", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.SynSubCompany")
    @ResponseWrapper(localName = "SynSubCompanyResponse", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.SynSubCompanyResponse")
    @WebResult(name = "out", targetNamespace = "http://localhost/services/HrmService")
    public String synSubCompany(
            @WebParam(name = "in0", targetNamespace = "http://localhost/services/HrmService")
                    String in0,
            @WebParam(name = "in1", targetNamespace = "http://localhost/services/HrmService")
                    String in1
    );

    @WebMethod(action = "urn:weaver.hrm.webservice.HrmService.getHrmJobTitleInfo")
    @RequestWrapper(localName = "getHrmJobTitleInfo", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.GetHrmJobTitleInfo")
    @ResponseWrapper(localName = "getHrmJobTitleInfoResponse", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.GetHrmJobTitleInfoResponse")
    @WebResult(name = "out", targetNamespace = "http://localhost/services/HrmService")
    public weaver.hrm.webservice.ArrayOfJobTitleBean getHrmJobTitleInfo(
            @WebParam(name = "ipaddress", targetNamespace = "http://localhost/services/HrmService")
                    String ipaddress,
            @WebParam(name = "subcompanyId", targetNamespace = "http://localhost/services/HrmService")
                    String subcompanyId,
            @WebParam(name = "departmentid", targetNamespace = "http://localhost/services/HrmService")
                    String departmentid
    );

    @WebMethod(action = "urn:weaver.hrm.webservice.HrmService.getHrmSubcompanyInfo")
    @RequestWrapper(localName = "getHrmSubcompanyInfo", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.GetHrmSubcompanyInfo")
    @ResponseWrapper(localName = "getHrmSubcompanyInfoResponse", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.GetHrmSubcompanyInfoResponse")
    @WebResult(name = "out", targetNamespace = "http://localhost/services/HrmService")
    public weaver.hrm.webservice.ArrayOfSubCompanyBean getHrmSubcompanyInfo(
            @WebParam(name = "ipaddress", targetNamespace = "http://localhost/services/HrmService")
                    String ipaddress
    );

    @WebMethod(action = "urn:weaver.hrm.webservice.HrmService.getHrmUserInfo")
    @RequestWrapper(localName = "getHrmUserInfo", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.GetHrmUserInfo")
    @ResponseWrapper(localName = "getHrmUserInfoResponse", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.GetHrmUserInfoResponse")
    @WebResult(name = "out", targetNamespace = "http://localhost/services/HrmService")
    public weaver.hrm.webservice.ArrayOfUserBean getHrmUserInfo(
            @WebParam(name = "ipaddress", targetNamespace = "http://localhost/services/HrmService")
                    String ipaddress,
            @WebParam(name = "loginid", targetNamespace = "http://localhost/services/HrmService")
                    String loginid,
            @WebParam(name = "subcompanyId", targetNamespace = "http://localhost/services/HrmService")
                    String subcompanyId,
            @WebParam(name = "departmentid", targetNamespace = "http://localhost/services/HrmService")
                    String departmentid,
            @WebParam(name = "jobtitleid", targetNamespace = "http://localhost/services/HrmService")
                    String jobtitleid,
            @WebParam(name = "lastChangeDate", targetNamespace = "http://localhost/services/HrmService")
                    String lastChangeDate
    );

    @WebMethod(operationName = "SynDepartment", action = "urn:weaver.hrm.webservice.HrmService.SynDepartment")
    @RequestWrapper(localName = "SynDepartment", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.SynDepartment")
    @ResponseWrapper(localName = "SynDepartmentResponse", targetNamespace = "http://localhost/services/HrmService", className = "localhost.services.hrmservice.SynDepartmentResponse")
    @WebResult(name = "out", targetNamespace = "http://localhost/services/HrmService")
    public String synDepartment(
            @WebParam(name = "in0", targetNamespace = "http://localhost/services/HrmService")
                    String in0,
            @WebParam(name = "in1", targetNamespace = "http://localhost/services/HrmService")
                    String in1
    );
}
