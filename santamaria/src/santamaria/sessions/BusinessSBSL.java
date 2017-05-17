package santamaria.sessions;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceUnit;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import santamaria.util.parameters.JpqlParam;
import santamaria.util.parameters.JpqlParamExt;

@Stateless
@LocalBean
public class BusinessSBSL implements BusinessSBSLLocal {

	@PersistenceUnit
	protected EntityManagerFactory emf;
	protected EntityManager em;
	
	public static final long TRANSACTION_INSERT = 1L;
	public static final long TRANSACTION_UPDATE = 2L;
	public static final long TRANSACTION_DELETE = 3L;
    
    public BusinessSBSL() {
    	
    }
    
    @Override
	public Object findByPk(Class<? extends Serializable> entity, Object pk)
			throws Exception {
		Object registro = null;
		try {
			em = emf.createEntityManager();
			registro = em.find(entity, pk);
			// em.refresh(registro);
			// em.flush();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return registro;
	}
    
    @Override
	public List<?> findAll(Class<? extends Serializable> entity)
			throws Exception {
		List<?> registros = null;
		try {
			em = emf.createEntityManager();
			CriteriaBuilder qb = em.getCriteriaBuilder();
			CriteriaQuery<?> cq = qb.createQuery(entity);
			Query q = em.createQuery(cq);
			q.setHint("javax.persistence.cache.storeMode", "REFRESH");
			registros = q.getResultList();
			// em.flush();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return registros;
	}
    
    @SuppressWarnings("unused")
	@Override
	public Serializable update(Serializable registro) throws Exception {
		long pk = 0;
		EntityTransaction tx = null;
		try {
			em = emf.createEntityManager();
			PersistenceUnitUtil util = emf.getPersistenceUnitUtil();
			pk = (Long) util.getIdentifier(registro);
			tx = em.getTransaction();
			tx.begin();
			registro = em.merge(registro);
			em.flush();
			tx.commit();
		} catch (Exception e) {			
			if (tx != null)
				tx.rollback();
			throw new Exception(e);
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return registro;
	}

	@SuppressWarnings("unused")
	@Override
	public Serializable insert(Serializable registro) throws Exception {
		long pk = 0;
		EntityTransaction tx = null;
		try {
			em = emf.createEntityManager();
			PersistenceUnitUtil util = emf.getPersistenceUnitUtil();
			tx = em.getTransaction();
			tx.begin();
			em.persist(registro);
			em.flush();
			tx.commit();
			pk = (Long) util.getIdentifier(registro);
		} catch (Exception e) {			
			if (tx != null)
				tx.rollback();
			throw new Exception(e);
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return registro;
	}

	@Override
	public void delete(Serializable registro) throws Exception {
		long pk = 0;
		EntityTransaction tx = null;
		try {
			em = emf.createEntityManager();
			tx = em.getTransaction();
			tx.begin();
			PersistenceUnitUtil util = emf.getPersistenceUnitUtil();
			pk = (Long) util.getIdentifier(registro);
			registro = em.find(registro.getClass(), pk);
			em.remove(registro);
			em.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw new Exception(e);
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}		
	}

	@Override
	public List<?> findByProperty(Class<? extends Serializable> entity,
			String property, Object value) {
		List<?> lst = null;
		try {
			em = emf.createEntityManager();
			CriteriaBuilder qb = em.getCriteriaBuilder();
			CriteriaQuery<?> cq = qb.createQuery(entity);
			Root<?> registro = cq.from(entity);
			cq.where(qb.equal(registro.get(property), value));
			Query q = em.createQuery(cq);
			q.setHint("javax.persistence.cache.storeMode", "REFRESH");
			lst = q.getResultList();
			// em.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return lst;
	}

	@Override
	public Object findByPropertyUnique(Class<? extends Serializable> entity,
			String property, Object value) {
		Object registros = null;
		try {
			em = emf.createEntityManager();
			CriteriaBuilder qb = em.getCriteriaBuilder();
			CriteriaQuery<?> cq = qb.createQuery(entity);
			Root<?> registro = cq.from(entity);
			cq.where(qb.equal(registro.get(property), value));
			Query q = em.createQuery(cq);
			q.setHint("javax.persistence.cache.storeMode", "REFRESH");
			registros = q.getSingleResult();
			// //em.flush();
		} catch (NoResultException e) {
			// e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return registros;
	}
	
	@Override
	public List<?> findByCondition(Class<? extends Serializable> entity,
			String where, String order) {
		em = emf.createEntityManager();
		List<?> lista = null;
		try {
			String sql = "select object(o) from " + entity.getSimpleName()
					+ " as o";
			if ((where != null) && (where.length() > 0)) {
				sql += " where " + where;
			}

			if ((order != null) && (order.length() > 0)) {
				sql += " order by " + order;
			}
			Query q = em.createQuery(sql);
			q.setHint("javax.persistence.cache.storeMode", "REFRESH");
			lista = q.getResultList();
			// em.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return lista;
	}

	@Override
	public List<?> findByJpql(Class<? extends Serializable> entity,
			String jpqlSentence) {
		em = emf.createEntityManager();
		List<?> lista = null;
		try {
			Query q = em.createQuery(jpqlSentence);
			q.setHint("javax.persistence.cache.storeMode", "REFRESH");
			lista = q.getResultList();
			// em.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return lista;
	}

	@Override
	public List<?> findByJpql(Class<? extends Serializable> entity,
			String jpqlSentence, List<?> params) {
		em = emf.createEntityManager();
		List<?> lista = null;
		try {
			Query q = em.createQuery(jpqlSentence);
			if (params.size() > 0) {
				for (Object param : params) {
					if (param.getClass().getSimpleName().equals("JpqlParam")) {
						q.setParameter(((JpqlParam) param).getName(),
								((JpqlParam) param).getValue());
					} else if (param.getClass().getSimpleName()
							.equals("JpqlParamExt")) {
						if (((JpqlParamExt) param).getValue().getClass()
								.getSimpleName().equals("Date")) {
							q.setParameter(((JpqlParamExt) param).getName(),
									(Date) ((JpqlParamExt) param).getValue(),
									((JpqlParamExt) param).getTemporal());
						} else if (((JpqlParamExt) param).getValue().getClass()
								.getSimpleName().equals("Calendar")) {
							q.setParameter(((JpqlParamExt) param).getName(),
									(Calendar) ((JpqlParamExt) param)
											.getValue(), ((JpqlParamExt) param)
											.getTemporal());
						}
					}
				}
			}
			q.setHint("javax.persistence.cache.storeMode", "REFRESH");
			lista = q.getResultList();
			// em.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return lista;
	}
	
	@Override
	public List<?> findByNativeQuery(Class<? extends Serializable> entity,
			String query, Object[] params) {
		em = emf.createEntityManager();
		List<?> lista = null;
		try {
			Query q = em.createNativeQuery(query, entity);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					q.setParameter(i + 1, params[i]);
				}
			}
			lista = q.getResultList();
			// em.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return lista;
	}

	@Override
	public List<?> findByNativeQuery(String query, Object[] params) {
		em = emf.createEntityManager();
		List<?> lista = null;
		try {
			Query q = em.createNativeQuery(query);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					q.setParameter(i + 1, params[i]);
				}
			}
			lista = q.getResultList();
			// em.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return lista;
	}

	@Override
	public List<?> findByNativeQuery(Class<? extends Serializable> entity,
			String query, Map<String, Object> param) {
		em = emf.createEntityManager();
		List<?> lista = null;
		try {
			Query q = em.createNativeQuery(query, entity);
			if (param != null && param.size() > 0) {
				for (Entry<String, Object> e : param.entrySet()) {
					q.setParameter(e.getKey(), e.getValue());
				}
			}
			lista = q.getResultList();
			// em.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return lista;
	}

	@Override
	public List<?> findByNativeQuery(String query, Map<String, Object> param) {
		em = emf.createEntityManager();
		List<?> lista = null;
		try {
			Query q = em.createNativeQuery(query);
			if (param != null && param.size() > 0) {
				for (Entry<String, Object> e : param.entrySet()) {
					q.setParameter(e.getKey(), e.getValue());
				}
			}
			lista = q.getResultList();
			// em.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return lista;
	}
	
	@Override
	public int yaExiste(String tablas, String condicion) {
		int count = 0;
		try {
			em = emf.createEntityManager();
			String sql = "select count(1) from " + tablas;
			if (condicion != null)
				sql += " where " + condicion;
			Query q = em.createNativeQuery(sql);
			//System.out.println("sql ----> "+sql);
			count = Integer.parseInt(q.getSingleResult().toString());
			// em.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return count;
	}
	
	@Override
	public List<?> findByNamedQuery(Class<? extends Serializable> entity,
			String name, Map<String, Object> param, boolean refresh) {
		List<?> lista = null;
		try {
			em = emf.createEntityManager();
			Query q = em.createNamedQuery(name, entity);
			if (param != null && param.size() > 0) {
				for (Entry<String, Object> e : param.entrySet()) {
					q.setParameter(e.getKey(), e.getValue());
				}
			}
			if (refresh) {
				q.setHint("javax.persistence.cache.storeMode", "REFRESH");
			}
			lista = q.getResultList();
			// em.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return lista;
	}

	@Override
	public List<?> findByNamedQuery(Class<? extends Serializable> entity,
			String name, Map<String, Object> param) {
		return findByNamedQuery(entity, name, param, true);
	}

	@Override
	public List<?> findByNamedQuery(Class<? extends Serializable> entity,
			String name, Object[] params, boolean refresh) {
		List<?> lista = null;
		try {
			em = emf.createEntityManager();
			Query q = em.createNamedQuery(name, entity);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					q.setParameter(i + 1, params[i]);
				}
			}
			if (refresh) {
				q.setHint("javax.persistence.cache.storeMode", "REFRESH");
			}
			lista = q.getResultList();
			// em.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return lista;
	}

	@Override
	public List<?> findByNamedQuery(Class<? extends Serializable> entity,
			String name, Object[] params) {
		return findByNamedQuery(entity, name, params, true);
	}
	
	@Override
	public Serializable findReference(Class<? extends Serializable> entity,
			Object pk) {
		Serializable registro = null;
		try {
			em = emf.createEntityManager();
			registro = em.getReference(entity, pk);
			// em.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return registro;
	}
	
	public Serializable refresh(Class<? extends Serializable> entity, Object pk) {
		Serializable object = null;
		if (pk != null) {
			em = emf.createEntityManager();
			object = (Serializable) em.find(entity, pk);
			//object.setSelected(true);
			em.refresh(object);
			em.clear();
			em.close();
		}
		return object;
	}
	
	@Override
	public boolean executeQuery(Class<? extends Serializable> entity,
			String query, Map<String, Object> params) {
		boolean resultado = false;
		try {
			em = emf.createEntityManager();
			Query qry = em.createQuery(query);
			if (params != null && params.size() > 0) {
				for (Entry<String, Object> e : params.entrySet()) {
					qry.setParameter(e.getKey(), e.getValue());
				}
			}
			qry.executeUpdate();
			// em.flush();
			resultado = true;
		} catch (Exception e) {
			e.printStackTrace();
			resultado = false;
		} finally {
			if (em != null) {
				em.clear();
				em.close();
			}
		}
		return resultado;
	}
	
	@Override
	public List<?> menuByUser(Class<? extends Serializable> entity, long usuId,
			long sisId) {
		em = emf.createEntityManager();
		try {
			// Query q =
			// em.createQuery("select object(o) from GimAdmMenu as o where o.mnuPadre = :mnuPadre and o.mnuActivo = 'A'  order by o.mnuPadre, o.mnuOrden");
			Query q = em
					.createNativeQuery(
							"select a.* from santamaria.adm_menu a "
									+ "where exists ( select 1 from santamaria.adm_menu_roles b, santamaria.adm_usuarios_permisos c "
									+ "where a.mnu_id = b.mnr_mnu_id and b.mnr_rol_id = c.upr_rol_id and b.reg_activo = 1 "
									+ "and c.reg_activo = 1 and c.upr_usu_id = ? and c.upr_sis_id = a.mnu_sis_id) "
									+ "and a.mnu_sis_id = ? and a.reg_activo = 1 order by a.mnu_id_padre, a.mnu_orden",
							entity);
			q.setHint("javax.persistence.cache.storeMode", "REFRESH");
			q.setParameter(1, usuId);
			q.setParameter(2, sisId);
			List<?> lista = q.getResultList();
			return lista;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}
}
