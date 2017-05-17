package santamaria.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the adm_empresa database table.
 * 
 */
@Entity
@Table(name="adm_empresa", schema="santamaria")
@NamedQuery(name="AdmEmpresa.findAll", query="SELECT a FROM AdmEmpresa a")
public class AdmEmpresa implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@TableGenerator(name="EMP_ID", schema="santamaria", table="contador", pkColumnName="cnt_nombre",valueColumnName="cnt_valor", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="EMP_ID")
	@Column(name="emp_id")
	private long empId;

	@Column(name="emp_descripcion")
	private String empDescripcion;

	@Column(name="emp_nombre")
	private String empNombre;

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
	@OneToMany(mappedBy="admEmpresa")
	private List<AdmMenu> admMenus;

	//bi-directional many-to-one association to AdmUsuario
	@OneToMany(mappedBy="admEmpresa")
	private List<AdmUsuario> admUsuarios;

	public AdmEmpresa() {
	}

	public long getEmpId() {
		return this.empId;
	}

	public void setEmpId(long empId) {
		this.empId = empId;
	}

	public String getEmpDescripcion() {
		return this.empDescripcion;
	}

	public void setEmpDescripcion(String empDescripcion) {
		this.empDescripcion = empDescripcion;
	}

	public String getEmpNombre() {
		return this.empNombre;
	}

	public void setEmpNombre(String empNombre) {
		this.empNombre = empNombre;
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

	public List<AdmMenu> getAdmMenus() {
		return this.admMenus;
	}

	public void setAdmMenus(List<AdmMenu> admMenus) {
		this.admMenus = admMenus;
	}

	public AdmMenu addAdmMenus(AdmMenu admMenus) {
		getAdmMenus().add(admMenus);
		admMenus.setAdmEmpresa(this);

		return admMenus;
	}

	public AdmMenu removeAdmMenus(AdmMenu admMenus) {
		getAdmMenus().remove(admMenus);
		admMenus.setAdmEmpresa(null);

		return admMenus;
	}

	public List<AdmUsuario> getAdmUsuarios() {
		return this.admUsuarios;
	}

	public void setAdmUsuarios(List<AdmUsuario> admUsuarios) {
		this.admUsuarios = admUsuarios;
	}

	public AdmUsuario addAdmUsuario(AdmUsuario admUsuario) {
		getAdmUsuarios().add(admUsuario);
		admUsuario.setAdmEmpresa(this);

		return admUsuario;
	}

	public AdmUsuario removeAdmUsuario(AdmUsuario admUsuario) {
		getAdmUsuarios().remove(admUsuario);
		admUsuario.setAdmEmpresa(null);

		return admUsuario;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (empId ^ (empId >>> 32));
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
		AdmEmpresa other = (AdmEmpresa) obj;
		if (empId != other.empId)
			return false;
		return true;
	}

}