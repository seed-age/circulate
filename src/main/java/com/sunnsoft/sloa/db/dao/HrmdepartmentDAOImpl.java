package com.sunnsoft.sloa.db.dao;

import com.sunnsoft.sloa.db.vo.Hrmdepartment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gteam.db.dao.BaseDAO;

import java.util.List;

// Generated by Hibernate Tools 3.4.0.CR1,modified by llade
/**
 * Home object for domain model class Hrmdepartment.
 * @see com.sunnsoft.sloa.db.vo.Hrmdepartment
 * @author Hibernate Tools
 * @author llade
 */
@SuppressWarnings("unchecked")
public class HrmdepartmentDAOImpl extends BaseDAO implements HrmdepartmentDAO {

    private static final Log log = LogFactory.getLog(HrmdepartmentDAO.class);

	public Hrmdepartment findById( Integer id) {
        if(log.isDebugEnabled()){
        	log.debug("getting Hrmdepartment instance with id: " + id);
        }
        try {
            Hrmdepartment instance = (Hrmdepartment) getHibernateTemplate()
                    .get("com.sunnsoft.sloa.db.vo.Hrmdepartment", id);
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
    
    public void save(Hrmdepartment transientInstance) {
    	if(log.isDebugEnabled()){
        	log.debug("saving Hrmdepartment instance");
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
    
	public void delete(Hrmdepartment persistentInstance) {
		if(log.isDebugEnabled()){
        	log.debug("deleting Hrmdepartment instance");
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
			log.debug("finding Hrmdepartment instance with property: " + propertyName + ", value: " + value);
	    }
		try {
		    String queryString = "from Hrmdepartment as model where model." 
		 						+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
}

