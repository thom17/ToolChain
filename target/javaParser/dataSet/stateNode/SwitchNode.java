package dataSet.stateNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;

public class SwitchNode extends StateNode
{
	Map<String, ArrayList<Statement> > caseMap = new LinkedHashMap();
	String target;
	public SwitchNode(SwitchStatement node)
	{
		ArrayList<Statement> stmtList = null;
		String key = null;
		target = node.getExpression().toString();
		for(Object stmt : node.statements())
		{
			if(stmt instanceof SwitchCase)
			{
				if(stmtList != null)
					caseMap.put(key, stmtList);
	
				key = ((SwitchCase) stmt).toString();
				stmtList = new ArrayList<Statement>();
			}else
			{
				stmtList.add((Statement)stmt);
			}
			
		}
	}
	public Map<String, ArrayList<Statement> > getCaseMap()
	{
		return this.caseMap;
	}
	public String getTarget() {return this.target;}
	public void print()
	{
		System.out.println("switch "+target);
		for(String key : caseMap.keySet() )
		{
			System.out.println("case - "+key);
			for(Statement stmt : caseMap.get(key)) 
			{
				System.out.println("\t"+stmt.toString());
			}
		}
	}
}

