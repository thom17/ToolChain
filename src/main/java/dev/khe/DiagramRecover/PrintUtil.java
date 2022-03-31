package dev.khe.DiagramRecover;

public class PrintUtil {
	
	private static final String TAB = "\t";
	
	private static final String INFORMATION_TAG = "<INFO>";
	
	private static final String WARNING_TAG = "<WARNING>";
	
	private static final String ERROR_TAG = "<ERROR>";

	public static void printlnInfomation(String str) {
		System.out.print(INFORMATION_TAG);
		System.out.print(TAB);
		System.out.println(str);
	}

	public static void printlnWarning(String str) {
		System.out.print(WARNING_TAG);
		System.out.print(TAB);
		System.out.println(str);
	}

	public static void printlnError(String str) {
		System.out.print(ERROR_TAG);
		System.out.print(TAB);
		System.out.println(str);
	}
	
	public static void printlnAddedTab(int depth, String str) {
		String tabStr = "";
		for (int i = 0; i < depth; i++) {
			tabStr += TAB;
		}
		System.out.println(tabStr + str.replaceAll("\n", "\n" + tabStr));
	}
	
}
