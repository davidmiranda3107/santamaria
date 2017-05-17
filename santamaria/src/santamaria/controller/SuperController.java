package santamaria.controller;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import santamaria.security.ObjAppsSession;
import santamaria.util.AppConstant;

public class SuperController extends ReportController {
	
	protected boolean error = false;
	protected String outcome;
	protected String bundleName;
	protected ObjAppsSession objAppsSession;
	
	public SuperController(){
		outcome = null;
	}

	protected void addMessage(FacesMessage message) {  
		RequestContext context = RequestContext.getCurrentInstance(); 
		FacesContext.getCurrentInstance().addMessage(null, message);
//		context.showMessageInDialog(message);
		context.addCallbackParam("error", error);
    }
	
	protected void addInfo(FacesMessage message) {
		message.setSeverity(FacesMessage.SEVERITY_INFO);
		if(message.getSummary() == null) {
			message.setSummary("INFORMACION:");			
		}
		error = false;
		addMessage(message);  
    }  
	
	protected void addError(FacesMessage message) {  
		message.setSeverity(FacesMessage.SEVERITY_ERROR);
		if(message.getSummary() == null) {
			message.setSummary("ERROR:");
		}
		error = true;
		addMessage(message);  
    }
	
	protected void addWarn(FacesMessage message) {  
		message.setSeverity(FacesMessage.SEVERITY_WARN);
		if(message.getSummary() == null) {
			message.setSummary("ADVERTENCIA:");
		}
		error = true;
		addMessage(message);  
    }
	
	protected void addFatal(FacesMessage message) {  
		message.setSeverity(FacesMessage.SEVERITY_FATAL);
		if(message.getSummary() == null) {
			message.setSummary("FATAL:");
		}
		error = true;
		addMessage(message);  
    }
	
	public String getStringMessage(String key) {
		return getBundleApps().getString(key);
	}
	
	public String getStringMessage(String key, String bundleName) {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundle = context.getApplication().getResourceBundle(
				context, bundleName);
		return bundle.getString(key);
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public String getBundleName() {
		return bundleName;
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	public ObjAppsSession getObjAppsSession() {
		if (objAppsSession == null){
			objAppsSession = (ObjAppsSession) FacesContext.getCurrentInstance().
					getExternalContext().getSessionMap().get(AppConstant.AUTH_SESSION);
		}
		return objAppsSession;
	}

	public void setObjAppsSession(ObjAppsSession objAppsSession) {
		this.objAppsSession = objAppsSession;
	}
	
}
