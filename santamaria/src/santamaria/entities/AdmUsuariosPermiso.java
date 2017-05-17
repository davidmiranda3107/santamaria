package santamaria.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the adm_usuarios_permisos database table.
 * 
 */
@Entity
@Table(name="adm_usuarios_permisos", schema="santamaria")
@NamedQuery(name="AdmUsuariosPermiso.findAll", query="SELECT a FROM AdmUsuariosPermiso a")
public class AdmUsuariosPermiso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@TableGenerator(name="UPR_ID", schema="santamaria", table="contador", pkColumnName="cnt_nombre",valueColumnName="cnt_valor", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="UPR_ID")
	@Column(name="upr_id")
	private long uprId;

	@Temporal(TemporalType.DATE)
	@Column(name="fec_crea")
	private Date fecCrea;

	@Temporal(TemporalType.DATE)
	@Column(name="fec_modi")
	private Date fecModi;

	@Column(name="reg_activo")
	private BigDecimal regActivo;

	@Column(name="usu_crea")
	private String usuCrea;

	@Column(name="usu_modi")
	private String usuModi;

	//bi-directional many-to-one association to AdmPermiso
	@ManyToOne
	@JoinColumn(name="upr_per_id")
	private AdmPermiso admPermiso;

	//bi-directional many-to-one association to AdmRole
	@ManyToOne
	@JoinColumn(name="upr_rol_id")
	private AdmRoles admRol;

	//bi-directional many-to-one association to AdmSistema
	@ManyToOne
	@JoinColumn(name="upr_sis_id")
	private AdmSistema admSistema;

	//bi-directional many-to-one association to AdmUsuario
	@ManyToOne
	@JoinColumn(name="upr_usu_id")
	private AdmUsuario admUsuario;

	public AdmUsuariosPermiso() {
	}

	public long getUprId() {
		return this.uprId;
	}

	public void setUprId(long uprId) {
		this.uprId = uprId;
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

	public AdmPermiso getAdmPermiso() {
		return this.admPermiso;
	}

	public void setAdmPermiso(AdmPermiso admPermiso) {
		this.admPermiso = admPermiso;
	}

	public AdmRoles getAdmRol() {
		return this.admRol;
	}

	public void setAdmRol(AdmRoles admRol) {
		this.admRol = admRol;
	}

	public AdmSistema getAdmSistema() {
		return this.admSistema;
	}

	public void setAdmSistema(AdmSistema admSistema) {
		this.admSistema = admSistema;
	}

	public AdmUsuario getAdmUsuario() {
		return this.admUsuario;
	}

	public void setAdmUsuario(AdmUsuario admUsuario) {
		this.admUsuario = admUsuario;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (uprId ^ (uprId >>> 32));
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
		AdmUsuariosPermiso other = (AdmUsuariosPermiso) obj;
		if (uprId != other.uprId)
			return false;
		return true;
	}

}