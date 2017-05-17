package santamaria.util.parameters;

import javax.persistence.TemporalType;

public class JpqlParamExt extends JpqlParam {

	private TemporalType temporal;
	
	public JpqlParamExt() {		
	}

	public JpqlParamExt(String name, Object value) {
		super(name, value);
		if((value.getClass().getSimpleName().equals("Date")) ||
				(value.getClass().getSimpleName().equals("Calendar"))) {
			this.temporal = TemporalType.DATE;
		}
	}

	public JpqlParamExt(String name, Object value, TemporalType temporal) {
		super(name, value);
		this.temporal = temporal;
	}

	public TemporalType getTemporal() {
		return temporal;
	}

	public void setTemporal(TemporalType temporal) {
		this.temporal = temporal;
	}
}
