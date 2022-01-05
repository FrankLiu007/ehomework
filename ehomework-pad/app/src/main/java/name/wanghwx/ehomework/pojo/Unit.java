package name.wanghwx.ehomework.pojo;

import java.util.List;

public class Unit{

	private Integer unitId;
	
	private Material material;
	
	private String name;
	
	private Unit parent;
	
	private Boolean isParent;
	
	private List<Item> items;

	public Integer getUnitId(){
		return unitId;
	}

	public void setUnitId(Integer unitId){
		this.unitId = unitId;
	}

	public Material getMaterial(){
		return material;
	}

	public void setMaterial(Material material){
		this.material = material;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public Unit getParent(){
		return parent;
	}

	public void setParent(Unit parent){
		this.parent = parent;
	}

	public Boolean getIsParent(){
		return isParent;
	}

	public void setIsParent(Boolean isParent){
		this.isParent = isParent;
	}

	public List<Item> getItems(){
		return items;
	}

	public void setItems(List<Item> items){
		this.items = items;
	}

}