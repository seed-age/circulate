package com.sunnsoft.sloa.db.dao;
import org.gteam.db.dao.BaseDAO;
import com.sunnsoft.sloa.db.vo.Discuss;
// Generated by Hibernate Tools 3.4.0.CR1,modified by llade

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
/**
 * Home object for domain model class Discuss.
 * @see com.sunnsoft.sloa.db.vo.Discuss
 * @author Hibernate Tools
 * @author llade
 */
@SuppressWarnings("unchecked")
public class DiscussDAOImpl extends BaseDAO implements DiscussDAO {

    private static final Log log = LogFactory.getLog(DiscussDAO.class);

	public Discuss findById( Long id) {
        if(log.isDebugEnabled()){
        	log.debug("getting Discuss instance with id: " + id);
        }
        try {
            Discuss instance = (Discuss) getHibernateTemplate()
                    .get("com.sunnsoft.sloa.db.vo.Discuss", id);
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
    
    public void save(Discuss transientInstance) {
    	if(log.isDebugEnabled()){
        	log.debug("saving Discuss instance");
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
    
	public void delete(Discuss persistentInstance) {
		if(log.isDebugEnabled()){
        	log.debug("deleting Discuss instance");
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
			log.debug("finding Discuss instance with property: " + propertyName + ", value: " + value);
	    }
		try {
		    String queryString = "from Discuss as model where model." 
		 						+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
}

