package rest.model;

import java.math.BigDecimal;
import java.util.List;

public class Plato {
	
	private Long idplato;
	private String nomplato;
	private String descplato;
	private BigDecimal precplato;
	private String fecha;
	private String imgplato;
	private String estatus;
	private List<Ingrediente> ingredientes;
	
	
	public Long getIdplato() {
		return idplato;
	}
	public void setIdplato(Long idplato) {
		this.idplato = idplato;
	}
	public String getNomplato() {
		return nomplato;
	}
	public void setNomplato(String nomplato) {
		this.nomplato = nomplato;
	}
	public String getDescplato() {
		return descplato;
	}
	public void setDescplato(String descplato) {
		this.descplato = descplato;
	}
	public BigDecimal getPrecplato() {
		return precplato;
	}
	public void setPrecplato(BigDecimal precplato) {
		this.precplato = precplato;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}	
	
	public String getImgplato() {
		return imgplato;
	}
	public void setImgplato(String imgPlato) {
		this.imgplato = imgPlato;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public List<Ingrediente> getIngredientes() {
		return ingredientes;
	}
	public void setIngredientes(List<Ingrediente> ingredientes) {
		this.ingredientes = ingredientes;
	}
	
	@Override
	public String toString() {
		StringBuffer plato = new StringBuffer("{ nomplato : ").append(this.nomplato)
			.append(", desplato : ").append(this.descplato)
			.append(", precplato : ").append(this.precplato)
			.append(", fecha : ").append(this.fecha)
			.append(", imgplato : ").append(this.imgplato);
		plato.append("[");
		for (Ingrediente i : this.ingredientes)
		{	plato.append("{ idingrediente : ").append(i.getIdingrediente())
			.append(", nomingrediente : ").append(i.getNomingrediente())
			.append("}");			
		}
		plato.append(" ] }");
		return plato.toString();
	}

	

}
