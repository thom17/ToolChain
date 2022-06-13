package visitor;

import java.util.List;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.CallableDeclaration.Signature;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;

import complexity.TypeLevel;
import dataSet.Class;
import dataSet.OMS;
import dataSet.Data_base;
import dataSet.Function;
import dataSet.Member;
import dev.khe.DiagramRecover.TypeUtil;
/**
 * 현재 사용중
 * 
 *
 */
public class DataMaker extends VoidVisitorAdapter<StringBuilder>
{
	private OMS datalist;
	Class hostClass;
	
	public DataMaker(OMS datalist)	
	{
		this.datalist = datalist; 
	};
	
	private void setExtendClass(Class target, ClassOrInterfaceDeclaration n)
	{
		NodeList<ClassOrInterfaceType> extendList = n.getExtendedTypes();
		int size = extendList.size();
		if( 0 < size )
		{
			Class extendClassList[] = new Class[size];
			String srcName[] = new String[size];
			for(int i = 0; i < size; i++)
			{
				//ResolvedReferenceType resolve = extendList.get(i).resolve();
				//String extendSrc = resolve.getQualifiedName();
				ClassOrInterfaceType extendCls = extendList.get(i);
				ResolvedReferenceType resolve = extendCls.resolve();
				String extendSrc = resolve.getQualifiedName();
				
				extendClassList[i] = datalist.findClass(extendSrc);
				srcName[i] = extendSrc;
				if(extendClassList[i] == null)
				{
					extendClassList[i] = new Class(extendSrc);
					datalist.addData (extendClassList[i]);					
				}	
			}
			
			target.setExtendClass(extendClassList);	
			target.setExtendSrcName(srcName);
		}
		
	}
	private void setImplementedClass(Class target, ClassOrInterfaceDeclaration n)
	{
		NodeList<ClassOrInterfaceType> extendList = n.getImplementedTypes();
		int size = extendList.size();
		if( 0 < size )
		{
			Class extendClassList[] = new Class[size];
			String srcName[] = new String[size];
			for(int i = 0; i < size; i++)
			{
				ResolvedReferenceType resolve = extendList.get(i).resolve();
				String extendSrc = resolve.getQualifiedName();
				String extendName = extendList.get(i).getName().asString();
				
				extendClassList[i] = datalist.findClass(extendSrc);
				srcName[i] = extendSrc;
				if(extendClassList[i] == null)
				{
					extendClassList[i] = new Class(extendSrc);
					datalist.addData(extendClassList[i]);					
				}	
			}
			
			target.setImplementClass(extendClassList);	
			target.setImplementSrcName(srcName);
		}
		
	}
	public void visit(PackageDeclaration n , StringBuilder arg)
	{
		arg.append(n.toString());
	}
	
	@Override	//클래스,인터페이스 구조 작성 정의 bodt
	public void visit(ClassOrInterfaceDeclaration n , StringBuilder arg)
	{
		ResolvedReferenceTypeDeclaration resolve = n.resolve();
		String srcName = resolve.getQualifiedName();
		Class cls = datalist.findClass(srcName);
		if(cls == null)
		{
			cls = new Class(n.resolve().getPackageName().toString(), n.getName().toString());
			datalist.addData(cls);
		}
		//////system.outprint("super");
		// 슈퍼클래스 상속 관련 구문 처리 
		setExtendClass(cls, n);
		setImplementedClass(cls, n);
				
		cls.setInterface(resolve.isInterface());
		cls.setIsInterface(resolve.isInterface());
		cls.setLib(false);
		setModif(cls, n.getModifiers() );
		arg.append( srcName);
		hostClass =cls;
		super.visit(n, arg);
		arg.delete(0, arg.length());
	}
	public void setModif(Data_base target, NodeList<Modifier> list )
	{
		for(Modifier modfi : list)
		{
			String str = modfi.toString();
		//	System.out.println("mod : "+str);
			if(str.equals("private ") || str.equals("protected "))
				target.setModfi(str);
			else if(str.equals("static "))
				target.setStatic(true);
			else if(str.equals("abstract "))
				target.setAbstract(true);
		//	target.setAbstract(isAbstract);
		}
	}
	
	@Override	//필드 선언 body
	public void visit(FieldDeclaration n, StringBuilder arg)
	{
		Class cls = hostClass;
		String type = "null";
		for(VariableDeclarator dec : n.getVariables()) //int x, y, z 와 같은경우?
		{
			String name = dec.getName().asString();
			Class typeClass = null;
			Type tp = dec.getType();
		
			//ResolvedType typeResolve = tp.getElementType().resolve();	
			ResolvedType typeResolve = tp.getElementType().resolve();			
			String describe = typeResolve.describe();

			String list[] = describe.split("\\.");
			if(1 < list.length) //Class
			{
				typeClass = datalist.findClass(describe);
				if(typeClass == null)
				{
					typeClass = new Class(describe);
					datalist.addData(typeClass);
				}
				cls.addHas(typeClass);
			}
			type = tp.resolve().describe();
			Member member = new Member(type, name, cls);	
			member.setTypeDef(typeClass);
			setModif(member, n.getModifiers());
			cls.addHas(member);
			datalist.addData(member);	
						
		}
		

		//super.visit(n, arg);
		
		//////system.outprintln(n.resolve().getType().toString()); //error
	}
	
	@Override	//생성자 정의 body
	public void visit(ConstructorDeclaration n, StringBuilder arg) 
	{
		Class cls = hostClass;
		ResolvedConstructorDeclaration resolve = n.resolve();
		
		String srcSignature = resolve.getQualifiedSignature();
		
		String forDebug = resolve.getSignature();
		if(forDebug.equals("checkRecipe(coffeeShop.Resource)"))
			System.out.println("for debug");
		Function fun = datalist.findFunction(srcSignature);
		if(fun == null)
		{
			fun = new Function(resolve.getSignature(), cls);
		//	fun = new Function( type, name, cls );
			datalist.addData(fun);
		}
	
		String name = n.getName().asString();
		String type = hostClass.getName();
		//fun.setNode(n.asMethodDeclaration());	//is ok?
		fun.setBlock(n.getBody());
		fun.setInit(type, name);
		fun.setConstructor(true);
		
		setParameter(n.getParameters() , fun);
		
		cls.addHas(fun);
		////system.outprintln("\n}");
		super.visit(n, arg);
		//////system.outprintln("after arg is "+arg);
	}

	private void setParameter(NodeList<Parameter> list, Function fun) 
	{
		String type[] = null;
		String name[] = null;
		if( ! list.isEmpty() )
		{
			int size = list.size();
			type = new String[size];
			name = new String[size];
			
			for(int i=0; i<size; i++ )
			{
				Parameter info = list.get(i);
				ResolvedParameterDeclaration resolve=info.resolve();
				type[i] = resolve.describeType();
		
				if( type[i].equals("dataSet.Class[]"))
					System.out.println();
				
		//		type[i] = info.getType().toString();
				name[i] = info.getName().asString();
				Member parameter = new Member(type[i] , name[i], fun);
				fun.addHas( parameter);
				datalist.addData(parameter);
				if(!TypeLevel.isDataType(type[i]))
				{
					String elementType = info.getType().getElementType().resolve().describe();
					Class cls = datalist.findClass(elementType);
					if(cls == null)
						cls = new Class(type[i]);
					fun.addCall(cls);
				}
				
				
			}
			fun.setParameter(type, name);
		}
	}

	public void setFunctionModifier(ResolvedMethodDeclaration n, Function fun)
	{
		fun.setStatic(n.isStatic());
		fun.setAbstract(n.isAbstract());
	}
	
	@Override	//함수 정의 body
	public void visit(MethodDeclaration n, StringBuilder arg) 
	{
		Class cls = hostClass;
		ResolvedMethodDeclaration resolve = n.resolve();
		NodeList<MethodCallExpr> list = new NodeList<MethodCallExpr>();
		Object obj =n.findAll(	MethodCallExpr.class);
		Signature sig = n.getSignature();
		String srcSignature = resolve.getQualifiedSignature();
		System.out.println("dataMaker (srcSig) : "+srcSignature);
		System.out.println("dataMake (sig) : " + n.getSignature());
		String forDebug = resolve.getSignature();
		if(forDebug.equals("setWater(int)"))
			System.out.println("for debug");
		Function fun = datalist.findFunction(srcSignature);
		if(fun == null)
		{
			fun = new Function(resolve.getSignature(), cls);
		//	fun = new Function( type, name, cls );
			datalist.addData(fun);
		}
		
		String name = n.getName().asString();
		String type = n.getType().toString();
		
		if(n.getComment().isPresent())
			fun.setCmd(n.getComment().get().getContent());
		
		fun.setInit(type, name);
		fun.setStatic(n.isStatic());
		fun.setNode(n);
		
		setFunctionModifier(resolve, fun);
		setParameter ( n.getParameters(), fun);
		//setParameter(resolve, fun);	resolve = ResolvedMethodDeclaration 만 가능해서 constructor와 함께 적용 못함.
		
		cls.addHas(fun);
		arg.append("."+fun.getName()+fun.getParameterName());
		
		////system.outprintln("\n}");
		super.visit(n, arg);
		//////system.outprintln("after arg is "+arg);
	}



}
