package santamaria.components;

import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;

@FacesComponent("generaRep")
public class GeneraRep extends UIInput implements NamingContainer {

	public GeneraRep() {
		
	}
	
	@Override
	public String getFamily() {
		return UINamingContainer.COMPONENT_FAMILY;
	}
}
