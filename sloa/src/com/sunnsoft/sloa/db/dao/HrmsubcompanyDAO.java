package com.sunnsoft.sloa.db.dao;
import org.gteam.db.dao.IBaseDAO;
import com.sunnsoft.sloa.db.vo.Hrmsubcompany;
// Generated by Hibernate Tools 3.4.0.CR1,modified by llade


import java.util.List;
/**
 * Home object for domain model class Hrmsubcompany.
 * @see com.sunnsoft.sloa.db.vo.Hrmsubcompany
 * @author Hibernate Tools
 * @author llade
 */
@SuppressWarnings("unchecked")
public interface HrmsubcompanyDAO extends IBaseDAO {


	public abstract  Hrmsubcompany findById( java.lang.Integer id) ;
    
    public abstract  void save(Hrmsubcompany transientInstance) ;
    
	public abstract  void delete(Hrmsubcompany persistentInstance) ;
    
    public abstract  List findByProperty(String propertyName, Object value) ;

}
