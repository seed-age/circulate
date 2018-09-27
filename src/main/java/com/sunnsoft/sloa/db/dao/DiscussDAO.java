package com.sunnsoft.sloa.db.dao;
import org.gteam.db.dao.IBaseDAO;
import com.sunnsoft.sloa.db.vo.Discuss;
// Generated by Hibernate Tools 3.4.0.CR1,modified by llade


import java.util.List;
/**
 * Home object for domain model class Discuss.
 * @see com.sunnsoft.sloa.db.vo.Discuss
 * @author Hibernate Tools
 * @author llade
 */
@SuppressWarnings("unchecked")
public interface DiscussDAO extends IBaseDAO {


	public abstract  Discuss findById(Long id) ;
    
    public abstract  void save(Discuss transientInstance) ;
    
	public abstract  void delete(Discuss persistentInstance) ;
    
    public abstract  List findByProperty(String propertyName, Object value) ;

}