/**
 * 
 */
package simbio.se.sau.widget;

import simbio.se.sau.API;
import android.R.string;
import android.content.Context;
import android.widget.Toast;

/**
 * A class to show {@link Toast} with an easy way
 * 
 * @author Ademar Alves de Oliveira <ademar111190@gmail.com>
 * @date 2013-sep-29 22:28:56
 * 
 * @since {@link API#Version_2_0_0}
 * 
 */
public class ToastMaker {

	/**
	 * This method show a toast, more details see {@link Toast#makeText(Context, CharSequence, int)} and {@link Toast#show()}
	 * 
	 * @param context
	 *            the {@link Context} to be used, more details see {@link Toast#makeText(Context, CharSequence, int)}
	 * @param text
	 *            the {@link CharSequence} to be showed, more details see {@link Toast#makeText(Context, CharSequence, int)}
	 * @param duration
	 *            the {@link Integer} the time duration, is a good idea use {@link Toast#LENGTH_SHORT} or {@link Toast#LENGTH_LONG}, more details see {@link Toast#makeText(Context, CharSequence, int)}
	 * @since {@link API#Version_2_0_0}
	 */
	public static void toast(Context context, CharSequence text, int duration) {
		Toast.makeText(context, text, duration).show();
	}

	/**
	 * This method show a toast, more details see {@link Toast#makeText(Context, int, int)} and {@link Toast#show()}
	 * 
	 * @param context
	 *            the {@link Context} to be used, more details see {@link Toast#makeText(Context, int, int)}
	 * @param text
	 *            the {@link Integer} id of {@link string}, more details see {@link Toast#makeText(Context, int, int)}
	 * @param duration
	 *            the {@link Integer} the time duration, is a good idea use {@link Toast#LENGTH_SHORT} or {@link Toast#LENGTH_LONG}, more details see {@link Toast#makeText(Context, int, int)}
	 * @since {@link API#Version_2_0_0}
	 */
	public static void toast(Context context, int text, int duration) {
		Toast.makeText(context, text, duration).show();
	}

	/**
	 * This method show a toast using the {@link Toast#LENGTH_LONG} as duration, more details see {@link Toast#makeText(Context, CharSequence, int)} and {@link Toast#show()}
	 * 
	 * @param context
	 *            the {@link Context} to be used, more details see {@link Toast#makeText(Context, CharSequence, int)}
	 * @param text
	 *            the {@link CharSequence} to be showed, more details see {@link Toast#makeText(Context, CharSequence, int)}
	 * @since {@link API#Version_2_0_0}
	 */
	public static void toast(Context context, CharSequence text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	/**
	 * This method show a toast using the {@link Toast#LENGTH_LONG} as duration, more details see {@link Toast#makeText(Context, int, int)} and {@link Toast#show()}
	 * 
	 * @param context
	 *            the {@link Context} to be used, more details see {@link Toast#makeText(Context, int, int)}
	 * @param text
	 *            the {@link Integer} id of {@link string}, more details see {@link Toast#makeText(Context, int, int)}
	 * @since {@link API#Version_2_0_0}
	 */
	public static void toast(Context context, int text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	/**
	 * This method show a toast using the {@link Toast#LENGTH_LONG} as duration and "Here" as message text, more details see {@link Toast#makeText(Context, CharSequence, int)} and {@link Toast#show()}
	 * 
	 * @param context
	 *            the {@link Context} to be used, more details see {@link Toast#makeText(Context, CharSequence, int)}
	 * @since {@link API#Version_2_0_0}
	 */
	public static void toast(Context context) {
		Toast.makeText(context, "Here", Toast.LENGTH_LONG).show();
	}

	/**
	 * This method show a toast using the {@link Toast#LENGTH_LONG} as duration and "Here" as message text, more details see {@link Toast#makeText(Context, CharSequence, int)} and {@link Toast#show()}
	 * 
	 * @param context
	 *            the {@link Context} to be used, more details see {@link Toast#makeText(Context, CharSequence, int)}
	 * @param e
	 *            an {@link Exception} to be toasted
	 * @since {@link API#Version_3_0_0}
	 */
	public static void toast(Context context, Exception e) {
		Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
	}

}
