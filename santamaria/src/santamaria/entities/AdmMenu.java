package santamaria.entities;

import java.io.Serializable;
import javax.persistence.*;

import santamaria.interfaces.IAdmMenu;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the adm_menu database table.
 * 
 */
@Entity
@Table(name="adm_menu", schema="santamaria")
@NamedQuery(name="AdmMenu.findAll", query="SELECT a FROM AdmMenu a")
public class AdmMenu implements IAdmMenu, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@TableGenerator(name="MNU_ID", schema="santamaria", table="contador", pkColumnName="cnt_nombre",valueColumnName="cnt_valor", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="MNU_ID")
	@Column(name="mnu_id")
	private long mnuId;

	@Temporal(TemporalType.DATE)
	@Column(name="fec_crea")
	private Date fecCrea;

	@Temporal(TemporalType.DATE)
	@Column(name="fec_modi")
	private Date fecModi;

	@Column(name="mnu_id_padre")
	private BigDecimal mnuIdPadre;

	@Column(name="mnu_nombre")
	private String mnuNombre;

	@Column(name="mnu_orden")
	private BigDecimal mnuOrden;

	@Column(name="mnu_tipo")
	private String mnuTipo;

	@Column(name="mnu_url")
	private String mnuUrl;

	@Column(name="reg_activo")
	private BigDecimal regActivo;

	@Column(name="usu_crea")
	private String usuCrea;

	@Column(name="usu_modi")
	private String usuModi;

	//bi-directional many-to-one association to AdmEmpresa
	@ManyToOne
	@JoinColumn(name="mnu_emp_id")
	private AdmEmpresa admEmpresa;

	//bi-directional many-to-one association to AdmSistema
	@ManyToOne
	@JoinColumn(name="mnu_sis_id")
	private AdmSistema admSistema;

	//bi-directional many-to-one association to AdmMenuRole
	@OneToMany(mappedBy="admMenu")
	private List<AdmMenuRoles> admMenuRoles;

	public AdmMenu() {
	}

	public long getMnuId() {
		return this.mnuId;
	}

	public void setMnuId(long mnuId) {
		this.mnuId = mnuId;
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

	public BigDecimal getMnuIdPadre() {
		return this.mnuIdPadre;
	}

	public void setMnuIdPadre(BigDecimal mnuIdPadre) {
		this.mnuIdPadre = mnuIdPadre;
	}

	public String getMnuNombre() {
		return this.mnuNombre;
	}

	public void setMnuNombre(String mnuNombre) {
		this.mnuNombre = mnuNombre;
	}

	public BigDecimal getMnuOrden() {
		return this.mnuOrden;
	}

	public void setMnuOrden(BigDecimal mnuOrden) {
		this.mnuOrden = mnuOrden;
	}

	public String getMnuTipo() {
		return this.mnuTipo;
	}

	public void setMnuTipo(String mnuTipo) {
		this.mnuTipo = mnuTipo;
	}

	public String getMnuUrl() {
		return this.mnuUrl;
	}

	public void setMnuUrl(String mnuUrl) {
		this.mnuUrl = mnuUrl;
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

	public AdmEmpresa getAdmEmpresa() {
		return this.admEmpresa;
	}

	public void setAdmEmpresa(AdmEmpresa admEmpresa) {
		this.admEmpresa = admEmpresa;
	}

	public AdmSistema getAdmSistema() {
		return this.admSistema;
	}

	public void setAdmSistema(AdmSistema admSistema) {
		this.admSistema = admSistema;
	}

	public List<AdmMenuRoles> getAdmMenuRoles() {
		return this.admMenuRoles;
	}

	public void setAdmMenuRoles(List<AdmMenuRoles> admMenuRoles) {
		this.admMenuRoles = admMenuRoles;
	}

	public AdmMenuRoles addAdmMenuRole(AdmMenuRoles admMenuRol) {
		getAdmMenuRoles().add(admMenuRol);
		admMenuRol.setAdmMenu(this);

		return admMenuRol;
	}

	public AdmMenuRoles removeAdmMenuRole(AdmMenuRoles admMenuRol) {
		getAdmMenuRoles().remove(admMenuRol);
		admMenuRol.setAdmMenu(null);

		return admMenuRol;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (mnuId ^ (mnuId >>> 32));
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
		AdmMenu other = (AdmMenu) obj;
		if (mnuId != other.mnuId)
			return false;
		return true;
	}

}