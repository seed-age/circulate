package com.sunnsoft.sloa.db.dao;
import org.gteam.db.dao.IBaseDAO;
import com.sunnsoft.sloa.db.vo.PersistentLogins;
// Generated by Hibernate Tools 3.4.0.CR1,modified by llade


import java.util.List;
/**
 * Home object for domain model class PersistentLogins.
 * @see com.sunnsoft.sloa.db.vo.PersistentLogins
 * @author Hibernate Tools
 * @author llade
 */
@SuppressWarnings("unchecked")
public interface PersistentLoginsDAO extends IBaseDAO {


	public abstract  PersistentLogins findById(String id) ;
    
    public abstract  void save(PersistentLogins transientInstance) ;
    
	public abstract  void delete(PersistentLogins persistentInstance) ;
    
    public abstract  List findByProperty(String propertyName, Object value) ;

}
