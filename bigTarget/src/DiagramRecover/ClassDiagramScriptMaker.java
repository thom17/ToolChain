package dev.khe.DiagramRecover;

import java.util.List;

import dev.khe.DiagramRecover.model.MClass;
import dev.khe.DiagramRecover.model.MMember;
import dev.khe.DiagramRecover.model.MOperation;
import dev.khe.DiagramRecover.model.ModelManager;

public class ClassDiagramScriptMaker {

	private ModelManager manager = null;

	private StringBuilder sb = null;

	public ClassDiagramScriptMaker(ModelManager manager) {
		this.manager = manager;
		this.sb = new StringBuilder();
	}

	/**
	 * 클래스 선언 시 Abstract 여부를 확인하여 PlantUML 코드값을 반환한다.<br>
	 * Abstract가 아닐 경우 문자열 ""를 반환한다.
	 * 
	 * @param modifierList
	 * @return
	 */
	private String getClassAbstractCode(List<String> modifierList) {
		for (String modifier : modifierList) {
			if (modifier.contains("abstract")) {
				return "abstract ";
			}
		}

		return "";
	}

	/**
	 * 변수 선언 또는 메소드 선언의 Modifier에 따라 PlantUML에서 사용되는 코드값을 반환한다.<br>
	 * 아래 Modifier 중 해당되는 항목이 없을 경우 문자열 ""를 반환한다.<br>
	 * private = -<br>
	 * protected = #<br>
	 * public = +<br>
	 * abstract = {abstract}<br>
	 * static = {static}<br>
	 * 
	 * @param modifierList
	 * @return
	 */
	private String getModifierCode(List<String> modifierList) {
		String codeStr = "";

		for (String modifier : modifierList) {
			if (modifier.contains("private")) {
				codeStr += "-";
			} else if (modifier.contains("protected")) {
				codeStr += "#";
			} else if (modifier.contains("public")) {
				codeStr += "+";
			} else if (modifier.contains("abstract")) {
				codeStr += "{abstract}";
			} else if (modifier.contains("static")) {
				codeStr += "{static}";
			}
		}

		return codeStr;
	}

	public void makeScript() {
		sb = new StringBuilder();
		sb.append("@startuml");
		sb.append("\n");

		MOperation operation = null;
		for (MClass _class : manager.get_ClassList()) {
			// 클래스 정보 출력1(상속관계)
			if (null != _class.getSuperClassQualifiedName()) {
				sb.append(_class.getSuperClassQualifiedName());
				sb.append(" <|-- ");
				sb.append(_class.getQualifiedName());
				sb.append("\n");
			}
			for (String siqn : _class.getSuperInterfaceQualifiedNameList()) {
				sb.append(siqn);
				sb.append(" <|-- ");
				sb.append(_class.getQualifiedName());
				sb.append("\n");
			}

			// 클래스 정보 출력2(연관관계)
			for (MMember member : _class.getMemberList()) {
				if (null != manager.get_Class(member.getTypeName())) {
					sb.append(member.getTypeName());
					sb.append(" <-- ");
					sb.append(_class.getQualifiedName());
					sb.append(" : ");
					sb.append(member.getName());
					sb.append("\n");
				}
			}

			// 클래스 정보 출력3
			sb.append(getClassAbstractCode(_class.getModifierList()));
			if (_class.isInterface()) {
				sb.append("interface ");
			} else {
				sb.append("class ");
			}
			sb.append(_class.getQualifiedName());
			sb.append(" {");
			sb.append("\n");

			// 멤버 변수 정보 출력
			for (MMember member : _class.getMemberList()) {
				if (null == manager.get_Class(member.getTypeName())) {
					sb.append("\t");

					sb.append(getModifierCode(member.getModifierList()));
					if (member.getTypeName().contains(".")) {
						sb.append(member.getTypeName().substring(member.getTypeName().lastIndexOf(".") + 1,
								member.getTypeName().length()));
					} else {
						sb.append(member.getTypeName());
					}

					sb.append(" ");
					sb.append(member.getName());
					sb.append("\n");
				}
			}

			// 메소드 정보 출력
			for (String oqn : _class.getOperationQualifiedNameList()) {
				operation = manager.getOperation(oqn);
				sb.append("\t");

				sb.append(getModifierCode(operation.getModifierList()));
				if (operation.getReturnType().contains(".")) {
					sb.append(operation.getReturnType().substring(operation.getReturnType().lastIndexOf(".") + 1,
							operation.getReturnType().length()));
				} else {
					sb.append(operation.getReturnType());
				}

				sb.append(" ");
				sb.append(operation.getName());
				sb.append(StringUtil.toString(TypeUtil.getSimpleTypeNameList(operation.getArgumentTypeList()), "(",
						", ", ")"));
				sb.append("\n");
			} // for - oqn

			sb.append("}");
			sb.append("\n");
		} // for - _class

		sb.append("@enduml");
		sb.append("\n");
	}

	public String getScript() {
		return sb.toString();
	}

}