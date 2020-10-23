package rcptask.viewpac.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExp {
	private static final Pattern NAME_PATTERN = Pattern.compile("^([a-zA-Zа-яА-ЯёЁ]+\\s?)+$");
	private static final Pattern GROUP_PATTERN = Pattern.compile("^[1-9]+0*[0-9]*$");
	private static final Pattern ADRESS_PATTERN = Pattern.compile("^([a-zA-Zа-яА-ЯёЁ0-9]+,?\\s?)+$");
	private static final Pattern CITY_PATTERN = Pattern.compile("^([a-zA-Zа-яА-ЯёЁ\\-]+\\s?\\-?)+$");
	private static final Pattern RESULT_PATTERN = Pattern.compile("^[1-5]$");

	public static boolean isNameValid(String input) {
		Matcher nameMatcher = NAME_PATTERN.matcher(input);
		return nameMatcher.matches();
	}

	public static boolean isGroupValid(String input) {
		Matcher groupMatcher = GROUP_PATTERN.matcher(input);
		return groupMatcher.matches();
	}
	
	public static boolean isAdressValid(String input) {
		Matcher adressMatcher = ADRESS_PATTERN.matcher(input);
		return adressMatcher.matches();
	}
	
	public static boolean isCityValid(String input) {
		Matcher citypMatcher = CITY_PATTERN.matcher(input);
		return citypMatcher.matches();
	}
	
	public static boolean isResultValid(String input) {
		Matcher resultMatcher = RESULT_PATTERN.matcher(input);
		return resultMatcher.matches();
	}
}
