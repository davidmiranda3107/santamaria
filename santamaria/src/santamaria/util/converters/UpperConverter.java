package santamaria.util.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(value="upper")
public class UpperConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent comp, String value) throws ConverterException {
		if (!value.isEmpty()){
			value = value.toUpperCase();
		}
		return value;
	}
	
	@Override
	public String getAsString(FacesContext facesContext, UIComponent comp, Object value) throws ConverterException {
		return value.toString();
	}
}
