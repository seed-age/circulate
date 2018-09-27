package com.sunnsoft.sloa.db.dao;
import org.gteam.db.dao.IBaseDAO;
import com.sunnsoft.sloa.db.vo.Tag;
// Generated by Hibernate Tools 3.4.0.CR1,modified by llade


import java.util.List;
/**
 * Home object for domain model class Tag.
 * @see com.sunnsoft.sloa.db.vo.Tag
 * @author Hibernate Tools
 * @author llade
 */
@SuppressWarnings("unchecked")
public interface TagDAO extends IBaseDAO {


	public abstract  Tag findById(Long id) ;
    
    public abstract  void save(Tag transientInstance) ;
    
	public abstract  void delete(Tag persistentInstance) ;
    
    public abstract  List findByProperty(String propertyName, Object value) ;

}
