package dev.khe.DiagramRecover;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.resolution.types.ResolvedType;

//resovleType 객체를 해석하는 클래스
public class TypeUtil {

	public static String getTypeStr(ResolvedType resolvedType) {
		if (resolvedType.isArray()) {
			return getTypeStr(resolvedType.asArrayType().getComponentType()) + "[]";
		} else if (resolvedType.isPrimitive()) {
			return resolvedType.asPrimitive().getBoxTypeQName();
		} else if (resolvedType.isReferenceType()) {
			return resolvedType.asReferenceType().getQualifiedName();
		} else if (resolvedType.isVoid()) {
			return "void";
		}

		return resolvedType.toString();
	}

	public static String getSimpleTypeName(String qualifiedTypeName) {
		return qualifiedTypeName.substring(qualifiedTypeName.lastIndexOf(".") + 1, qualifiedTypeName.length());
	}
	
	public static List<String> getSimpleTypeNameList(List<String> qualifiedTypeNameList) {
		List<String> list = new ArrayList<String>();
		
		for (String qtn : qualifiedTypeNameList) {
			list.add(getSimpleTypeName(qtn));
		}
		
		return list;
	}

}
