package dev.khe.DiagramRecover.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelManager {

	/**
	 * Key: classQualifiedName String<br>
	 * Value: MClass Object
	 */
	private Map<String, MClass> classMap = null;

	/**
	 * Key: operationQualifiedName String<br>
	 * Value: MOperation Object
	 */
	private Map<String, MOperation> operationMap = null;

	/**
	 * Key: operationCallQualifiedName String<br>
	 * Value: MOperationCall Object
	 */
	private Map<String, MOperationCall> operationCallMap = null;

	public ModelManager() {
		initialize();
	}

	public void initialize() {
		classMap = new HashMap<String, MClass>();
		operationMap = new HashMap<String, MOperation>();
		operationCallMap = new HashMap<String, MOperationCall>();
	}

	public List<MClass> get_ClassList() {
		return new ArrayList<MClass>(classMap.values());
	}

	public MClass get_Class(String classQualifiedName) {
		return classMap.get(classQualifiedName);
	}

	public void add_Class(MClass _class) {
		classMap.put(_class.getQualifiedName(), _class);
	}

	public MOperation getOperation(String operationQualifiedName) {
		return operationMap.get(operationQualifiedName);
	}

	public void addOperation(MOperation operation) {
		operationMap.put(operation.getQualifiedName(), operation);
		classMap.get(operation.getOwnerClassQualifiedName()).getOperationQualifiedNameList()
				.add(operation.getQualifiedName());
	}

	public MOperationCall getOperationCall(String operationCallQualifiedName) {
		return operationCallMap.get(operationCallQualifiedName);
	}

	/**
	 * 인자로 주어진 operationCall을 operationCallMap에 추가한다.<br>
	 * 또한 코드 상에서 operationCall이 위치한 메소드(MOperation)를 검색하여<br>
	 * 해당하는 MOperation 객체의 getOprationCallQualifiedNameList에<br>
	 * 목록을 추가한다.
	 * 
	 * @param operationCall
	 */
	public void addOperationCall(MOperationCall operationCall) {
		operationCallMap.put(operationCall.getQualifiedName(), operationCall);
		operationMap.get(operationCall.getBelongingOperationQualifiedName()).getOprationCallQualifiedNameList()
				.add(operationCall.getQualifiedName());
	}
	
	/**
	 * 인자로 주어진 member를 소유하는 MClass 객체에 추가한다.
	 * @param member
	 */
	public void addMember(MMember member) {
		classMap.get(member.getOwnerClassQualifiedName()).addMember(member);
	}

}
