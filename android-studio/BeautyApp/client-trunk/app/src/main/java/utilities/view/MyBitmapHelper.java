package utilities.view;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class MyBitmapHelper {

	public static String encodeTobase64(Bitmap image) {
		Bitmap immage = image;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		immage.compress(Bitmap.CompressFormat.JPEG, 85, baos);
		byte[] b = baos.toByteArray();
		String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

		return imageEncoded;
	}

	public static Bitmap decodeBase64(String input) {
		byte[] decodedByte = Base64.decode(input.getBytes(), Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

}
