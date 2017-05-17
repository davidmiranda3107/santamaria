package santamaria.sessions;

import java.math.BigDecimal;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import santamaria.entities.AdmUsuario;
import santamaria.util.Utils;

@Stateless
@LocalBean
public class ValidacionSBSL implements ValidacionSBSLLocal {

	@EJB(mappedName = "ejb/BusinessSBSL")
	BusinessSBSLLocal businessLocal;

	private static final String CLAVE_ESTANDAR = "abc123";

	public static final Integer VAL_ERROR_NO_DETERMINADO = -1;
	public static final Integer VAL_USUARIO_NO_EXISTE = 0;
	public static final Integer VAL_USUARIO_VALIDO = 1;
	public static final Integer VAL_USUARIO_DEBE_CAMBIAR_CLAVE = 2;
	public static final Integer VAL_ERROR_EN_CLAVE = 3;
	public static final Integer VAL_USUARIO_EXPIRADO = 4;
	public static final Integer VAL_USUARIO_DE_BAJA = 5;
	public static final Integer VAL_USUARIO_NO_TIENE_ACCESO_A_SISTEMA = 6;

	public ValidacionSBSL() {

	}

	@Override
	public Integer validar(String usuario, String clave, String sistema) {
		Integer resp = VAL_USUARIO_NO_EXISTE;
		try {
			resp = validar(usuario, clave);
			//System.out.println("resp "+resp);
			if (resp == VAL_USUARIO_VALIDO) {
				String tabla = " santamaria.adm_usuarios,santamaria.adm_usuarios_permisos ";
				String condicion = " adm_usuarios.usu_login = '" + usuario + "' "
						+ " AND adm_usuarios.usu_id = adm_usuarios_permisos.upr_usu_id "
						+ " AND adm_usuarios_permisos.upr_sis_id = " + sistema
						+ " AND adm_usuarios_permisos.upr_rol_id = 1 " + " AND adm_usuarios_permisos.upr_per_id = 1 "
						+ " AND adm_usuarios_permisos.reg_activo = 1 ";
				//System.out.println("condicion ----> " +condicion);
				if (businessLocal.yaExiste(tabla, condicion) != 1L) {
					// USUARIO NO TIENE ACCESO A SISTEMA
					resp = VAL_USUARIO_NO_TIENE_ACCESO_A_SISTEMA;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	@Override
	public Integer validar(String usuario, String clave) {
		Integer resp = VAL_USUARIO_NO_EXISTE;
		String claveMD5 = "";
		try {
			AdmUsuario admUsuario = (AdmUsuario) businessLocal
					.findByPropertyUnique(AdmUsuario.class, "usuLogin",
							usuario);
			admUsuario = (AdmUsuario) businessLocal.refresh(
					AdmUsuario.class, admUsuario.getUsuId());
			// valida que el usuario exista en base de datos
			if (admUsuario != null) {
				// valida que el usuario este activo
				if (admUsuario.getRegActivo().equals(BigDecimal.ONE)) {
					// valida que la clave sea correcta
					claveMD5 = Utils.getMD5String(usuario + clave);
					if (admUsuario.getUsuClave().equals(claveMD5)) {
						if (isClaveEstandar(usuario, claveMD5)) {
							// CLAVE ESTANDAR (CAMBIARLA)
							resp = VAL_USUARIO_DEBE_CAMBIAR_CLAVE;
						} else {
							// valida que la usuario/clave no haya expirado
							if (!isExpirado(admUsuario)) {
								// USUARIO VALIDO
								resp = VAL_USUARIO_VALIDO;
							} else {
								// USUARIO EXPIRADO
								resp = VAL_USUARIO_EXPIRADO;
							}
						}
					} else {
						// CLAVE EQUIVOCADA
						resp = VAL_ERROR_EN_CLAVE;
					}
				} else {
					// USUARIO INACTIVO O DE BAJA
					resp = VAL_USUARIO_DE_BAJA;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	private boolean isClaveEstandar(String usuario, String clave) {
		boolean estandar = false;
		if (clave.equals(Utils.getMD5String(usuario + CLAVE_ESTANDAR))) {
			estandar = true;
		}
		return estandar;
	}
	
	@SuppressWarnings("unused")
	private boolean isExpirado(AdmUsuario usuario) {
		boolean expirado = false;
		/*try {
			// valida si USU_EXPIRADO = 1, usuario esta expirado
			if (usuario.getUsuExpirado().equals(BigDecimal.ONE)) {
				expirado = true;
			} else {
				// valida si USU_FCH_CAMBIO_CLAVE no es null
				if (usuario.getUsuFchCambioClave() != null) {
					// si la fecha actual es mayor a USU_FCH_CAMBIO_CLAVE esta
					// expirada
					expirado = (usuario.getUsuFchCambioClave().compareTo(
							new Date()) < 0);

					if (expirado) {
						// expira el usuario en la base de datos
						usuario.setUsuExpirado(BigDecimal.ONE);
						businessLocal.update(usuario);
					}
				} else {
					// si USU_FCH_CAMBIO_CLAVE es null, no caduda clave
					expirado = false;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return expirado;*/
		return false;
	}

	@Override
	public Integer cambiarClave(String usuario, String clave, String nuevaClave) {
		// resp = 22 : CLAVE ES IGUAL A ANTERIOR
		Integer resp = 22;
		if (!clave.equals(nuevaClave)) {
			Integer valUsuario = validar(usuario, clave);
			// 1 = USUARIO VALIDO, 2 = CLAVE ESTANDAR, 4 = USUARIO EXPIRADO
			if (valUsuario == 1 || valUsuario == 2 || valUsuario == 4) {
				Integer valClave = validarClave(nuevaClave);
				// valClave = 20 : CLAVE CAMBIADA CORRECTAMENTE
				if (valClave == 20) {
					if (actualizarClave(usuario, nuevaClave)) {
						resp = 20;
					} else {
						resp = 32;
					}
				} else {
					resp = valClave;
				}
			} else {
				resp = 21;
			}
		}
		return resp;
	}
	
	private Integer validarClave(String clave) {
		Integer resp = 23;
		boolean may = false, min = false, num = false, blanco = false;
		if (clave.length() >= 8) {
			char[] arrayClave = clave.toCharArray();
			for (int c = 0; c < arrayClave.length; c++) {
				if (Character.isDigit(arrayClave[c])) {
					num = true;
				}
				if (Character.isLowerCase(arrayClave[c])) {
					min = true;
				}
				if (Character.isUpperCase(arrayClave[c])) {
					may = true;
				}
				if (Character.isSpaceChar(arrayClave[c])
						|| Character.isWhitespace(arrayClave[c])) {
					blanco = true;
				}
			}
			if (blanco) {
				resp = 30;
			} else {
				if (num && min && may) {
					resp = 20;
				} else {
					if (!may && !min && !num) {
						resp = 31;
					} else if (!may && min && !num) {
						resp = 29;
					} else if (may && !min && !num) {
						resp = 28;
					} else if (!may && !min && num) {
						resp = 27;
					} else if (may && min && !num) {
						resp = 26;
					} else if (!may && min && num) {
						resp = 25;
					} else if (may && !min && num) {
						resp = 24;
					}
				}
			}
		}
		return resp;
	}
	
	private boolean actualizarClave(String usuario, String nuevaClave) {
		boolean resp = false;
		try {
			AdmUsuario admUsuario = (AdmUsuario) businessLocal
					.findByPropertyUnique(AdmUsuario.class, "usuIdentidad",
							usuario);
			if (admUsuario != null) {
				//Date hoy = Utils.getHoy();
				// Clave expira en 6 meses de acuerdo a normativa
				//Date sigCambioClave = DateUtils.addMonths(hoy, 6);
				admUsuario.setUsuClave(Utils
						.getMD5String(usuario + nuevaClave));
				//appsUsuario.setUsuFchCambioClave(sigCambioClave);
				//appsUsuario.setUsuExpirado(BigDecimal.ZERO);
				if (this.businessLocal.update(admUsuario) != null) {
					resp = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return resp;
	}
	
	@Override
	public Integer resetearClave(String usuario) {
		Integer resp = 32;
		try {
			AdmUsuario user = (AdmUsuario) businessLocal
					.findByPropertyUnique(AdmUsuario.class, "usuLogin",
							usuario);
			if (user != null) {
				// se asigna clave estandar
				user.setUsuClave(Utils.getMD5String(user.getUsuLogin() + CLAVE_ESTANDAR));
				// se expira el usuario
				//user.setUsuExpirado(BigDecimal.ONE);
				// se expira en el momento y que se cambie de inmediato
				//user.setUsuFchCambioClave(new Date(System.currentTimeMillis()));
				if (this.businessLocal.update(user) != null) {
					resp = 20;
				}
			} else {
				resp = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	@Override
	public Integer generarClave(String usuario, long sisId) {
		Integer resp = 32;
		try {
			AdmUsuario user = (AdmUsuario) businessLocal
					.findByPropertyUnique(AdmUsuario.class, "usuLogin",
							usuario);
			if (user != null) {
				/*if (user.getUsuCorreo() != null) {
					AdmSistema sistema = (AdmSistema) businessLocal
							.findByPk(AdmSistema.class, sisId);
					String passwd = Utils.getPassword(8);
					String correo = user.getUsuCorreo();
					// se asigna clave aleatoria
					user.setUsuClave(Utils.getMD5String(usuario + passwd));
					// se expira el usuario
					user.setUsuExpirado(BigDecimal.ONE);
					// se expira en el momento y que se cambie de inmediato
					user.setUsuFchCambioClave(Utils.getHoy());
					if (this.businessLocal.update(user) != null) {
						if (notificaClave(correo, passwd, sistema) == AppsSendMail.STATUS_SEND) {
							resp = 20;
						} else {
							resp = 34;
						}
					}
				} else {
					resp = 33;
				}*/
			} else {
				resp = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
}
