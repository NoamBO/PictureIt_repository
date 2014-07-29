package utilities.view;

public class AutoResizeTextViewSetter {

	public static void setText(AutoResizeTextView textView, String text) {
		final String DOUBLE_BYTE_SPACE = "\u3000";
		String fixString = "";
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1
		   && android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {  
		    fixString = DOUBLE_BYTE_SPACE;
		}
		textView.setText(fixString + text + fixString);
	}
	
	public static String elipsizeText(int maxLenth, String string) {
		String[] charSecuence = string.split("");
		if(charSecuence.length > maxLenth) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < maxLenth; i++) {
				sb.append(charSecuence[i]);
			}
			string = sb.toString()+"..";
		}
		return string;
	}
}
