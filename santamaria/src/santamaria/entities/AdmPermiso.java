package santamaria.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the adm_permisos database table.
 * 
 */
@Entity
@Table(name="adm_permisos", schema="santamaria")
@NamedQuery(name="AdmPermiso.findAll", query="SELECT a FROM AdmPermiso a")
public class AdmPermiso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@TableGenerator(name="PER_ID", schema="santamaria", table="contador", pkColumnName="cnt_nombre",valueColumnName="cnt_valor", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="PER_ID")
	@Column(name="per_id")
	private long perId;

	@Temporal(TemporalType.DATE)
	@Column(name="fec_crea")
	private Date fecCrea;

	@Temporal(TemporalType.DATE)
	@Column(name="fec_modi")
	private Date fecModi;

	@Column(name="per_descripcion")
	private String perDescripcion;

	@Column(name="per_nombre")
	private String perNombre;

	@Column(name="reg_activo")
	private BigDecimal regActivo;

	@Column(name="usu_crea")
	private String usuCrea;

	@Column(name="usu_modi")
	private String usuModi;

	//bi-directional many-to-one association to AdmRolesPermiso
	@OneToMany(mappedBy="admPermiso")
	private List<AdmRolesPermiso> admRolesPermisos;

	//bi-directional many-to-one association to AdmUsuariosPermiso
	@OneToMany(mappedBy="admPermiso")
	private List<AdmUsuariosPermiso> admUsuariosPermisos;

	public AdmPermiso() {
	}

	public long getPerId() {
		return this.perId;
	}

	public void setPerId(long perId) {
		this.perId = perId;
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

	public String getPerDescripcion() {
		return this.perDescripcion;
	}

	public void setPerDescripcion(String perDescripcion) {
		this.perDescripcion = perDescripcion;
	}

	public String getPerNombre() {
		return this.perNombre;
	}

	public void setPerNombre(String perNombre) {
		this.perNombre = perNombre;
	}

	public BigDecimal getRegActivo() {
		return this.regActivo;
	}

	public void setRegActivo(BigDecimal regActivo) {
		this.regActivo = regActivo;
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

	public List<AdmRolesPermiso> getAdmRolesPermisos() {
		return this.admRolesPermisos;
	}

	public void setAdmRolesPermisos(List<AdmRolesPermiso> admRolesPermisos) {
		this.admRolesPermisos = admRolesPermisos;
	}

	public AdmRolesPermiso addAdmRolesPermiso(AdmRolesPermiso admRolesPermiso) {
		getAdmRolesPermisos().add(admRolesPermiso);
		admRolesPermiso.setAdmPermiso(this);

		return admRolesPermiso;
	}

	public AdmRolesPermiso removeAdmRolesPermiso(AdmRolesPermiso admRolesPermiso) {
		getAdmRolesPermisos().remove(admRolesPermiso);
		admRolesPermiso.setAdmPermiso(null);

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
		admUsuariosPermiso.setAdmPermiso(this);

		return admUsuariosPermiso;
	}

	public AdmUsuariosPermiso removeAdmUsuariosPermiso(AdmUsuariosPermiso admUsuariosPermiso) {
		getAdmUsuariosPermisos().remove(admUsuariosPermiso);
		admUsuariosPermiso.setAdmPermiso(null);

		return admUsuariosPermiso;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (perId ^ (perId >>> 32));
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
		AdmPermiso other = (AdmPermiso) obj;
		if (perId != other.perId)
			return false;
		return true;
	}

}