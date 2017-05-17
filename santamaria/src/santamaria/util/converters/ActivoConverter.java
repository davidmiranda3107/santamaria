package santamaria.util.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(value="activo")
public class ActivoConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent comp, String value) throws ConverterException {
		return value;
	}
	
	@Override
	public String getAsString(FacesContext facesContext, UIComponent comp, Object value) throws ConverterException {
		String valor = value.toString();
		if (!valor.isEmpty()){
			if (valor.equals("1"))
				valor = "ACTIVO";
			else if (valor.equals("0"))
				valor = "INACTIVO";
			else
				valor = "---";
		}
		return valor;
	}
}
