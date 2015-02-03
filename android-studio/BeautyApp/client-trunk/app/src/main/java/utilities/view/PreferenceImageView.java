package utilities.view;

import utilities.PixelsConverter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pictureit.noambaroz.beautyapp.R;

public class PreferenceImageView extends Preference {

	private static final String MYSCHEME = "http://schemas.noambaroz.com/beutyapp";

	private final String TAG = getClass().getName();

	private String mImageInput;

	private ImageView mImageView;
	private int mImageWidth;
	private int mImageHeight;
	private int mGravity;

	private Drawable icon = null;
	private final int DEFULT_ICON_RES_ID = R.drawable.android_icon;

	public PreferenceImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPreference(context, attrs);
	}

	public PreferenceImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initPreference(context, attrs);
	}

	private void initPreference(Context context, AttributeSet attrs) {
		setValuesFromXml(context, attrs);
		mImageView = new ImageView(context);
		LayoutParams params = new LayoutParams(mImageWidth, mImageHeight);
		params.gravity = mGravity;
		mImageView.setLayoutParams(params);

		String s = getPersistedString("");
		if (!s.equalsIgnoreCase(""))
			mImageInput = s;
	}

	private void setValuesFromXml(Context context, AttributeSet attrs) {

		int tempImageHeight = (int) PixelsConverter.convertDpToPixel(150, context);
		int tempImageWidth = (int) PixelsConverter.convertDpToPixel(150, context);

		mImageHeight = attrs.getAttributeIntValue(MYSCHEME, "imageHeight", tempImageHeight);

		mImageWidth = attrs.getAttributeIntValue(MYSCHEME, "imageWidth", tempImageWidth);

		mGravity = attrs.getAttributeIntValue(MYSCHEME, "gravity", Gravity.CENTER);

		int res = attrs.getAttributeIntValue(MYSCHEME, "src", DEFULT_ICON_RES_ID);
		if (res != 0)
			icon = context.getResources().getDrawable(res);
	}

	@Override
	protected View onCreateView(ViewGroup parent) {
		RelativeLayout layout = null;

		try {
			LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			layout = (RelativeLayout) mInflater.inflate(R.layout.image_view_preference_layout, parent, false);
		} catch (Exception e) {
			Log.e(TAG, "Error creating image view preference", e);
		}

		return layout;
	}

	@Override
	public void onBindView(View view) {
		super.onBindView(view);

		Log.i("tagg", "onBindView");
		try {
			// move our imageView to the new view we've been given
			ViewParent oldContainer = mImageView.getParent();
			ViewGroup newContainer = (ViewGroup) view.findViewById(R.id.imageViewPrefBarContainer);

			if (oldContainer != newContainer) {
				// remove the imageView from the old view
				if (oldContainer != null) {
					((ViewGroup) oldContainer).removeView(mImageView);
				}
				// remove the existing imageView (there may not be one) and add
				// ours
				newContainer.removeAllViews();
				newContainer.addView(mImageView, ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
			}
		} catch (Exception ex) {
			Log.e(TAG, "Error binding view: " + ex.toString());
		}

		updateView(view);
	}

	/**
	 * Update a ImageViewPreference view with our current state
	 * 
	 * @param view
	 */
	protected void updateView(View view) {
		Log.i("tagg", "updateView");
		try {
			setImage();

		} catch (Exception e) {
			Log.e(TAG, "Error updating seek bar preference", e);
		}
	}

	private void setImage() {
		Log.i("tagg", "setImage");
		if (mImageInput != null) {
			mImageView.setImageBitmap(MyBitmapHelper.decodeBase64(mImageInput));
		} else if (icon != null) {
			mImageView.setImageDrawable(icon);
		} else {
			mImageView.setVisibility(View.GONE);
		}
	}

	public void setNewImage(Bitmap bitmap) {
		Log.i("tagg", "setNewImage");
		Bitmap bm = Bitmap.createScaledBitmap(bitmap, mImageWidth, mImageHeight, false);
		String bitString = MyBitmapHelper.encodeTobase64(bm);
		bm.recycle();
		if (!callChangeListener(bitString)) {
			setImage();
			return;
		}

		// change accepted, store it
		mImageInput = bitString;
		setImage();
		boolean is = persistString(bitString);
		Log.i("tagg", "setNewImage persisting = " + is);
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
		Log.i("tagg", "onSetInitialValue");
		if (restorePersistedValue) {
			mImageInput = getPersistedString(mImageInput);
		} else {
			String temp = "";
			try {
				temp = (String) defaultValue;
			} catch (Exception ex) {
				Log.e(TAG, "Invalid default value: " + defaultValue.toString());
			}

			persistString(temp);
			mImageInput = temp;
		}

	}

}
