package com.sunnsoft.sloa.db.dao;
import org.gteam.db.dao.IBaseDAO;
import com.sunnsoft.sloa.db.vo.SystemBackupLog;
// Generated by Hibernate Tools 3.4.0.CR1,modified by llade


import java.util.List;
/**
 * Home object for domain model class SystemBackupLog.
 * @see com.sunnsoft.sloa.db.vo.SystemBackupLog
 * @author Hibernate Tools
 * @author llade
 */
@SuppressWarnings("unchecked")
public interface SystemBackupLogDAO extends IBaseDAO {


	public abstract  SystemBackupLog findById( java.lang.Long id) ;
    
    public abstract  void save(SystemBackupLog transientInstance) ;
    
	public abstract  void delete(SystemBackupLog persistentInstance) ;
    
    public abstract  List findByProperty(String propertyName, Object value) ;

}
