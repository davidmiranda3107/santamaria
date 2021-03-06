package santamaria.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the adm_roles_permisos database table.
 * 
 */
@Entity
@Table(name="adm_roles_permisos", schema="santamaria")
@NamedQuery(name="AdmRolesPermiso.findAll", query="SELECT a FROM AdmRolesPermiso a")
public class AdmRolesPermiso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@TableGenerator(name="RLP_ID", schema="santamaria", table="contador", pkColumnName="cnt_nombre",valueColumnName="cnt_valor", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="RLP_ID")
	@Column(name="rlp_id")
	private long rlpId;

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
	@JoinColumn(name="rlp_per_id")
	private AdmPermiso admPermiso;

	//bi-directional many-to-one association to AdmRole
	@ManyToOne
	@JoinColumn(name="rlp_rol_id")
	private AdmRoles admRol;

	public AdmRolesPermiso() {
	}

	public long getRlpId() {
		return this.rlpId;
	}

	public void setRlpId(long rlpId) {
		this.rlpId = rlpId;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (rlpId ^ (rlpId >>> 32));
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
		AdmRolesPermiso other = (AdmRolesPermiso) obj;
		if (rlpId != other.rlpId)
			return false;
		return true;
	}

}