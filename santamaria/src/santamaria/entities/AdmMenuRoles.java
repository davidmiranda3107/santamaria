package santamaria.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the adm_menu_roles database table.
 * 
 */
@Entity
@Table(name="adm_menu_roles", schema="santamaria")
@NamedQuery(name="AdmMenuRoles.findAll", query="SELECT a FROM AdmMenuRoles a")
public class AdmMenuRoles implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@TableGenerator(name="MNR_ID", schema="santamaria", table="contador", pkColumnName="cnt_nombre",valueColumnName="cnt_valor", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="MNR_ID")
	@Column(name="mnr_id")
	private long mnrId;

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

	//bi-directional many-to-one association to AdmMenu
	@ManyToOne
	@JoinColumn(name="mnr_mnu_id")
	private AdmMenu admMenu;

	//bi-directional many-to-one association to AdmRole
	@ManyToOne
	@JoinColumn(name="mnr_rol_id")
	private AdmRoles admRol;

	public AdmMenuRoles() {
	}

	public long getMnrId() {
		return this.mnrId;
	}

	public void setMnrId(long mnrId) {
		this.mnrId = mnrId;
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

	public AdmMenu getAdmMenu() {
		return this.admMenu;
	}

	public void setAdmMenu(AdmMenu admMenu) {
		this.admMenu = admMenu;
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
		result = prime * result + (int) (mnrId ^ (mnrId >>> 32));
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
		AdmMenuRoles other = (AdmMenuRoles) obj;
		if (mnrId != other.mnrId)
			return false;
		return true;
	}

}