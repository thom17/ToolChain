package dev.khe.DiagramRecover.model;

import java.util.ArrayList;
import java.util.List;

public class MMember {

	private String name = null;
	
	private String typeName = null;
	
	private String ownerClassQualifiedName = null;

	private List<String> modifierList = null;
	
	public MMember() {
		this.modifierList = new ArrayList<String>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getOwnerClassQualifiedName() {
		return ownerClassQualifiedName;
	}

	public void setOwnerClassQualifiedName(String ownerClassQualifiedName) {
		this.ownerClassQualifiedName = ownerClassQualifiedName;
	}

	public List<String> getModifierList() {
		return modifierList;
	}

	public void addModifier(String modifier) {
		modifierList.add(modifier);
	}
	
}
