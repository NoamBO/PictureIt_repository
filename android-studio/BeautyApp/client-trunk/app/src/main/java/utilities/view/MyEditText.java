package utilities.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class MyEditText extends EditText {

	public MyEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}

	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);

	}

	public MyEditText(Context context) {
		super(context);
		init(null);
	}

	private void init(AttributeSet attrs) {
		if (attrs != null) {
			Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/FbExtrim-Regular.ttf");
			setTypeface(myTypeface);
		}
	}
}
