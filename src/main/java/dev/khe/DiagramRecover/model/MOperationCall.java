package dev.khe.DiagramRecover.model;

import java.util.ArrayList;


import com.github.javaparser.ast.expr.Expression;

public class MOperationCall {
	//private ArrayList<Node> caseList;
	private ArrayList<String> selectList= null;
	private String qualifiedName = null;

	private String name = null;

	private String returnType = null;
	
	private String ownerClassQualifiedName = null;

	private String belongingOperationQualifiedName = null;
	
	private boolean isConstructor = false;
	
	private boolean isCalled = false;

	public void  setSelctList(ArrayList list) {selectList = list;}
	public ArrayList getSelctList() {return selectList;}
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

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getOwnerClassQualifiedName() {
		return ownerClassQualifiedName;
	}

	public void setOwnerClassQualifiedName(String ownerClassQualifiedName) {
		this.ownerClassQualifiedName = ownerClassQualifiedName;
	}

	public String getBelongingOperationQualifiedName() {
		return belongingOperationQualifiedName;
	}

	public void setBelongingOperationQualifiedName(String belongingOperationQualifiedName) {
		this.belongingOperationQualifiedName = belongingOperationQualifiedName;
	}

	public boolean isConstructor() {
		return isConstructor;
	}

	public void setConstructor(boolean isConstructor) {
		this.isConstructor = isConstructor;
	}

	public boolean isCalled() {
		return isCalled;
	}

	public void setCalled(boolean isCalled) {
		this.isCalled = isCalled;
	}


}
