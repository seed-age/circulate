package com.sunnsoft.sloa.db.dao;
import org.gteam.db.dao.BaseDAO;
import com.sunnsoft.sloa.db.vo.UserInfo;
// Generated by Hibernate Tools 3.4.0.CR1,modified by llade

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
/**
 * Home object for domain model class UserInfo.
 * @see com.sunnsoft.sloa.db.vo.UserInfo
 * @author Hibernate Tools
 * @author llade
 */
@SuppressWarnings("unchecked")
public class UserInfoDAOImpl extends BaseDAO implements UserInfoDAO {

    private static final Log log = LogFactory.getLog(UserInfoDAO.class);

	public UserInfo findById( Long id) {
        if(log.isDebugEnabled()){
        	log.debug("getting UserInfo instance with id: " + id);
        }
        try {
            UserInfo instance = (UserInfo) getHibernateTemplate()
                    .get("com.sunnsoft.sloa.db.vo.UserInfo", id);
            if(log.isDebugEnabled()){
	            if (instance==null) {
	                log.debug("get successful, no instance found");
	            }
	            else {
	                log.debug("get successful, instance found");
	            }
            }
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    public void save(UserInfo transientInstance) {
    	if(log.isDebugEnabled()){
        	log.debug("saving UserInfo instance");
        }
        try {
            getHibernateTemplate().save(transientInstance);
            if(log.isDebugEnabled()){
            	log.debug("save successful");
            }
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(UserInfo persistentInstance) {
		if(log.isDebugEnabled()){
        	log.debug("deleting UserInfo instance");
        }
        try {
            getHibernateTemplate().delete(persistentInstance);
            if(log.isDebugEnabled()){
            	log.debug("delete successful");
            }
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public List findByProperty(String propertyName, Object value) {
		if(log.isDebugEnabled()){
			log.debug("finding UserInfo instance with property: " + propertyName + ", value: " + value);
	    }
		try {
		    String queryString = "from UserInfo as model where model." 
		 						+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
}

