package jadx.utils;

public class StringUtils {

	public static String unescapeString(String str) {
		int len = str.length();
		StringBuilder res = new StringBuilder();

		for (int i = 0; i < len; i++) {
			int c = str.charAt(i) & 0xFFFF;
			processChar(c, res);
		}
		return '"' + res.toString() + '"';
	}

	public static String unescapeChar(char ch) {
		if (ch == '\'')
			return "'\\\''";

		StringBuilder res = new StringBuilder();
		processChar(ch, res);
		return '\'' + res.toString() + '\'';
	}

	public static void processChar(int c, StringBuilder res) {
		switch (c) {
			case '\n':
				res.append("\\n");
				break;
			case '\r':
				res.append("\\r");
				break;
			case '\t':
				res.append("\\t");
				break;
			case '\b':
				res.append("\\b");
				break;
			case '\f':
				res.append("\\f");
				break;
			case '\'':
				res.append('\'');
				break;
			case '"':
				res.append("\\\"");
				break;
			case '\\':
				res.append("\\\\");
				break;

			default:
				if (32 <= c && c <= 126) {
					res.append((char) c);
				} else {
					res.append("\\u").append(String.format("%04x", c));
				}
				break;
		}
	}
}
