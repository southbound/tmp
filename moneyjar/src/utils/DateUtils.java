package utils;
import java.text.DecimalFormat;
public class DateUtils {
	public static String StrToDate(String date_6digit) {
		String year = "20" + date_6digit.substring(0, 2);
		String month = date_6digit.substring(2, 4);
		String day = date_6digit.substring(4, 6);

		String result = year + "年" + month + "月" + day + "日";

		return result;
   }
	public static String StrToMonth(String month_4digit) {
		String year = "20" + month_4digit.substring(0, 2);
		String month = month_4digit.substring(2, 4);

		String result = year + "年" + month + "月";

		return result;
   }
	public static String StrToDecimal(String money) {
		DecimalFormat formatter = new DecimalFormat("#,###");
		int money_int = Integer.parseInt(money);
		String result = formatter.format(money_int);
		return result;
   }
	public static String StrToDecimal(long money_long) {
		DecimalFormat formatter = new DecimalFormat("#,###");
		int money_int = (int)money_long;
		String result = formatter.format(money_int);
		return result;
   }
}