package santamaria.security;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.primefaces.component.panelmenu.PanelMenu;
import org.primefaces.context.RequestContext;

import santamaria.controller.SuperController;
import santamaria.entities.AdmMenu;
import santamaria.entities.AdmSistema;
import santamaria.entities.AdmUsuario;
import santamaria.entities.AdmUsuariosPermiso;
import santamaria.interfaces.IAdmMenu;
import santamaria.sessions.ValidacionSBSLLocal;
import santamaria.util.AppConstant;
import santamaria.util.SystemConfig;

public class SecurityController extends SuperController {

	@EJB(mappedName = "ejb/ValidacionSBSL")
	protected ValidacionSBSLLocal validacionLocal;

	private PanelMenu menu;
	private String usuario;
	private String clave;
	private String claveNueva;
	private String claveConfirmada;
	private long sisId;
	private boolean loggedIn = false;
	private String changePassOutcome;
	private Date fechaSistema;
	private boolean validarSistema = true;
	private String nit;
	private String dui;
	private boolean runLogout = true;

	private Integer msgCodigo = 0;// guarda la respuesta del login

	public SecurityController() {
		super();
		TimeZone tzSv = TimeZone.getTimeZone("America/El_Salvador");
		fechaSistema = Calendar.getInstance(tzSv, new Locale("es", "SV")).getTime();

	}

	@PostConstruct
	public void init() {

	}

	@SuppressWarnings("unchecked")
	public String onLogin() {
		loggedIn = false;
		FacesMessage message = new FacesMessage();
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			if (beforeLogin()) {
				if (usuario == null || clave == null || sisId == 0) {
					message.setDetail(getStringMessage("msg.error.login.nulos"));
					addWarn(message);
					return null;
				} else {
					msgCodigo = 0;
					msgCodigo = validacionLocal.validar(usuario, clave, String.valueOf(sisId));
					// System.out.println("msgCodigo " + msgCodigo);
					// el usuario es valido
					if (msgCodigo.equals(ValidacionSBSLLocal.VAL_USUARIO_VALIDO)) {
						ObjAppsSession objSession = new ObjAppsSession();
						AdmUsuario user = (AdmUsuario) businessLocal.findByPropertyUnique(AdmUsuario.class, "usuLogin",
								usuario);
						if (user == null) {
							message.setDetail(getStringMessage("msg.error.login"));
							addWarn(message);
							return null;
						}
						// System.out.println("user: " + user.getUsuLogin());
						objSession.setUsuario(user);
						objSession.setPermisos((List<AdmUsuariosPermiso>) businessLocal
								.findByProperty(AdmUsuariosPermiso.class, "admUsuario", user));
						// System.out.println("permisos: " +
						// objSession.getPermisos().size());
						menu = (new SystemConfig()).crearMenu(
								(List<IAdmMenu>) businessLocal.menuByUser(AdmMenu.class, user.getUsuId(), sisId));
						try {
							objSession.setSistema((AdmSistema) businessLocal.findByPk(AdmSistema.class, sisId));
							// System.out.println("sistema: " +
							// objSession.getSistema().getSisNombre());
						} catch (Exception e) {
							message.setDetail(getStringMessage("msg.error.valida.sistema"));
							addWarn(message);
							e.printStackTrace();
							return null;
						}
						context.getExternalContext().getSessionMap().put(AppConstant.AUTH_KEY, usuario);
						context.getExternalContext().getSessionMap().put(AppConstant.AUTH_SESSION, objSession);
						setObjAppsSession(objSession);
						loggedIn = true;
						afterLogin();
					} else if (msgCodigo.equals(ValidacionSBSLLocal.VAL_USUARIO_EXPIRADO)) {
						message.setDetail("Usuario ya expiro");
						addWarn(message);
						afterLogin();
						return changePassOutcome;
					} else if (msgCodigo.equals(ValidacionSBSLLocal.VAL_ERROR_EN_CLAVE)) {
						message.setDetail("Contraseña incorrecta");
						addWarn(message);
						return null;
					} else {
						// message.setDetail(getStringMessage("msg.error.inicio.sesion"));
						message.setDetail("Se presento un error al iniciar Sesion en el Sistema");
						addWarn(message);
						return null;
					}
				}
				// System.out.println("outcome" + outcome);
				return outcome;
			}
		} catch (Exception e) {
			e.printStackTrace();
			message.setDetail(getStringMessage("msg.error.inicio.sesion"));
			addWarn(message);
			return null;
		}
		return null;
	}

	protected boolean beforeLogin() {
		return true;
	}

	protected void afterLogin() {
	}

	public String onLogout() {
		if (beforeLogout()) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getSessionMap().remove(AppConstant.AUTH_KEY);
			context.getExternalContext().getSessionMap().remove(AppConstant.AUTH_SESSION);
			HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
			HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-control", "must-revalidate");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			// Eliminando los MB's de la aplicacion
			Enumeration<String> attribs = session.getAttributeNames();
			while (attribs.hasMoreElements()) {
				String mb = attribs.nextElement();
				if (mb.endsWith("MB")) {
					session.removeAttribute(mb);
				}
			}
			session.invalidate();
			this.usuario = null;
			this.clave = null;
			this.loggedIn = false;
			menu = null;
			afterLogout();
			return outcome;
		}
		return null;
	}

	protected void afterLogout() {
	}

	protected boolean beforeLogout() {
		return true;
	}

	public String onChangePasswd() {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if (this.clave.equals(this.claveNueva)) {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("ADVERTENCIA:");
			message.setDetail(getStringMessage("msg.error.clave.igual"));
			context.addMessage(null, message);
		} else if (!this.claveNueva.equals(this.claveConfirmada)) {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			message.setSummary("ADVERTENCIA:");
			message.setDetail(getStringMessage("msg.error.clave.diferente"));
			context.addMessage(null, message);
		} else {
			Integer msgCodigo = validacionLocal.cambiarClave(usuario, clave, claveNueva);
			if (msgCodigo != 20) {
				message.setSeverity(FacesMessage.SEVERITY_WARN);
				message.setSummary("ADVERTENCIA:");
				message.setDetail("Error al cambiar la contraseña");
				context.addMessage(null, message);
			} else {
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				message.setSummary("INFORMACION:");
				message.setDetail(getStringMessage("msg.info.clave") + " " + getStringMessage("msg.info.usuario"));
				context.addMessage(null, message);

				if (isRunLogout()) {
					return onLogout();
				}
			}
		}
		return null;
	}

	public void onGeneraClave() {
		RequestContext rcontext = RequestContext.getCurrentInstance();
		Integer msgCodigo = validacionLocal.generarClave(usuario, sisId);
		if (msgCodigo != 20) {
			addWarn(new FacesMessage(null, "Error al generar la clave"));
			rcontext.addCallbackParam("success", false);
		} else {
			addInfo(new FacesMessage(getStringMessage("lbl.clave.temporal"),
					getStringMessage("msg.envio.clave.correo.int")));
			rcontext.addCallbackParam("success", true);
		}
	}

	public PanelMenu getMenu() {
		return menu;
	}

	public void setMenu(PanelMenu menu) {
		this.menu = menu;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getClaveNueva() {
		return claveNueva;
	}

	public void setClaveNueva(String claveNueva) {
		this.claveNueva = claveNueva;
	}

	public String getClaveConfirmada() {
		return claveConfirmada;
	}

	public void setClaveConfirmada(String claveConfirmada) {
		this.claveConfirmada = claveConfirmada;
	}

	public long getSisId() {
		return sisId;
	}

	public void setSisId(long sisId) {
		this.sisId = sisId;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public String getChangePassOutcome() {
		return changePassOutcome;
	}

	public void setChangePassOutcome(String changePassOutcome) {
		this.changePassOutcome = changePassOutcome;
	}

	public Date getFechaSistema() {
		return fechaSistema;
	}

	public void setFechaSistema(Date fechaSistema) {
		this.fechaSistema = fechaSistema;
	}

	public boolean isValidarSistema() {
		return validarSistema;
	}

	public void setValidarSistema(boolean validarSistema) {
		this.validarSistema = validarSistema;
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getDui() {
		return dui;
	}

	public void setDui(String dui) {
		this.dui = dui;
	}

	public boolean isRunLogout() {
		return runLogout;
	}

	public void setRunLogout(boolean runLogout) {
		this.runLogout = runLogout;
	}

	public Integer getMsgCodigo() {
		return msgCodigo;
	}

	public void setMsgCodigo(Integer msgCodigo) {
		this.msgCodigo = msgCodigo;
	}
}
