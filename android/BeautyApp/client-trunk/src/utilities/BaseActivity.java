package utilities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.util.LruCache;
import android.view.View;

import com.pictureit.noambaroz.beautyapp.R;

//Written by @Noam Bar-Oz

public class BaseActivity extends Activity {

	@SuppressWarnings("unchecked")
	protected <T extends View> T findView(int id) {
		return (T) findViewById(id);
	}

	@SuppressWarnings("unchecked")
	protected <T extends View> T findView(View rootView, int id) {
		return (T) rootView.findViewById(id);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			SpannableString s = new SpannableString(getTitle());
			s.setSpan(new TypefaceSpan(this, "FbExtrim-Regular.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			// Update the action bar title with the TypefaceSpan instance
			actionBar.setTitle(s);
		}
	}

	public static class TypefaceSpan extends MetricAffectingSpan {
		/** An <code>LruCache</code> for previously loaded typefaces. */
		private static LruCache<String, Typeface> sTypefaceCache = new LruCache<String, Typeface>(12);

		private Typeface mTypeface;

		private Context mContext;

		/**
		 * Load the {@link Typeface} and apply to a {@link Spannable}.
		 */
		public TypefaceSpan(Context context, String typefaceName) {
			mContext = context;
			mTypeface = sTypefaceCache.get(typefaceName);

			if (mTypeface == null) {
				mTypeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(),
						String.format("fonts/%s", typefaceName));

				// Cache the loaded Typeface
				sTypefaceCache.put(typefaceName, mTypeface);
			}
		}

		@Override
		public void updateMeasureState(TextPaint p) {
			p.setTypeface(mTypeface);

			// Note: This flag is required for proper typeface rendering
			p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
			Resources r = mContext.getResources();
			p.setTextSize(r.getDimension(R.dimen.actionbar_title_text_size));
		}

		@Override
		public void updateDrawState(TextPaint tp) {
			tp.setTypeface(mTypeface);

			// Note: This flag is required for proper typeface rendering
			tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
			Resources r = mContext.getResources();
			tp.setTextSize(r.getDimension(R.dimen.actionbar_title_text_size));
		}
	}

}
