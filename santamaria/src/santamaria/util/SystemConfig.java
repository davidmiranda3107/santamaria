package santamaria.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.component.menuitem.UIMenuItem;
import org.primefaces.component.panelmenu.PanelMenu;
import org.primefaces.component.submenu.UISubmenu;

import santamaria.interfaces.IAdmMenu;

public class SystemConfig {

	public SystemConfig() {

	}
	
	public PanelMenu crearMenu(List<IAdmMenu> opciones) {
		Map<String, Object> grupos = new HashMap<String, Object>();
		PanelMenu panelMenu = new PanelMenu();
		String id = "";
		try {
			for (IAdmMenu m1 : opciones) {
				if (m1.getMnuTipo().equals("I")) {
					id = "mnuItem" + m1.getMnuId();
					grupos.put(id, getItem(m1));
				} else {
					id = "subMnu"+m1.getMnuId();
					grupos.put(id, getSubMenu(m1));
				}
			}
			String idParent = "";
			String idParent1 = "";
			UIComponent parent = null;
			UIComponent children = null;
			for (IAdmMenu m1 : opciones) {
				if (m1.getMnuIdPadre() != null){
					idParent = "subMnu"+m1.getMnuIdPadre();
					if (!idParent.equals(idParent1)){
						parent = (UIComponent)grupos.get(idParent);
						idParent1 = idParent;
					}
					if (parent != null){
						if (m1.getMnuTipo().equals("I")) {
							id = "mnuItem"+m1.getMnuId();
							children = (UIComponent)grupos.get(id);
							parent.getChildren().add(children);
						} else {
							id = "subMnu"+m1.getMnuId();
							children = (UIComponent)grupos.get(id);
							parent.getChildren().add(children);
						}
					}
				} else {
					if (m1.getMnuTipo().equals("I")) {
						id = "mnuItem"+m1.getMnuId();
					} else {
						id = "subMnu"+m1.getMnuId();
					}
					children = (UIComponent)grupos.get(id);
					panelMenu.getChildren().add(children);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		UISubmenu submenu = new UISubmenu();
		submenu.setId("subMnuSession");
		submenu.setLabel("Sesion");
		UIMenuItem mItemSes = new UIMenuItem();
		mItemSes.setId("subItemSession111");
		mItemSes.setValue("Cambiar Clave");
		mItemSes.setAjax(false);
		mItemSes.setActionExpression(getOutcome("loginMB.getChangePassOutcome"));
		submenu.getChildren().add(mItemSes);
		UIMenuItem mItemSes1 = new UIMenuItem();
		mItemSes1.setId("subItemSession222");
		mItemSes1.setValue("Salir");
		mItemSes1.setAjax(false);
		mItemSes1.setActionExpression(getOutcome("loginMB.onLogout"));
		submenu.getChildren().add(mItemSes1);
		panelMenu.getChildren().add(submenu);
		return panelMenu;
	}
	
	private UIMenuItem getItem(IAdmMenu reg){
		UIMenuItem item = new UIMenuItem();
		item.setId("mnuItem" + reg.getMnuId());
		item.setValue(reg.getMnuNombre());
		item.setUrl(reg.getMnuUrl());
		item.setAjax(false);
		//item.setActionExpression(getOutcomePath(reg.getMnuUrl()));
		return item;
	}


	private UISubmenu getSubMenu(IAdmMenu reg){
		UISubmenu submenu = new UISubmenu();
		submenu.setId("subMnu"+reg.getMnuId());
		submenu.setLabel(reg.getMnuNombre());
		return submenu;
	}
	
	public MethodExpression getOutcome(String outcome) {
		FacesContext fc = FacesContext.getCurrentInstance();
		ELContext ec = fc.getELContext();
		return fc
				.getApplication()
				.getExpressionFactory()
				.createMethodExpression(ec, "#{" + outcome + "}", String.class,
						new Class[] {});
	}

	public MethodExpression getOutcomePath(String outcome) {
		FacesContext fc = FacesContext.getCurrentInstance();
		ELContext ec = fc.getELContext();
		return fc
				.getApplication()
				.getExpressionFactory()
				.createMethodExpression(ec,  outcome, String.class,
						new Class[] {});
	}
}
