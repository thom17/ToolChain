package dev.khe.DiagramRecover.model;

import java.util.ArrayList;
import java.util.List;

public class MClass {

	private boolean isInterface = false;

	private String qualifiedName = null;

	private String name = null;

	private String ownerPackageName = null;

	private List<String> modifierList = null;		//protected private 

	private List<String> operationQualifiedNameList = null;

	private String superClassQualifiedName = null;

	private List<String> superInterfaceQualifiedNameList = null;
	
	private List<MMember> memberList = null;

	public MClass() {
		this.modifierList = new ArrayList<String>();
		this.operationQualifiedNameList = new ArrayList<String>();
		this.superInterfaceQualifiedNameList = new ArrayList<String>();
		this.memberList = new ArrayList<MMember>();
	}

	public boolean isInterface() {
		return isInterface;
	}

	public void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
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

	public String getOwnerPackageName() {
		return ownerPackageName;
	}

	public void setOwnerPackageName(String ownerPackageName) {
		this.ownerPackageName = ownerPackageName;
	}

	public List<String> getModifierList() {
		return modifierList;
	}

	public void addModifier(String modifier) {
		modifierList.add(modifier);
	}

	public List<String> getOperationQualifiedNameList() {
		return operationQualifiedNameList;
	}

	public void setOperationQualifiedNameList(List<String> operationQualifiedNameList) {
		this.operationQualifiedNameList = operationQualifiedNameList;
	}

	public String getSuperClassQualifiedName() {
		return superClassQualifiedName;
	}

	public void setSuperClassQualifiedName(String superClassQualifiedName) {
		this.superClassQualifiedName = superClassQualifiedName;
	}

	public List<String> getSuperInterfaceQualifiedNameList() {
		return superInterfaceQualifiedNameList;
	}

	public void addSuperInterfaceQualifiedName(String superInterfaceQualifiedName) {
		superInterfaceQualifiedNameList.add(superInterfaceQualifiedName);
	}

	public List<MMember> getMemberList() {
		return memberList;
	}

	public void addMember(MMember member) {
		memberList.add(member);
	}

}
