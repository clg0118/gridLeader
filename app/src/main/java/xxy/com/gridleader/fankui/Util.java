package xxy.com.gridleader.fankui;

import android.app.AlertDialog;
import android.content.Context;
import android.hardware.Camera;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

	public static AlertDialog dialog;
	
	public static boolean isCanShow = true;
	// 保证该类不能被实例化
	private Util() {

	}

	// 获取当前时间
	public static String GetShiJian() {
		
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  String time=format.format(date);
		  return time;
	}
	
	
	
	
	// 获取月份
	public static int GetYue(String s) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int month = 0;
		try {
			// 用parse方法，可能会异常，所以要try-catch
			Date date = format.parse(s);
			// 获取日期实例
			Calendar calendar = Calendar.getInstance();
			// 将日历设置为指定的时间
			calendar.setTime(date);
			// 获取年
			int year = calendar.get(Calendar.YEAR);
			// 这里要注意，月份是从0开始。
			month = calendar.get(Calendar.MONTH);
			// 获取天
			int day = calendar.get(Calendar.DAY_OF_MONTH);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return month + 1;
	}

	public static String GetTime(String s) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = new Date(s);
		String t1 = format.format(d1);
		return t1;
	}

	// 密码判断
	public static boolean isPassWord(String ps) {

		Pattern pattern = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[a-zA-Z0-9]{6,20}");
		Matcher matcher = pattern.matcher(ps);
		return matcher.matches();

	}

	// 手机号码判断
	public static boolean isMobileNumber(String phoneNumber) {
		Pattern pattern = Pattern.compile("^(0|86|17951)?(13[0-9]|15[0-9]|17[0-9]|18[0-9]|14[0-9])[0-9]{8}$");
		Matcher matcher = pattern.matcher(phoneNumber);
		return matcher.matches();
	}

	// 判断是不是数字
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * @Method : isNumber
	 * @Description: 判断是否为金额
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		Pattern pattern = Pattern
				.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
		Matcher match = pattern.matcher(str);
		return match.matches();
	}

	// 强制转成double类型
	public static String doubleMoney(String str) {

		Double cny = Double.parseDouble(str);// 6.2041 这个是转为double类型
		DecimalFormat df = new DecimalFormat("0.00");
		String CNY = df.format(cny); // 6.20 这个是字符串，但已经是我要的两位小数了
		Double mdoubleMoney = Double.parseDouble(CNY); // 6.20
		String s1 = String.format("%.2f", mdoubleMoney);
		return s1;
	}

	// 获取相机权限
	@SuppressWarnings({ "deprecation", "finally" })
	public static boolean getCameraPermission(Context context) {

		boolean isCanUse = false;
		Camera mCamera = null;
		try {
			mCamera = Camera.open();
			Camera.Parameters mParameters = mCamera.getParameters();
			mCamera.setParameters(mParameters);
			isCanUse = true;
		} catch (Exception e) {
			isCanUse = false;
		} finally {
			// 释放相机，这个必须要，必须要，必须要！！！！
			if (mCamera != null) {
				try {
					mCamera.release();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return isCanUse;
		}
	}

	// 判断车牌第二位是否是数字
	public static boolean isShuZi(String s) {

		Pattern p = Pattern.compile("[0-9]*");
		Matcher m = p.matcher(s);
		if (m.matches()) {
			return true;
		}
		return false;

	}

	// 判断车牌中是否含有I或者O
	public static boolean isHaveIAndO(String s) {
		if (s.contains("I") || s.contains("O")) {
			return true;
		}
		return false;
	}


	
	// 判断是否是字母
	public static boolean isABC(String s) {
		Pattern p=Pattern.compile("[a-zA-Z]"); 
		Matcher m = p.matcher(s); 
		if(m.matches()){
			return true;
		}
		return false;
	}
	// 判断是否是汉字
		public static boolean isWord(String s) {
			Pattern p=Pattern.compile("[\u4e00-\u9fa5]"); 
			Matcher m = p.matcher(s); 
			if(m.matches()){
				return true;
			}
			return false;
		}
	// 根据时间分钟，拼接字符串
		public static String HourAndMinitus(String s) {
			String time = "";
			if(s==null||"".equals(s)){
				
			}else{
				int ss = Integer.parseInt(s);
				int h = ss/60;
				int m = ss%60;
				if(h>0){
					time = h+"小时";
				}
				if(m>0){
					time = time+m+"分钟";
				}
			}
			return time;
		}
		// 判断新能源车牌是否合法
		public static boolean isTrueCarNum(String s) {
			Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]{1}[A-Z]{1}[DF]{1}[A-Z0-9]{1}[0-9]{4}$");
			Matcher m = pattern.matcher(s);
			Pattern pattern1 = Pattern.compile("[\u4e00-\u9fa5]{1}[A-Z]{1}[0-9]{5}[DF]{1}$");
			Matcher m1 = pattern1.matcher(s);

			if (m.matches() || m1.matches()) {
				return true;
			}
			return false;
		}
		
		
//	    public static void ShowLoadingDialog(Context context, String msg) {
//	    	if(!isCanShow)
//	    		return;
//	    	dialog = new AlertDialog.Builder(context).create();
//			dialog.setCancelable(false);
//			dialog.show();
//			Window window = dialog.getWindow();
//			// 设置布局
//			window.setContentView(R.layout.activity_dialog);
//			// 设置宽高
//			window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
//			window.setGravity(Gravity.CENTER);
//			// 设置弹出的动画效果
//			window.setWindowAnimations(Gravity.CENTER);
//			isCanShow = false;
//		}
	    
//		public static void CancelLoadingDialog(Context context, String msg) {
//			dialog.cancel();
//			isCanShow = true;
//		}
		
}
