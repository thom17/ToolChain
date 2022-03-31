package dataSet.statement;

import dataSet.Function;
import dataSet.Member;

public class MethodCallStateNode extends StateNode
{
	Function client;
	Function host;
	Member parameter[];
	Member caller;
	
	public void setFuns(Function client, Function host)
	{
		this.client = client;
		this.host = host;
	}
	
	public void setParm(Member parameters[])
	{
		this.parameter = parameters;
	}
	public void setCaller(Member caller)
	{
		this.caller = caller;
	}
	public Member getCaller()
	{
		return caller;
	}
	public Function getClient()
	{
		return client;
	}

	public Function getHost() {
		// TODO Auto-generated method stub
		return host;
	}
}
