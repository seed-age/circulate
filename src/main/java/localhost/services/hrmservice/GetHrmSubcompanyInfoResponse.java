
package localhost.services.hrmservice;

import weaver.hrm.webservice.ArrayOfSubCompanyBean;

import javax.xml.bind.annotation.*;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="out" type="{http://webservice.hrm.weaver}ArrayOfSubCompanyBean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "out"
})
@XmlRootElement(name = "getHrmSubcompanyInfoResponse")
public class GetHrmSubcompanyInfoResponse {

    @XmlElement(required = true, nillable = true)
    protected ArrayOfSubCompanyBean out;

    /**
     * 获取out属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSubCompanyBean }
     *     
     */
    public ArrayOfSubCompanyBean getOut() {
        return out;
    }

    /**
     * 设置out属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSubCompanyBean }
     *     
     */
    public void setOut(ArrayOfSubCompanyBean value) {
        this.out = value;
    }

}
