package santamaria.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the adm_roles database table.
 * 
 */
@Entity
@Table(name="adm_roles", schema="santamaria")
@NamedQuery(name="AdmRoles.findAll", query="SELECT a FROM AdmRoles a")
public class AdmRoles implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@TableGenerator(name="ROL_ID", schema="santamaria", table="contador", pkColumnName="cnt_nombre",valueColumnName="cnt_valor", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="ROL_ID")
	@Column(name="rol_id")
	private long rolId;

	@Temporal(TemporalType.DATE)
	@Column(name="fec_crea")
	private Date fecCrea;

	@Temporal(TemporalType.DATE)
	@Column(name="fec_modi")
	private Date fecModi;

	@Column(name="reg_activo")
	private BigDecimal regActivo;

	@Column(name="rol_descripcion")
	private String rolDescripcion;

	@Column(name="rol_nombre")
	private String rolNombre;

	@Column(name="usu_crea")
	private String usuCrea;

	@Column(name="usu_modi")
	private String usuModi;

	//bi-directional many-to-one association to AdmMenuRole
	@OneToMany(mappedBy="admRol")
	private List<AdmMenuRoles> admMenuRoles;

	//bi-directional many-to-one association to AdmRolesPermiso
	@OneToMany(mappedBy="admRol")
	private List<AdmRolesPermiso> admRolesPermisos;

	//bi-directional many-to-one association to AdmUsuariosPermiso
	@OneToMany(mappedBy="admRol")
	private List<AdmUsuariosPermiso> admUsuariosPermisos;

	public AdmRoles() {
	}

	public long getRolId() {
		return this.rolId;
	}

	public void setRolId(long rolId) {
		this.rolId = rolId;
	}

	public Date getFecCrea() {
		return this.fecCrea;
	}

	public void setFecCrea(Date fecCrea) {
		this.fecCrea = fecCrea;
	}

	public Date getFecModi() {
		return this.fecModi;
	}

	public void setFecModi(Date fecModi) {
		this.fecModi = fecModi;
	}

	public BigDecimal getRegActivo() {
		return this.regActivo;
	}

	public void setRegActivo(BigDecimal regActivo) {
		this.regActivo = regActivo;
	}

	public String getRolDescripcion() {
		return this.rolDescripcion;
	}

	public void setRolDescripcion(String rolDescripcion) {
		this.rolDescripcion = rolDescripcion;
	}

	public String getRolNombre() {
		return this.rolNombre;
	}

	public void setRolNombre(String rolNombre) {
		this.rolNombre = rolNombre;
	}

	public String getUsuCrea() {
		return this.usuCrea;
	}

	public void setUsuCrea(String usuCrea) {
		this.usuCrea = usuCrea;
	}

	public String getUsuModi() {
		return this.usuModi;
	}

	public void setUsuModi(String usuModi) {
		this.usuModi = usuModi;
	}

	public List<AdmMenuRoles> getAdmMenuRoles() {
		return this.admMenuRoles;
	}

	public void setAdmMenuRoles(List<AdmMenuRoles> admMenuRoles) {
		this.admMenuRoles = admMenuRoles;
	}

	public AdmMenuRoles addAdmMenuRole(AdmMenuRoles admMenuRol) {
		getAdmMenuRoles().add(admMenuRol);
		admMenuRol.setAdmRol(this);

		return admMenuRol;
	}

	public AdmMenuRoles removeAdmMenuRole(AdmMenuRoles admMenuRol) {
		getAdmMenuRoles().remove(admMenuRol);
		admMenuRol.setAdmRol(null);

		return admMenuRol;
	}

	public List<AdmRolesPermiso> getAdmRolesPermisos() {
		return this.admRolesPermisos;
	}

	public void setAdmRolesPermisos(List<AdmRolesPermiso> admRolesPermisos) {
		this.admRolesPermisos = admRolesPermisos;
	}

	public AdmRolesPermiso addAdmRolesPermiso(AdmRolesPermiso admRolesPermiso) {
		getAdmRolesPermisos().add(admRolesPermiso);
		admRolesPermiso.setAdmRol(this);

		return admRolesPermiso;
	}

	public AdmRolesPermiso removeAdmRolesPermiso(AdmRolesPermiso admRolesPermiso) {
		getAdmRolesPermisos().remove(admRolesPermiso);
		admRolesPermiso.setAdmRol(null);

		return admRolesPermiso;
	}

	public List<AdmUsuariosPermiso> getAdmUsuariosPermisos() {
		return this.admUsuariosPermisos;
	}

	public void setAdmUsuariosPermisos(List<AdmUsuariosPermiso> admUsuariosPermisos) {
		this.admUsuariosPermisos = admUsuariosPermisos;
	}

	public AdmUsuariosPermiso addAdmUsuariosPermiso(AdmUsuariosPermiso admUsuariosPermiso) {
		getAdmUsuariosPermisos().add(admUsuariosPermiso);
		admUsuariosPermiso.setAdmRol(this);

		return admUsuariosPermiso;
	}

	public AdmUsuariosPermiso removeAdmUsuariosPermiso(AdmUsuariosPermiso admUsuariosPermiso) {
		getAdmUsuariosPermisos().remove(admUsuariosPermiso);
		admUsuariosPermiso.setAdmRol(null);

		return admUsuariosPermiso;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (rolId ^ (rolId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdmRoles other = (AdmRoles) obj;
		if (rolId != other.rolId)
			return false;
		return true;
	}

}