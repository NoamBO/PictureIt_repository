package utilities.view;

import utilities.PixelsConverter;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.pictureit.noambaroz.beautyapp.R;

public class MyNumberPicker extends LinearLayout {

	public interface onValueChangeListener {
		public void onValueChange(int value);
	}

	private onValueChangeListener mListener;

	private final long REPEAT_DELAY = 50;

	private final int ELEMENT_HEIGHT = 25;
	private final int ELEMENT_WIDTH = ELEMENT_HEIGHT;

	private final int EDIT_TEXT_HEIGHT = ELEMENT_HEIGHT + 10;
	private final int EDIT_TEXT_WIDTH = ELEMENT_HEIGHT + 13;

	private final int textSize = 12;

	private final int MINIMUM = 0;
	private final int MAXIMUM = 10;

	public Integer value;

	Button decrement;
	Button increment;
	public EditText valueText;

	private Handler repeatUpdateHandler = new Handler();

	private boolean autoIncrement = false;
	private boolean autoDecrement = false;

	class RepetetiveUpdater implements Runnable {
		public void run() {
			if (autoIncrement) {
				increment();
				repeatUpdateHandler.postDelayed(new RepetetiveUpdater(), REPEAT_DELAY);
			} else if (autoDecrement) {
				decrement();
				repeatUpdateHandler.postDelayed(new RepetetiveUpdater(), REPEAT_DELAY);
			}
		}
	}

	public MyNumberPicker(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);

		this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		this.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

		LayoutParams elementParams = new LinearLayout.LayoutParams((int) PixelsConverter.convertDpToPixel(
				ELEMENT_WIDTH, context), (int) PixelsConverter.convertDpToPixel(ELEMENT_HEIGHT, context));
		LayoutParams editTextParams = new LinearLayout.LayoutParams((int) PixelsConverter.convertDpToPixel(
				EDIT_TEXT_WIDTH, context), (int) PixelsConverter.convertDpToPixel(EDIT_TEXT_HEIGHT, context));

		// init the individual elements
		initDecrementButton(context);
		initValueEditText(context);
		initIncrementButton(context);

		// Can be configured to be vertical or horizontal
		// Thanks for the help, LinearLayout!
		if (getOrientation() == VERTICAL) {
			addView(increment, elementParams);
			addView(valueText, editTextParams);
			addView(decrement, elementParams);
		} else {
			addView(decrement, elementParams);
			addView(valueText, editTextParams);
			addView(increment, elementParams);
		}
		valueText.setTextColor(context.getResources().getColor(android.R.color.black));
	}

	private void initIncrementButton(Context context) {
		increment = new Button(context);
		// increment.setTextSize(textSize);
		// increment.setTypeface(null, Typeface.BOLD);
		// increment.setTextColor(getContext().getResources().getColor(R.color.app_background));
		// increment.setText("+");
		// increment.setBackgroundColor(getContext().getResources().getColor(R.color.app_most_common_yellow_color));
		increment.setBackgroundResource(R.drawable.ic_plus);

		// Increment once for a click
		increment.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				increment();
			}
		});

		// // Auto increment for a long click
		// increment.setOnLongClickListener(new View.OnLongClickListener() {
		// public boolean onLongClick(View arg0) {
		// autoIncrement = true;
		// repeatUpdateHandler.post(new RepetetiveUpdater());
		// return false;
		// }
		// });
		//
		// // When the button is released, if we're auto incrementing, stop
		// increment.setOnTouchListener(new View.OnTouchListener() {
		// public boolean onTouch(View v, MotionEvent event) {
		// if (event.getAction() == MotionEvent.ACTION_UP && autoIncrement) {
		// autoIncrement = false;
		// }
		// return false;
		// }
		// });
	}

	private void initValueEditText(Context context) {

		value = Integer.valueOf(0);

		valueText = new EditText(context);
		valueText.setTextSize(textSize);
		valueText.setBackgroundColor(Color.parseColor("#00000000"));

		// Since we're a number that gets affected by the button, we need to be
		// ready to change the numeric value with a simple ++/--, so whenever
		// the value is changed with a keyboard, convert that text value to a
		// number. We can set the text area to only allow numeric input, but
		// even so, a carriage return can get hacked through. To prevent this
		// little quirk from causing a crash, store the value of the internal
		// number before attempting to parse the changed value in the text area
		// so we can revert to that in case the text change causes an invalid
		// number

		// valueText.setOnKeyListener(new View.OnKeyListener() {
		// public boolean onKey(View v, int arg1, KeyEvent event) {
		// int backupValue = value;
		// try {
		// value = Integer.parseInt( ((EditText)v).getText().toString() );
		// } catch( NumberFormatException nfe ){
		// value = backupValue;
		// }
		// return false;
		// }
		// });
		valueText.setEnabled(false);

		// Highlight the number when we get focus
		valueText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					((EditText) v).selectAll();
				}
			}
		});
		valueText.setGravity(Gravity.CENTER);
		valueText.setText(value.toString());
		valueText.setInputType(InputType.TYPE_CLASS_NUMBER);
	}

	private void initDecrementButton(Context context) {
		decrement = new Button(context);
		// decrement.setTextSize(textSize);
		// decrement.setText("-");
		// decrement.setTypeface(null, Typeface.BOLD);
		// decrement.setTextColor(getContext().getResources().getColor(R.color.app_background));
		// decrement.setBackgroundColor(getContext().getResources().getColor(R.color.app_most_common_yellow_color));
		// decrement.setGravity(Gravity.CENTER);
		decrement.setBackgroundResource(R.drawable.ic_minus);

		// Decrement once for a click
		decrement.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				decrement();
			}
		});

		// // Auto Decrement for a long click
		// decrement.setOnLongClickListener(new View.OnLongClickListener() {
		// public boolean onLongClick(View arg0) {
		// autoDecrement = true;
		// repeatUpdateHandler.post(new RepetetiveUpdater());
		// return false;
		// }
		// });
		//
		// // When the button is released, if we're auto decrementing, stop
		// decrement.setOnTouchListener(new View.OnTouchListener() {
		// public boolean onTouch(View v, MotionEvent event) {
		// if (event.getAction() == MotionEvent.ACTION_UP && autoDecrement) {
		// autoDecrement = false;
		// }
		// return false;
		// }
		// });
	}

	public void increment() {
		if (value < MAXIMUM) {
			value = value + 1;
			valueText.setText(value.toString());
			valueChange();
		}
	}

	public void decrement() {
		if (value > MINIMUM) {
			value = value - 1;
			valueText.setText(value.toString());
			valueChange();
		}
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		if (value > MAXIMUM)
			value = MAXIMUM;
		if (value >= 0) {
			this.value = value;
			valueText.setText(this.value.toString());
		}
	}

	public void addOnValueChangeListener(onValueChangeListener listener) {
		this.mListener = listener;
	}

	private void valueChange() {
		if (mListener != null)
			if (!autoDecrement && !autoIncrement)
				mListener.onValueChange(value);
	}

}