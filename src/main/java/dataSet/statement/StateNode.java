package dataSet.statement;

public class StateNode {
	StmtType type = StmtType.point;
	String code;
	String nextCheck;
	String key;
	StateNode thenNode;
	StateNode elseNode;
	StateNode before;
	StateNodeList insideNode;

	public StateNode()
	{
		code="[*]";
		key = "EndNode";
		nextCheck = "true";
		type =  StateNode.getType(key);
	}
	public StateNode(String code, String nextCheck)
	{
		this.code = code;
		this.nextCheck = nextCheck;	
		
	}
	public StateNode(String code, String nextCheck, String key)
	{
		this.code = code;
		this.nextCheck = nextCheck;
		this.key = key;
		type =  StateNode.getType(key);
	}
	public void init(String code, String nextCheck) 
	{
		this.code=code;
		this.nextCheck = nextCheck;
	}
	public StateNode init(String code, String nextCheck, String key) 
	{
		this.code=code;
		this.nextCheck = nextCheck;
		this.key = key;
		this.type = StateNode.getType(key);
		return this;
	}
	/**
	 * deleter.thisMethod(replace) replace로 deleter 대체 
	 * @param code 
	 * @param nextCheck
	 * @param key
	 * @param stateNode thi.then
	 * @return
	 */
	public StateNode init(String code, String nextCheck, String key, StateNode stateNode) 
	{
		stateNode.init(code, nextCheck, key);
		//StateNode before =  this.before;
		
		if( before.getThen() == this )
			before.setThen(stateNode);
		else 
			before.setElse(stateNode);
		
		return stateNode;
	}
	private static StmtType getType(String key)
	{
		if(key.contains("parameter"))
			return StmtType.returnStmt;
		else if (key.contains("varDef"))
			return StmtType.varDef;
		else if (key.contains("fieldAcces"))
			return StmtType.fieldAccess;
		else if(key.contains("_change"))
			return StmtType.assignExpr;
		else if(key.contains("return"))
			return StmtType.returnStmt;
		else if(key.contains("_call"))
			return StmtType.methodCallExpr;
		else if(key.contains("if"))
			return StmtType.ifStmt;
		else if(key.contains("forStmt")|| key.contains("whileStmt"))
			return StmtType.repeatStmt;
		else
			return StmtType.point;
	}
	public StmtType getType() {return type; }
	public StateNode getThen() { return thenNode; }
	public StateNode getElse() { return elseNode; }
	
	public void setBefore(StateNode before) { this.before = before;}
	
	public StateNode setThen(StateNode node)
	{
		this.thenNode = node;
		node.setBefore( this );
		return node;
	}
	public StateNode setElse(StateNode node)
	{
		this.elseNode = node;
		node.before = this;
		return node;
	}

	public String getCondition() { return nextCheck; }
	public String getKey() {return this.key; }
	public String getCode() {return code;}
	
	public String getObjectKey()
	{
		String list[] = this.toString().split("@");
		return "stateNode"+list[list.length-1];
	}
	public StateNode getBefore() {
		// TODO Auto-generated method stub
		return this.before;
	}
	
	public StateNodeList getInsideNode() {
		return insideNode;
	}
	public void setInsideNode(StateNodeList insideNode) {
		this.insideNode = insideNode;
	}
}
