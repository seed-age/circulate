
package localhost.services.hrmservice;

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
 *         &lt;element name="ipaddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="loginid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="subcompanyId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="departmentid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="jobtitleid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="lastChangeDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "ipaddress",
    "loginid",
    "subcompanyId",
    "departmentid",
    "jobtitleid",
    "lastChangeDate"
})
@XmlRootElement(name = "getHrmUserInfo")
public class GetHrmUserInfo {

    @XmlElement(required = true, nillable = true)
    protected String ipaddress;
    @XmlElement(required = true, nillable = true)
    protected String loginid;
    @XmlElement(required = true, nillable = true)
    protected String subcompanyId;
    @XmlElement(required = true, nillable = true)
    protected String departmentid;
    @XmlElement(required = true, nillable = true)
    protected String jobtitleid;
    @XmlElement(required = true, nillable = true)
    protected String lastChangeDate;

    /**
     * 获取ipaddress属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpaddress() {
        return ipaddress;
    }

    /**
     * 设置ipaddress属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpaddress(String value) {
        this.ipaddress = value;
    }

    /**
     * 获取loginid属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoginid() {
        return loginid;
    }

    /**
     * 设置loginid属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoginid(String value) {
        this.loginid = value;
    }

    /**
     * 获取subcompanyId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubcompanyId() {
        return subcompanyId;
    }

    /**
     * 设置subcompanyId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubcompanyId(String value) {
        this.subcompanyId = value;
    }

    /**
     * 获取departmentid属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepartmentid() {
        return departmentid;
    }

    /**
     * 设置departmentid属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepartmentid(String value) {
        this.departmentid = value;
    }

    /**
     * 获取jobtitleid属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobtitleid() {
        return jobtitleid;
    }

    /**
     * 设置jobtitleid属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobtitleid(String value) {
        this.jobtitleid = value;
    }

    /**
     * 获取lastChangeDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastChangeDate() {
        return lastChangeDate;
    }

    /**
     * 设置lastChangeDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastChangeDate(String value) {
        this.lastChangeDate = value;
    }

}
