package com.sunnsoft.sloa.db.dao;
import org.gteam.db.dao.IBaseDAO;
import com.sunnsoft.sloa.db.vo.UpdateRecord;
// Generated by Hibernate Tools 3.4.0.CR1,modified by llade


import java.util.List;
/**
 * Home object for domain model class UpdateRecord.
 * @see com.sunnsoft.sloa.db.vo.UpdateRecord
 * @author Hibernate Tools
 * @author llade
 */
@SuppressWarnings("unchecked")
public interface UpdateRecordDAO extends IBaseDAO {


	public abstract  UpdateRecord findById(Long id) ;
    
    public abstract  void save(UpdateRecord transientInstance) ;
    
	public abstract  void delete(UpdateRecord persistentInstance) ;
    
    public abstract  List findByProperty(String propertyName, Object value) ;

}
