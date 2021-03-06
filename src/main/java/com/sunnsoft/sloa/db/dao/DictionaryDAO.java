package com.sunnsoft.sloa.db.dao;

import com.sunnsoft.sloa.db.vo.Dictionary;
import org.gteam.db.dao.IBaseDAO;

import java.util.List;

// Generated by Hibernate Tools 3.4.0.CR1,modified by llade
/**
 * Home object for domain model class Dictionary.
 * @see com.sunnsoft.sloa.db.vo.Dictionary
 * @author Hibernate Tools
 * @author llade
 */
@SuppressWarnings("unchecked")
public interface DictionaryDAO extends IBaseDAO {


	public abstract  Dictionary findById(Long id) ;
    
    public abstract  void save(Dictionary transientInstance) ;
    
	public abstract  void delete(Dictionary persistentInstance) ;
    
    public abstract  List findByProperty(String propertyName, Object value) ;

}
