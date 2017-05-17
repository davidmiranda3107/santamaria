package santamaria.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the adm_usuarios database table.
 * 
 */
@Entity
@Table(name="adm_usuarios", schema="santamaria")
@NamedQuery(name="AdmUsuario.findAll", query="SELECT a FROM AdmUsuario a")
public class AdmUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@TableGenerator(name="USU_ID", schema="santamaria", table="contador", pkColumnName="cnt_nombre",valueColumnName="cnt_valor", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="USU_ID")
	@Column(name="usu_id")
	private long usuId;

	@Temporal(TemporalType.DATE)
	@Column(name="fec_crea")
	private Date fecCrea;

	@Temporal(TemporalType.DATE)
	@Column(name="fec_modi")
	private Date fecModi;

	@Column(name="reg_activo")
	private BigDecimal regActivo;

	@Column(name="usu_clave")
	private String usuClave;

	@Column(name="usu_crea")
	private String usuCrea;

	@Column(name="usu_login")
	private String usuLogin;

	@Column(name="usu_modi")
	private String usuModi;

	@Column(name="usu_nombre")
	private String usuNombre;

	//bi-directional many-to-one association to AdmEmpresa
	@ManyToOne
	@JoinColumn(name="usu_emp_id")
	private AdmEmpresa admEmpresa;

	//bi-directional many-to-one association to AdmUsuariosPermiso
	@OneToMany(mappedBy="admUsuario")
	private List<AdmUsuariosPermiso> admUsuariosPermisos;

	public AdmUsuario() {
	}

	public long getUsuId() {
		return this.usuId;
	}

	public void setUsuId(long usuId) {
		this.usuId = usuId;
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

	public String getUsuClave() {
		return this.usuClave;
	}

	public void setUsuClave(String usuClave) {
		this.usuClave = usuClave;
	}

	public String getUsuCrea() {
		return this.usuCrea;
	}

	public void setUsuCrea(String usuCrea) {
		this.usuCrea = usuCrea;
	}

	public String getUsuLogin() {
		return this.usuLogin;
	}

	public void setUsuLogin(String usuLogin) {
		this.usuLogin = usuLogin;
	}

	public String getUsuModi() {
		return this.usuModi;
	}

	public void setUsuModi(String usuModi) {
		this.usuModi = usuModi;
	}

	public String getUsuNombre() {
		return this.usuNombre;
	}

	public void setUsuNombre(String usuNombre) {
		this.usuNombre = usuNombre;
	}

	public AdmEmpresa getAdmEmpresa() {
		return this.admEmpresa;
	}

	public void setAdmEmpresa(AdmEmpresa admEmpresa) {
		this.admEmpresa = admEmpresa;
	}

	public List<AdmUsuariosPermiso> getAdmUsuariosPermisos() {
		return this.admUsuariosPermisos;
	}

	public void setAdmUsuariosPermisos(List<AdmUsuariosPermiso> admUsuariosPermisos) {
		this.admUsuariosPermisos = admUsuariosPermisos;
	}

	public AdmUsuariosPermiso addAdmUsuariosPermiso(AdmUsuariosPermiso admUsuariosPermiso) {
		getAdmUsuariosPermisos().add(admUsuariosPermiso);
		admUsuariosPermiso.setAdmUsuario(this);

		return admUsuariosPermiso;
	}

	public AdmUsuariosPermiso removeAdmUsuariosPermiso(AdmUsuariosPermiso admUsuariosPermiso) {
		getAdmUsuariosPermisos().remove(admUsuariosPermiso);
		admUsuariosPermiso.setAdmUsuario(null);

		return admUsuariosPermiso;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (usuId ^ (usuId >>> 32));
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
		AdmUsuario other = (AdmUsuario) obj;
		if (usuId != other.usuId)
			return false;
		return true;
	}

}