package dataSet.statement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;

public class SwitchNode extends StateNode
{
	Map<String, ArrayList<Statement> > caseMap = new LinkedHashMap();
	String target;
	public SwitchNode(SwitchStmt node)
	{
		ArrayList<Statement> stmtList = null;
		NodeList<SwitchEntry> entries = node.getEntries();
		String key = null;
		target = node.getSelector().toString();
		for(SwitchEntry swe : entries)
		{
			
			System.out.println("type : "+swe.getType());
			System.out.println("labels : "+swe.getLabels());
			System.out.println("stmt : "+swe.getStatements());
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

