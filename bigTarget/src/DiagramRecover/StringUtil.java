package dev.khe.DiagramRecover;

import java.util.List;

public class StringUtil {

	public static String toString(List<String> list, String delimiter) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < list.size() - 1; i++) {
			sb.append(list.get(i));
			sb.append(delimiter);
		}
		if (list.size() >= 1) {
			sb.append(list.get(list.size() - 1));
		}

		return sb.toString();
	}

	public static String toString(List<String> list, String prefix, String delimiter, String postfix) {
		return prefix + toString(list, delimiter) + postfix;
	}

}
