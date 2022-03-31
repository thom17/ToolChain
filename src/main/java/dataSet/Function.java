package dataSet;

import java.util.Map;
import java.util.Optional;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;

import complexity.FunctionComplexity;
import dataSet.statement.StateNode;
import dataSet.statement.StateNodeList;

public class Function extends Data_base{
	MethodDeclaration node;
	BlockStmt blocknode;
	boolean isStatic = false;
	boolean isConstructor = false;
	Data_base owner;
	String returnType;
	String sig;
	String parameterType[] = null;
	String parameterName[] = null;
	StateNodeList nodeList;
	
	FunctionComplexity complex;
	
	/*
	public Function(MethodDeclaration n)
	{
		Optional<BlockStmt> block = n.getBody();
		returnType = n.getType().toString();
		name = n.getName().asString();
		parameterType = (String[]) n.getParameters().toArray();
		if(block.isPresent())
			this.node = block.get();
	//	System.out.println(name + "/"+n.getName().getIdentifier());		
	}
	*/
	public Function(String sig, Class owner)	//임시 선언
	{
		this.owner= owner;
		this.sig = sig;
		String list[] = sig.split("\\(");
		this.name = list[0];
		if(sig.contains(owner.getSrcName()))
			this.sig=sig.replace(owner.getSrcName() , "").substring(1);
	}

	/*
	public void initParameter(String parameter[])
	{
		if(parameter != null)
		{
			//System.out.println("parameter is not null");
			parameterType = new String[parameter.length];
			parameterName = new String[parameter.length];
			for(int i=0; i<parameter.length; i++)
			{
				
				String arg[] = parameter[i].split(" ");
				parameterType[i] = arg[0];
				parameterName[i] = arg[1];
			}
		}
	}
	*/
	public void setNodeList(StateNodeList nodeList) 
	{
		this.nodeList = nodeList;
	}
	public void setInit(String returnType , String name)
	{
		this.returnType = returnType;
		this.name = name;
	}
	public void setNode(MethodDeclaration n)	
	{
		this.node = n;
		if(n.getBody().isPresent())
			this.blocknode = n.getBody().get();	
	};
	public MethodDeclaration getNode() {return this.node; };
	
	public String getSrcName() 
	{
		return  owner.getSrcName() + "." + sig;	
	}

	public String getParameterName()
	{
		if(parameterType == null) return "( )";
		
		String str = "(";
		for(int i=0; i<parameterType.length; i++)
		{
			str += parameterType[i]+" ";
		}
		str+=")";
		return str;
	}
	public void setParameter(String type[], String name[])
	{
		parameterType = type;
		parameterName = name;
	}
	
	public void setConstructor(boolean isConstructor) 
	{
		this.isConstructor = isConstructor;
		if(isConstructor)
			this.returnType = "new";
	}
	public StateNodeList getNodeList() {return nodeList;}
	public StateNode getHeadNode() 
	{
		if(nodeList != null)
			return nodeList.getHead(); 
		else 
			return null;
	}

	public Data_base getOwner() {return this.owner; }
	public String getReturnType() {return this.returnType; }
	public String getSig() {return this.sig; }
	public String getTypeName() { return this.returnType; }
	public String[] getParameterTypeList() { return this.parameterType; }
	public String[] getParameterNameList() { return this.parameterName; }
	
	public int CallOtherClass()
	{
		int num = 0;
		for( Class cls : this.callList.getClassList() )
		{
			Data_base owner = this.owner;
			
			while( owner instanceof Function)
				owner = ((Function) owner).getOwner();
		if(owner != cls)
			num++;
			
		}
		return num;
	}
	public Class getOwnerClass()
	{
		Data_base cls = owner;
		while(owner instanceof Function)
		{
			cls = ((Function) cls).getOwner();
		}
		return (Class) cls;
	}
	public FunctionComplexity getComplex() { 
		if(complex == null)
			complex = new FunctionComplexity(this);
		return this.complex;
		}
	@Override
	public String getPackageName() {
		return owner.getPackageName();
	}
	@Override	//local Variable, local Function
	public void addHas(Data_base target) 
	{
		// TODO Auto-generated method stub
		hasList.addData(target);
	}
	@Override
	public void addCallBy(Data_base target) 
	{
		// TODO Auto-generated method stub
		callByList.addData(target);
		this.owner.addCallBy(target);
	}
	@Override
	public void addCall(Data_base target) {
		// TODO Auto-generated method stub
		callList.addData(target);
		this.owner.addCall(target);
		target.addCallBy(this);
		target.addCallBy(this.owner);
	}
	public BlockStmt getBlockStmt() {
		// TODO Auto-generated method stub
		if(this.blocknode != null)
			return this.blocknode;
		else 
			return null;
	}
	public StateNodeList getStateNodeList() {
		// TODO Auto-generated method stub
		return this.nodeList;
	}
	public void makeComplex()
	{
		this.complex = new FunctionComplexity(this);
	}
	public boolean isConstructor() {
		// TODO Auto-generated method stub
		return this.isConstructor;
	}

	public void setBlock(BlockStmt body) {
		// TODO Auto-generated method stub
		this.blocknode = body;
	}
}
