package server.pcs.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {
	private static final String FOLDER_SEPARATOR = "/";

	private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

	private static final String TOP_PATH = "..";

	private static final String CURRENT_PATH = ".";

	public static Boolean isEmpty(String data) {
		if (data == null || data.length() == 0) {
			return true;
		}
		return false;
	}

	public static String stringReverse(String src) {
		return new StringBuilder(src).reverse().toString();
	}

	public static String cleanPath(String path) {
		if (path == null) {
			return null;
		}
		String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);

		// Strip prefix from path to analyze, to not treat it as part of the
		// first path element. This is necessary to correctly parse paths like
		// "file:core/../core/io/Resource.class", where the ".." should just
		// strip the first "core" directory while keeping the "file:" prefix.
		int prefixIndex = pathToUse.indexOf(":");
		String prefix = "";
		if (prefixIndex != -1) {
			prefix = pathToUse.substring(0, prefixIndex + 1);
			if (prefix.contains("/")) {
				prefix = "";
			} else {
				pathToUse = pathToUse.substring(prefixIndex + 1);
			}
		}
		if (pathToUse.startsWith(FOLDER_SEPARATOR)) {
			prefix = prefix + FOLDER_SEPARATOR;
			pathToUse = pathToUse.substring(1);
		}

		String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
		List<String> pathElements = new LinkedList<String>();
		int tops = 0;

		for (int i = pathArray.length - 1; i >= 0; i--) {
			String element = pathArray[i];
			if (CURRENT_PATH.equals(element)) {
				// Points to current directory - drop it.
			} else if (TOP_PATH.equals(element)) {
				// Registering top path found.
				tops++;
			} else {
				if (tops > 0) {
					// Merging path element with element corresponding to top
					// path.
					tops--;
				} else {
					// Normal path element found.
					pathElements.add(0, element);
				}
			}
		}

		// Remaining top paths need to be retained.
		for (int i = 0; i < tops; i++) {
			pathElements.add(0, TOP_PATH);
		}

		String res = prefix + collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
		if (res.startsWith("//"))
			return res.replaceFirst("//", "/");
		return res;
	}

	public static String replace(String inString, String oldPattern, String newPattern) {
		if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
			return inString;
		}
		int index = inString.indexOf(oldPattern);
		if (index == -1) {
			// no occurrence -> can return input as-is
			return inString;
		}

		int capacity = inString.length();
		if (newPattern.length() > oldPattern.length()) {
			capacity += 16;
		}
		StringBuilder sb = new StringBuilder(capacity);

		int pos = 0; // our position in the old string
		int patLen = oldPattern.length();
		while (index >= 0) {
			sb.append(inString.substring(pos, index));
			sb.append(newPattern);
			pos = index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}

		// append any characters to the right of a match
		sb.append(inString.substring(pos));
		return sb.toString();
	}

	public static boolean hasLength(String str) {
		return (str != null && !str.isEmpty());
	}

	public static String[] delimitedListToStringArray(String str, String delimiter) {
		return delimitedListToStringArray(str, delimiter, null);
	}

	public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
		if (str == null) {
			return new String[0];
		}
		if (delimiter == null) {
			return new String[] { str };
		}

		List<String> result = new ArrayList<String>();
		if ("".equals(delimiter)) {
			for (int i = 0; i < str.length(); i++) {
				result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
			}
		} else {
			int pos = 0;
			int delPos;
			while ((delPos = str.indexOf(delimiter, pos)) != -1) {
				result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
				pos = delPos + delimiter.length();
			}
			if (str.length() > 0 && pos <= str.length()) {
				// Add rest of String, but not in case of empty input.
				result.add(deleteAny(str.substring(pos), charsToDelete));
			}
		}
		return toStringArray(result);
	}

	public static String deleteAny(String inString, String charsToDelete) {
		if (!hasLength(inString) || !hasLength(charsToDelete)) {
			return inString;
		}

		StringBuilder sb = new StringBuilder(inString.length());
		for (int i = 0; i < inString.length(); i++) {
			char c = inString.charAt(i);
			if (charsToDelete.indexOf(c) == -1) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String[] toStringArray(Collection<String> collection) {
		if (collection == null) {
			return null;
		}

		return collection.toArray(new String[collection.size()]);
	}

	public static String collectionToDelimitedString(Collection<?> coll, String delim) {
		return collectionToDelimitedString(coll, delim, "", "");
	}

	public static String collectionToDelimitedString(Collection<?> coll, String delim, String prefix, String suffix) {
		if (coll == null || coll.size() < 1) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Iterator<?> it = coll.iterator();
		while (it.hasNext()) {
			sb.append(prefix).append(it.next()).append(suffix);
			if (it.hasNext()) {
				sb.append(delim);
			}
		}
		return sb.toString();
	}

	public static ArrayList<String> 解析指定️字符串_by正则表达式(String s指定️字符串, String s正则表达式) {
	      Pattern pattern = Pattern.compile(s正则表达式, Pattern.MULTILINE);
	      Matcher matcher = pattern.matcher(s指定️字符串);
	      ArrayList<String> matchList = new ArrayList<String>();

	      while (matcher.find()) {
	        matchList.add(matcher.group());
	      }
		return matchList;
	}

	public static Map<String, String> 将ArrayStr转成Map(String[] a, String string) {
		/**
			将以下类型的数组转成Map
			[token=39aa2r, u=https%, adapter=, skin=default_v2, tpl=wi, clientfrom=, client=, secstate=]
			to
			Map<key, value>
				token,  39aa2r
				u,      https%
				adapter,
				skin,
				tpl,
				clientfrom,
				client,
				secstate,
		 */
		if(a == null || a.length==0) return null;
		if(StringUtils.isEmpty(string)) return null;

		Map<String, String> c = new HashMap<String, String>();
		for(String s : a) {
			String[] b = s.split(string);
			c.put(b[0], b.length>1?b[1]:null);
		}
		return c;
	}
	/**
	 * 将字符串中的"\n"换成"\b"
	 * @param l
	 * @return
	 * 问题：Java /n 无法替换
	 * 方法一（无效
	 *   在去除字符串中的换行符(\n)的时候，写成str.replace("\\n", "")才能正确执行。
	 *   str.replace("\n","") ，str.replaceAll("\\n","")，str.replaceAll("\n","")均替换失败。
	 * 方法二（无效
	 *   java str replace /n
	 * 方法三（有效
	 *   自己写函数解决
	 *   \n是换行，ASCLL码是10
	 */
	public static String 替换指定Str换行符(String l, char fromChar, char toChar) {
		char[] s = l.toCharArray();
		int iIndex = 0;
		for(char a : s) {
			if(a == fromChar) {
				System.out.println(a);
				s[iIndex] = toChar;
			}
			iIndex++;
		}
		return new String(s);
	}

	public static String 清理取到的Str(String a1) {
		a1 = StringUtil.替换指定Str换行符(a1, '\t', ' ');
		a1 = StringUtil.替换指定Str换行符(a1, '\r', ' ');
		a1 = StringUtil.替换指定Str换行符(a1, '\'', ' ');
		a1 = a1.replace(",", "");
		a1 = a1.trim();
		return a1;
	}
}
