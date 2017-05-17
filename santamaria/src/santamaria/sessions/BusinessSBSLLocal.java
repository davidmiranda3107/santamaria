package santamaria.sessions;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

@Local
public interface BusinessSBSLLocal {

	public Object findByPk(Class<? extends Serializable> entity, Object pk) throws Exception;
	public List<?> findAll(Class<? extends Serializable> entity) throws Exception;;
	public Serializable update(Serializable registro) throws Exception;
	public Serializable insert(Serializable registro) throws Exception;
	public void delete(Serializable entity) throws Exception;
	public List<?> findByProperty(Class<? extends Serializable> entity, String property, Object value);
	public Object findByPropertyUnique(Class<? extends Serializable> entity, String property, Object value);
	public List<?> findByCondition(Class<? extends Serializable> entity, String where, String order);
	public List<?> findByJpql(Class<? extends Serializable> entity,String jpqlSentence);
	public List<?> findByJpql(Class<? extends Serializable> entity,String jpqlSentence, List<?> params);
	public List<?> findByNativeQuery(Class<? extends Serializable> entity,String query, Object[] params);
	public List<?> findByNativeQuery(String query, Object[] params);
	public List<?> findByNativeQuery(Class<? extends Serializable> entity,String query, Map<String, Object> param);
	public List<?> findByNativeQuery(String query, Map<String, Object> param);
	public int yaExiste(String tablas, String condicion);
	public List<?> findByNamedQuery(Class<? extends Serializable> entity,String name, Map<String, Object> param, boolean refresh);
	public List<?> findByNamedQuery(Class<? extends Serializable> entity, String name, Map<String,Object> param);
	public List<?> findByNamedQuery(Class<? extends Serializable> entity, String name, Object[] params, boolean refresh);
	public List<?> findByNamedQuery(Class<? extends Serializable> entity, String name, Object[] params);
	public Serializable findReference(Class<? extends Serializable> entity, Object pk);
	public boolean executeQuery(Class<? extends Serializable> entity, String query, Map<String,Object> params);
	public Serializable refresh(Class<? extends Serializable> entity, Object pk);
	public List<?> menuByUser(Class<? extends Serializable> entity,  long usuId, long sisId);
}
