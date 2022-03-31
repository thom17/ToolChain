package dataSet.stateNode;

public class StateNode {
	int id=0;			//해당 하는 node의 id값 (종류) varDef
	String code;		//해당 하는 node의 코드	name = "test"
	String nextCheck = "true";	//if문일시 조건 그 외 true
	String key;			//해당 하는 node의 종류	varDef_name
	StateNode thenNode;	//다음 노드 
	StateNode elseNode;	//else 문이 있는 if문만 초기화 그외 null
	StateNode before;	//이전 노드
	public StateNode(String code)
	{
		this.code = code;
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
	}
	public StateNode() {
		this.code="";
		this.key = "[*]";
	}
	
	public void setBefore(StateNode before)
	{
		this.before = before;
	}
	public StateNode init(String code, String check, String key)
	{
		this.code = code;
		this.nextCheck= check;
		this.key = key;
		return this;
	}
	public StateNode init(String code, String check, String key, StateNode target)
	{
		target.init(code, check, key);
		if(this.before.getThen() == this)
			this.before.setThen(target);
		else this.before.setElse(target);
		
		return target;
		
	}
	public StateNode getThen() { return thenNode; }
	public StateNode getElse() { return elseNode; }
	
	public StateNode setThen(StateNode node)
	{
		this.thenNode = node;
		node.setBefore(this);
		return node;
	}
	public StateNode setElse(StateNode node)
	{
		this.elseNode = node;
		node.setBefore(this);
		return node;
	}

	public StateNode getBefore() {return this.before; }
	public String getCondition() { return nextCheck; }
	public String getKey() {return this.key; }
	public String getCode() {return code;}
}
