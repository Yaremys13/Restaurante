package rest.model;


import java.util.List;

public class Menu {

	private Long idmenu;
	private String fecha;
	private String estatus;
	private List<Plato> platos;
	
	public Long getIdmenu() {
		return idmenu;
	}
	public void setIdmenu(Long idmenu) {
		this.idmenu = idmenu;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public List<Plato> getPlatos() {
		return platos;
	}
	public void setPlatos(List<Plato> platos) {
		this.platos = platos;
	}
	
	
	@Override
	public String toString() {
		StringBuffer menu = new StringBuffer("{ fecha : ").append(this.fecha)
			.append(", idmenu : ").append(this.idmenu)
			.append(", estatus : ").append(this.estatus);
			
		menu.append("[");
		for (Plato p : this.platos)
		{	menu.append("{ idplato : ").append(p.getImgplato())
			.append(", nomplato : ").append(p.getNomplato())
			.append(" [ ");
			for (Ingrediente i : p.getIngredientes())
			{	menu.append("{ nomingrediente : ").append( i.getNomingrediente());
				menu.append("}");
			}
			menu.append("]}");			
		}
		menu.append(" ] }");
		return menu.toString();
	}

	
	
}
