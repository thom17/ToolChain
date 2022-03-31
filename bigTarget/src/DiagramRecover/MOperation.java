package dev.khe.DiagramRecover.model;

import java.util.ArrayList;
import java.util.List;

public class MOperation {
	
	private String qualifiedName = null;
	
	private String name = null;
	
	private String ownerClassQualifiedName = null;
	
	private String returnType = null;
	
	private List<String> modifierList = null;
	
	private List<String> argumentTypeList = null;

	private List<String> oprationCallQualifiedNameList = null;

	public MOperation() {
		modifierList = new ArrayList<String>();
		argumentTypeList = new ArrayList<String>();
		oprationCallQualifiedNameList = new ArrayList<String>();
	}

	public String getQualifiedName() {
		return qualifiedName;
	}

	public void setQualifiedName(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwnerClassQualifiedName() {
		return ownerClassQualifiedName;
	}

	public void setOwnerClassQualifiedName(String ownerClassQualifiedName) {
		this.ownerClassQualifiedName = ownerClassQualifiedName;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public List<String> getModifierList() {
		return modifierList;
	}

	public void addModifier(String modifier) {
		modifierList.add(modifier);
	}

	public List<String> getArgumentTypeList() {
		return argumentTypeList;
	}

	public void addArgumentType(String argumentType) {
		argumentTypeList.add(argumentType);
	}

	public List<String> getOprationCallQualifiedNameList() {
		return oprationCallQualifiedNameList;
	}

	public void setOprationCallQualifiedNameList(List<String> oprationCallQualifiedNameList) {
		this.oprationCallQualifiedNameList = oprationCallQualifiedNameList;
	}
}
