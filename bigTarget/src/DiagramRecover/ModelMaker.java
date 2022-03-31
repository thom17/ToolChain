package dev.khe.DiagramRecover;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import dev.khe.DiagramRecover.model.MClass;
import dev.khe.DiagramRecover.model.MMember;
import dev.khe.DiagramRecover.model.MOperation;
import dev.khe.DiagramRecover.model.MOperationCall;
import dev.khe.DiagramRecover.model.ModelManager;

public class ModelMaker extends VoidVisitorAdapter<StringBuilder> {

	private ModelManager manager = null;

	public ModelMaker(ModelManager manager) {
		this.manager = manager;
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, StringBuilder arg) {
		// TODO Auto-generated method stub
		MClass _class = new MClass();

		if (n.isInterface()) {
			_class.setInterface(true);
		}

		_class.setQualifiedName(n.resolve().getQualifiedName());
		_class.setName(n.getName().toString());
		_class.setOwnerPackageName(n.resolve().getPackageName());

		NodeList<Modifier> modifierList = n.getModifiers();
		Modifier m = null;
		for (int i = 0; i < modifierList.size(); i++) {
			m = modifierList.get(i);
			_class.addModifier(m.toString());
		}

		// 슈퍼클래스 상속 관련 구문 처리
		NodeList<ClassOrInterfaceType> exTypeList = n.getExtendedTypes();
		ClassOrInterfaceType coi = null;
		for (int i = 0; i < exTypeList.size(); i++) {
			coi = exTypeList.get(i);
			_class.setSuperClassQualifiedName(coi.resolve().getQualifiedName());
		}

		// 인터페이스 구현 관련 구문 처리
		NodeList<ClassOrInterfaceType> implTypeList = n.getImplementedTypes();
		coi = null;
		for (int i = 0; i < implTypeList.size(); i++) {
			coi = implTypeList.get(i);
			_class.addSuperInterfaceQualifiedName(coi.resolve().getQualifiedName());
		}

		manager.add_Class(_class);

		super.visit(n, arg);
	}

	@Override
	public void visit(FieldDeclaration n, StringBuilder arg) {
		// TODO Auto-generated method stub
		if (false == n.findAncestor(MethodDeclaration.class).isPresent()) {
			MMember member = new MMember();
			member.setName(n.resolve().getName().toString());

			// 타입 분석
			member.setTypeName(TypeUtil.getTypeStr(n.resolve().getType()));

			// 소속 클래스 분석
			if (n.findAncestor(ClassOrInterfaceDeclaration.class).isPresent()) {
				member.setOwnerClassQualifiedName(
						n.findAncestor(ClassOrInterfaceDeclaration.class).get().resolve().getQualifiedName());
			} else {
				PrintUtil.printlnError("Can not find ancestor \"ClassOrInterfaceDeclaration\" about \""
						+ n.resolve().getName().toString() + "\"!");
				return;
			}

			// Modifier 분석
			NodeList<Modifier> modifierList = n.getModifiers();
			Modifier m = null;
			for (int i = 0; i < modifierList.size(); i++) {
				m = modifierList.get(i);
				member.addModifier(m.toString());
			}

			manager.addMember(member);
		}

		super.visit(n, arg);
	}

	@Override
	public void visit(ConstructorDeclaration n, StringBuilder arg) {
		// TODO Auto-generated method stub
		if (n.findAncestor(MethodDeclaration.class).isPresent()) {
			return;
		}

		MOperation operation = new MOperation();
		operation.setQualifiedName(n.resolve().getQualifiedName());
		operation.setName(n.getName().toString());
		operation.setOwnerClassQualifiedName(
				operation.getQualifiedName().substring(0, operation.getQualifiedName().lastIndexOf(".")));
		operation.setReturnType("");

		NodeList<Modifier> modifierList = n.getModifiers();
		Modifier m = null;
		for (int i = 0; i < modifierList.size(); i++) {
			m = modifierList.get(i);
			operation.addModifier(m.toString());
		}

		NodeList<Parameter> parameterList = n.getParameters();
		Parameter p = null;
		for (int i = 0; i < parameterList.size(); i++) {
			p = parameterList.get(i);
			operation.addArgumentType(TypeUtil.getTypeStr(p.resolve().getType()));
		}

		manager.addOperation(operation);

		super.visit(n, arg);
	}

	@Override
	public void visit(MethodDeclaration n, StringBuilder arg) {
		// TODO Auto-generated method stub
		if (n.findAncestor(MethodDeclaration.class).isPresent()) {
			return;
		}

		MOperation operation = new MOperation();
		operation.setQualifiedName(n.resolve().getQualifiedName());
		operation.setName(n.getName().toString());
		operation.setOwnerClassQualifiedName(
				operation.getQualifiedName().substring(0, operation.getQualifiedName().lastIndexOf(".")));
		// operation.setReturnType(n.getType().toString());
		operation.setReturnType(TypeUtil.getTypeStr(n.resolve().getReturnType()));

		NodeList<Modifier> modifierList = n.getModifiers();
		Modifier m = null;
		for (int i = 0; i < modifierList.size(); i++) {
			m = modifierList.get(i);
			operation.addModifier(m.toString());
		}

		NodeList<Parameter> parameterList = n.getParameters();
		Parameter p = null;
		for (int i = 0; i < parameterList.size(); i++) {
			p = parameterList.get(i);
			// operation.addArgumentType(p.getType().toString());
			operation.addArgumentType(TypeUtil.getTypeStr(p.resolve().getType()));
		}

		manager.addOperation(operation);

		super.visit(n, arg);
	}

	@Override
	public void visit(ObjectCreationExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);

		MOperationCall operationCall = new MOperationCall();
		operationCall.setConstructor(true);
		operationCall.setQualifiedName(n.resolve().getQualifiedName());
		operationCall.setName(n.resolve().getName());
		// operationCall.setName(n.getName().toString());
		operationCall.setReturnType("void");
		// operationCall.setReturnType(n.resolve().getReturnType().toString());
		operationCall.setOwnerClassQualifiedName(
				operationCall.getQualifiedName().substring(0, operationCall.getQualifiedName().lastIndexOf(".")));

		if (n.findAncestor(MethodDeclaration.class).isPresent()) {
			MethodDeclaration md = n.findAncestor(MethodDeclaration.class).get();
			operationCall.setBelongingOperationQualifiedName(md.resolve().getQualifiedName());
			manager.addOperationCall(operationCall);
		} else if (n.findAncestor(ConstructorDeclaration.class).isPresent()) {
			ConstructorDeclaration cd = n.findAncestor(ConstructorDeclaration.class).get();
			operationCall.setBelongingOperationQualifiedName(cd.resolve().getQualifiedName());
			manager.addOperationCall(operationCall);
		}
	}

	@Override
	public void visit(MethodCallExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);

		MOperationCall operationCall = new MOperationCall();
		operationCall.setQualifiedName(n.resolve().getQualifiedName());
		operationCall.setName(n.getName().toString());
		operationCall.setReturnType(TypeUtil.getTypeStr(n.resolve().getReturnType()));
		operationCall.setOwnerClassQualifiedName(
				operationCall.getQualifiedName().substring(0, operationCall.getQualifiedName().lastIndexOf(".")));

		if (n.findAncestor(MethodDeclaration.class).isPresent()) {
			MethodDeclaration md = n.findAncestor(MethodDeclaration.class).get();
			operationCall.setBelongingOperationQualifiedName(md.resolve().getQualifiedName());
			manager.addOperationCall(operationCall);
		} else if (n.findAncestor(ConstructorDeclaration.class).isPresent()) {
			ConstructorDeclaration cd = n.findAncestor(ConstructorDeclaration.class).get();
			operationCall.setBelongingOperationQualifiedName(cd.resolve().getQualifiedName());
			manager.addOperationCall(operationCall);
		}
	}

}
