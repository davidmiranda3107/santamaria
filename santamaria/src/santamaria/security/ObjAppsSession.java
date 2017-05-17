package santamaria.security;

import java.math.BigDecimal;
import java.util.List;

import santamaria.entities.AdmSistema;
import santamaria.entities.AdmUsuario;
import santamaria.entities.AdmUsuariosPermiso;

public class ObjAppsSession {

	private AdmUsuario usuario;
	private AdmSistema sistema;
	private List<AdmUsuariosPermiso> permisos;

	public boolean isPermisoValido(String sistema, String rol) {
		boolean valido = false;
		if (permisos != null) {
			for (AdmUsuariosPermiso reg : permisos) {
				String comparar = sistema.trim().toUpperCase() + ":" + rol.trim().toUpperCase() + ":";
				String registro = reg.getAdmSistema().getSisNombre().trim().toUpperCase() + ":"
						+ reg.getAdmRol().getRolNombre().trim().toUpperCase() + ":";
				if ((registro.equals(comparar)) && (reg.getRegActivo().equals(BigDecimal.ONE))) {
					return true;
				}
			}
		}
		return valido;
	}

	public boolean isPermisoValido(String sistema, String rol, String permiso) {
		boolean valido = false;
		if(permisos != null) {
			for (AdmUsuariosPermiso reg : this.permisos) {
				String comparar = sistema.trim().toUpperCase() + ":"
						+ rol.trim().toUpperCase() + ":"
						+ permiso.trim().toUpperCase() + ":";
				String registro = reg.getAdmSistema().getSisNombre().trim()
						.toUpperCase()
						+ ":"
						+ reg.getAdmRol().getRolNombre().trim().toUpperCase()
						+ ":"
						+ reg.getAdmPermiso().getPerNombre().trim().toUpperCase()
						+ ":";
				if ((registro.equals(comparar))
						&& (reg.getRegActivo().equals(BigDecimal.ONE))) {
					return true;
				}
			}
		}
		return valido;
	}

	public AdmUsuario getUsuario() {
		return usuario;
	}

	public void setUsuario(AdmUsuario usuario) {
		this.usuario = usuario;
	}

	public AdmSistema getSistema() {
		return sistema;
	}

	public void setSistema(AdmSistema sistema) {
		this.sistema = sistema;
	}

	public List<AdmUsuariosPermiso> getPermisos() {
		return permisos;
	}

	public void setPermisos(List<AdmUsuariosPermiso> permisos) {
		this.permisos = permisos;
	}
	
}
