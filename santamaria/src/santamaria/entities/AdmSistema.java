package santamaria.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the adm_sistema database table.
 * 
 */
@Entity
@Table(name="adm_sistema", schema="santamaria")
@NamedQuery(name="AdmSistema.findAll", query="SELECT a FROM AdmSistema a")
public class AdmSistema implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@TableGenerator(name="SIS_ID", schema="santamaria", table="contador", pkColumnName="cnt_nombre",valueColumnName="cnt_valor", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="SIS_ID")
	@Column(name="sis_id")
	private long sisId;

	@Temporal(TemporalType.DATE)
	@Column(name="fec_crea")
	private Date fecCrea;

	@Temporal(TemporalType.DATE)
	@Column(name="fec_modi")
	private Date fecModi;

	@Column(name="reg_activo")
	private BigDecimal regActivo;

	@Column(name="sis_descripcion")
	private String sisDescripcion;

	@Column(name="sis_nombre")
	private String sisNombre;

	@Column(name="usu_crea")
	private String usuCrea;

	@Column(name="usu_modi")
	private String usuModi;

	//bi-directional many-to-one association to AdmMenu
	@OneToMany(mappedBy="admSistema")
	private List<AdmMenu> admMenus;

	//bi-directional many-to-one association to AdmUsuariosPermiso
	@OneToMany(mappedBy="admSistema")
	private List<AdmUsuariosPermiso> admUsuariosPermisos;

	public AdmSistema() {
	}

	public long getSisId() {
		return this.sisId;
	}

	public void setSisId(long sisId) {
		this.sisId = sisId;
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

	public String getSisDescripcion() {
		return this.sisDescripcion;
	}

	public void setSisDescripcion(String sisDescripcion) {
		this.sisDescripcion = sisDescripcion;
	}

	public String getSisNombre() {
		return this.sisNombre;
	}

	public void setSisNombre(String sisNombre) {
		this.sisNombre = sisNombre;
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

	public List<AdmMenu> getAdmMenus() {
		return this.admMenus;
	}

	public void setAdmMenus(List<AdmMenu> admMenus) {
		this.admMenus = admMenus;
	}

	public AdmMenu addAdmMenus(AdmMenu admMenus) {
		getAdmMenus().add(admMenus);
		admMenus.setAdmSistema(this);

		return admMenus;
	}

	public AdmMenu removeAdmMenus(AdmMenu admMenus) {
		getAdmMenus().remove(admMenus);
		admMenus.setAdmSistema(null);

		return admMenus;
	}

	public List<AdmUsuariosPermiso> getAdmUsuariosPermisos() {
		return this.admUsuariosPermisos;
	}

	public void setAdmUsuariosPermisos(List<AdmUsuariosPermiso> admUsuariosPermisos) {
		this.admUsuariosPermisos = admUsuariosPermisos;
	}

	public AdmUsuariosPermiso addAdmUsuariosPermiso(AdmUsuariosPermiso admUsuariosPermiso) {
		getAdmUsuariosPermisos().add(admUsuariosPermiso);
		admUsuariosPermiso.setAdmSistema(this);

		return admUsuariosPermiso;
	}

	public AdmUsuariosPermiso removeAdmUsuariosPermiso(AdmUsuariosPermiso admUsuariosPermiso) {
		getAdmUsuariosPermisos().remove(admUsuariosPermiso);
		admUsuariosPermiso.setAdmSistema(null);

		return admUsuariosPermiso;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (sisId ^ (sisId >>> 32));
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
		AdmSistema other = (AdmSistema) obj;
		if (sisId != other.sisId)
			return false;
		return true;
	}

}