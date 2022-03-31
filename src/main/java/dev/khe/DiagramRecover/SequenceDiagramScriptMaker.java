package dev.khe.DiagramRecover;

import java.util.ArrayList;

import dev.khe.DiagramRecover.model.MClass;
import dev.khe.DiagramRecover.model.MOperation;
import dev.khe.DiagramRecover.model.MOperationCall;
import dev.khe.DiagramRecover.model.ModelManager;

public class SequenceDiagramScriptMaker {

	private ModelManager manager = null;

	private StringBuilder sb = null;

	public SequenceDiagramScriptMaker(ModelManager manager) {
		this.manager = manager;
		this.sb = new StringBuilder();
	}

	public void makeScript() {
		sb = new StringBuilder();
		sb.append("@startuml");
		sb.append("\n");
		sb.append("actor User");
		sb.append("\n");

		for (MClass _class : manager.get_ClassList()) {
			for (String oqn : _class.getOperationQualifiedNameList()) {
				MOperation operation = manager.getOperation(oqn);

				if (operation.getName().equals("main")) {
					sb.append("User -> ");
					sb.append(_class.getQualifiedName());
					sb.append(" : ");
					sb.append(operation.getName());
					sb.append("\n");

					exploreOperation(operation);
					break;
				} // if
			} // for - oqn
		} // for - _class

		sb.append("@enduml");
		sb.append("\n");
	}

	private void exploreOperation(MOperation operation) {
		for (String ocqn : operation.getOprationCallQualifiedNameList()) {
			MOperationCall operationCall = manager.getOperationCall(ocqn);
			MOperation calledOperation = manager.getOperation(operationCall.getQualifiedName());

			if (operationCall.isCalled()) {
				continue;
			}
			//add
			ArrayList<String> list = operationCall.getSelctList();
			if(list != null)
			{
				for(int i=0; i< list.size(); i++)
				{
					for(int s=-1; s<i; s++) System.out.print(" ");
					System.out.println("ㄴ"+list.get(i));
				}
				System.out.println("then "+operationCall.getName());
			}
				
				System.out.println("----------------");
			
			//add dpone
			if (null != calledOperation) {
				// 클래스 내부 메소드를 호출하는 경우
				if (operation.getOwnerClassQualifiedName().equals(operationCall.getOwnerClassQualifiedName())) {
					sb.append(operation.getOwnerClassQualifiedName());
					if (false == calledOperation.getReturnType().equals("void")
							&& false == calledOperation.getReturnType().equals("")) {
						sb.append(" <-- ");
					} else {
						sb.append(" -> ");
					}
					sb.append(operationCall.getOwnerClassQualifiedName());
					sb.append(" : ");

					// operationCall 생성자 여부에 따른 라벨 출력 분기
					if (operationCall.isConstructor()) {
						sb.append("<<Create>>");
					} else {
						sb.append(operationCall.getName());
						sb.append(StringUtil.toString(TypeUtil.getSimpleTypeNameList(calledOperation.getArgumentTypeList()), "(", ", ", ")"));
					}

					if (false == calledOperation.getReturnType().equals("void")
							&& false == calledOperation.getReturnType().equals("")) {
						sb.append(", return: ");
						sb.append(TypeUtil.getSimpleTypeName(calledOperation.getReturnType()));
						// sb.append(calledOperation.getReturnType());
					}

					sb.append("\n");

					exploreOperation(calledOperation);
				} else { // 다른 클래스의 메소드를 호출하는 경우
					sb.append(operation.getOwnerClassQualifiedName());
					sb.append(" -> ");
					sb.append(operationCall.getOwnerClassQualifiedName());
					sb.append(" : ");

					// operationCall 생성자 여부에 따른 라벨 출력 분기
					if (operationCall.isConstructor()) {
						sb.append("<<Create>>");
					} else {
						sb.append(operationCall.getName());
						sb.append(StringUtil.toString(TypeUtil.getSimpleTypeNameList(calledOperation.getArgumentTypeList()), "(", ", ", ")"));
					}

					sb.append("\n");

					operationCall.setCalled(true);
					exploreOperation(calledOperation);
					operationCall.setCalled(false);

					if (false == calledOperation.getReturnType().equals("void")
							&& false == calledOperation.getReturnType().equals("")) {
						sb.append(operation.getOwnerClassQualifiedName());
						sb.append(" <-- ");
						sb.append(operationCall.getOwnerClassQualifiedName());
						sb.append(" : ");
						sb.append("return: ");
						// sb.append(calledOperation.getReturnType());
						sb.append(TypeUtil.getSimpleTypeName(calledOperation.getReturnType()));
						sb.append("\n");
					}
				}
			} else if (operationCall.isConstructor()) { // Override 없는 생성자를 호출하는 경우
				// 클래스 내부 생성자를 호출하는 경우
				if (operation.getOwnerClassQualifiedName().equals(operationCall.getOwnerClassQualifiedName())) {
					sb.append(operation.getOwnerClassQualifiedName());
					sb.append(" -> ");
					sb.append(operationCall.getOwnerClassQualifiedName());
					sb.append(" : ");
					sb.append(" <<Create>>");
					sb.append("\n");
				} else { // 다른 클래스의 생성자를 호출하는 경우
					sb.append(operation.getOwnerClassQualifiedName());
					sb.append(" -> ");
					sb.append(operationCall.getOwnerClassQualifiedName());
					sb.append(" : ");
					sb.append(" <<Create>>");
					sb.append("\n");
				}
			}
		} // for - ocqn
	}

	public String getScript() {
		return sb.toString();
	}

}
