package santamaria.interfaces;

import java.math.BigDecimal;

public interface IAdmMenu {

	public long getMnuId();
	public void setMnuId(long mnuId);

	public String getMnuNombre();
	public void setMnuNombre(String mnuNombre);

	public BigDecimal getMnuOrden();
	public void setMnuOrden(BigDecimal mnuOrden);

	public BigDecimal getMnuIdPadre();
	public void setMnuIdPadre(BigDecimal mnuIdPadre);
	
	public String getMnuTipo();
	public void setMnuTipo(String mnuTipo);

	public String getMnuUrl();
	public void setMnuUrl(String mnuUrl);
}
