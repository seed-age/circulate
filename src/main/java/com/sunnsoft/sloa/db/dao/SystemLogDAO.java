package com.sunnsoft.sloa.db.dao;
import org.gteam.db.dao.IBaseDAO;
import com.sunnsoft.sloa.db.vo.SystemLog;
// Generated by Hibernate Tools 3.4.0.CR1,modified by llade


import java.util.List;
/**
 * Home object for domain model class SystemLog.
 * @see com.sunnsoft.sloa.db.vo.SystemLog
 * @author Hibernate Tools
 * @author llade
 */
@SuppressWarnings("unchecked")
public interface SystemLogDAO extends IBaseDAO {


	public abstract  SystemLog findById(Long id) ;
    
    public abstract  void save(SystemLog transientInstance) ;
    
	public abstract  void delete(SystemLog persistentInstance) ;
    
    public abstract  List findByProperty(String propertyName, Object value) ;

}
