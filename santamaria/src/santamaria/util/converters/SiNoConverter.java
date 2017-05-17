package santamaria.util.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(value="siNo")
public class SiNoConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent comp, String value) throws ConverterException {
		return value;
	}
	
	@Override
	public String getAsString(FacesContext facesContext, UIComponent comp, Object value) throws ConverterException {
		String valor = value.toString();
		if (!valor.isEmpty()){
			if (valor.equalsIgnoreCase("S"))
				valor = "SI";
			else if (valor.equalsIgnoreCase("N"))
				valor = "NO";
			else
				valor = "--";
		}
		return valor;
	}
}
