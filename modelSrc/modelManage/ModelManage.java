package modelManage;


import org.eclipse.emf.common.util.EList;

import SWVS.Project;
import xmiManage.XmiReader;

public class ModelManage 
{
	XmiReader xmiReader;
	Project project;
	EList<SWVS.Class> classList;
	EList<SWVS.System> systemList;
	EList<SWVS.UseCase> usecaseList;
	
	public ModelManage()
	{
		
	}
	public static void main(String args[])
	{
		XmiReader.main("Dice.SWVS");
	}
	public void readXmi(String filePath)
	{
		xmiReader = new XmiReader(filePath);
	}
	
	public void readModels()
	{
		project = xmiReader.getProject();
		classList = project.getClass_();
		systemList = project.getSystem();
		
	}
	public SWVS.Class findClassWithSrc(String src)
	{
		for(SWVS.Class target : classList)
		{
			if(target.getData_base_SrcName().equals(src) )
				return target;
		}
		return null;
	}
	public SWVS.Object findFunction(SWVS.Class cls, String src)
	{
		
		for(SWVS.Object fun : cls.getMethod())
		{
			if ( fun.getData_base_SrcName().equals(src) )
				return fun;
		}
		return null;
	}
	public SWVS.UseCase findUseCase(SWVS.System system, String id)
	{
		for( SWVS.UseCase usecase : system.getUsecase() )
		{
			if(usecase.getId().equals(id))
				return usecase;
		}
		return null;
	}

}
