package com.sunnsoft.sloa.db.dao;

import com.sunnsoft.sloa.db.vo.Mail;
import org.gteam.db.dao.IBaseDAO;

import java.util.List;

// Generated by Hibernate Tools 3.4.0.CR1,modified by llade
/**
 * Home object for domain model class Mail.
 * @see com.sunnsoft.sloa.db.vo.Mail
 * @author Hibernate Tools
 * @author llade
 */
@SuppressWarnings("unchecked")
public interface MailDAO extends IBaseDAO {


	public abstract  Mail findById(Long id) ;
    
    public abstract  void save(Mail transientInstance) ;
    
	public abstract  void delete(Mail persistentInstance) ;
    
    public abstract  List findByProperty(String propertyName, Object value) ;

}
