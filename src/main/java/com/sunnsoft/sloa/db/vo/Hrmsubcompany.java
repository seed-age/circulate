package com.sunnsoft.sloa.db.vo;
// Generated by Hibernate Tools 3.4.0.CR1



/**
 * Hrmsubcompany generated by hbm2java
 */
public class Hrmsubcompany  implements java.io.Serializable {

	/**
	 *  serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	*Name:id
	*Comment:
	*/
     private int id;
	/**
	*Name:分部简称
	*Comment:
	*/
     private String subcompanyname;
	/**
	*Name:分部描述
	*Comment:
	*/
     private String subcompanydesc;
	/**
	*Name:所属总部ID
	*Comment:
	*/
     private Integer companyid;
	/**
	*Name:上级分部Id
	*Comment:
	*/
     private Integer supsubcomid;
	/**
	*Name:url
	*Comment:
	*/
     private String url;
	/**
	*Name:序列号
	*Comment:
	*/
     private Integer showorder;
	/**
	*Name:封存标识
	*Comment:
	*/
     private Character canceled;
	/**
	*Name:分部编码
	*Comment:
	*/
     private String subcompanycode;
	/**
	*Name:outkey
	*Comment:
	*/
     private String outkey;
	/**
	*Name:budgetatuomoveorder
	*Comment:
	*/
     private Integer budgetatuomoveorder;
	/**
	*Name:拼音
	*Comment:
	*/
     private String ecologyPinyinSearch;
	/**
	*Name:限制用户数
	*Comment:
	*/
     private Integer limitusers;
	/**
	*Name:等级
	*Comment:
	*/
     private Integer tlevel;

    public Hrmsubcompany() {
    }

	
    public Hrmsubcompany(int id) {
        this.id = id;
    }
    public Hrmsubcompany(int id, String subcompanyname, String subcompanydesc, Integer companyid, Integer supsubcomid, String url, Integer showorder, Character canceled, String subcompanycode, String outkey, Integer budgetatuomoveorder, String ecologyPinyinSearch, Integer limitusers, Integer tlevel) {
       this.id = id;
       this.subcompanyname = subcompanyname;
       this.subcompanydesc = subcompanydesc;
       this.companyid = companyid;
       this.supsubcomid = supsubcomid;
       this.url = url;
       this.showorder = showorder;
       this.canceled = canceled;
       this.subcompanycode = subcompanycode;
       this.outkey = outkey;
       this.budgetatuomoveorder = budgetatuomoveorder;
       this.ecologyPinyinSearch = ecologyPinyinSearch;
       this.limitusers = limitusers;
       this.tlevel = tlevel;
    }
   
	/**
	*Name:id
	*Comment:
	*/
    public int getId() {
        return this.id;
    }
    
	/**
	*Name:id
	*Comment:
	*/
    public void setId(int id) {
        this.id = id;
    }
	/**
	*Name:分部简称
	*Comment:
	*/
    public String getSubcompanyname() {
        return this.subcompanyname;
    }
    
	/**
	*Name:分部简称
	*Comment:
	*/
    public void setSubcompanyname(String subcompanyname) {
        this.subcompanyname = subcompanyname;
    }
	/**
	*Name:分部描述
	*Comment:
	*/
    public String getSubcompanydesc() {
        return this.subcompanydesc;
    }
    
	/**
	*Name:分部描述
	*Comment:
	*/
    public void setSubcompanydesc(String subcompanydesc) {
        this.subcompanydesc = subcompanydesc;
    }
	/**
	*Name:所属总部ID
	*Comment:
	*/
    public Integer getCompanyid() {
        return this.companyid;
    }
    
	/**
	*Name:所属总部ID
	*Comment:
	*/
    public void setCompanyid(Integer companyid) {
        this.companyid = companyid;
    }
	/**
	*Name:上级分部Id
	*Comment:
	*/
    public Integer getSupsubcomid() {
        return this.supsubcomid;
    }
    
	/**
	*Name:上级分部Id
	*Comment:
	*/
    public void setSupsubcomid(Integer supsubcomid) {
        this.supsubcomid = supsubcomid;
    }
	/**
	*Name:url
	*Comment:
	*/
    public String getUrl() {
        return this.url;
    }
    
	/**
	*Name:url
	*Comment:
	*/
    public void setUrl(String url) {
        this.url = url;
    }
	/**
	*Name:序列号
	*Comment:
	*/
    public Integer getShoworder() {
        return this.showorder;
    }
    
	/**
	*Name:序列号
	*Comment:
	*/
    public void setShoworder(Integer showorder) {
        this.showorder = showorder;
    }
	/**
	*Name:封存标识
	*Comment:
	*/
    public Character getCanceled() {
        return this.canceled;
    }
    
	/**
	*Name:封存标识
	*Comment:
	*/
    public void setCanceled(Character canceled) {
        this.canceled = canceled;
    }
	/**
	*Name:分部编码
	*Comment:
	*/
    public String getSubcompanycode() {
        return this.subcompanycode;
    }
    
	/**
	*Name:分部编码
	*Comment:
	*/
    public void setSubcompanycode(String subcompanycode) {
        this.subcompanycode = subcompanycode;
    }
	/**
	*Name:outkey
	*Comment:
	*/
    public String getOutkey() {
        return this.outkey;
    }
    
	/**
	*Name:outkey
	*Comment:
	*/
    public void setOutkey(String outkey) {
        this.outkey = outkey;
    }
	/**
	*Name:budgetatuomoveorder
	*Comment:
	*/
    public Integer getBudgetatuomoveorder() {
        return this.budgetatuomoveorder;
    }
    
	/**
	*Name:budgetatuomoveorder
	*Comment:
	*/
    public void setBudgetatuomoveorder(Integer budgetatuomoveorder) {
        this.budgetatuomoveorder = budgetatuomoveorder;
    }
	/**
	*Name:拼音
	*Comment:
	*/
    public String getEcologyPinyinSearch() {
        return this.ecologyPinyinSearch;
    }
    
	/**
	*Name:拼音
	*Comment:
	*/
    public void setEcologyPinyinSearch(String ecologyPinyinSearch) {
        this.ecologyPinyinSearch = ecologyPinyinSearch;
    }
	/**
	*Name:限制用户数
	*Comment:
	*/
    public Integer getLimitusers() {
        return this.limitusers;
    }
    
	/**
	*Name:限制用户数
	*Comment:
	*/
    public void setLimitusers(Integer limitusers) {
        this.limitusers = limitusers;
    }
	/**
	*Name:等级
	*Comment:
	*/
    public Integer getTlevel() {
        return this.tlevel;
    }
    
	/**
	*Name:等级
	*Comment:
	*/
    public void setTlevel(Integer tlevel) {
        this.tlevel = tlevel;
    }




}


