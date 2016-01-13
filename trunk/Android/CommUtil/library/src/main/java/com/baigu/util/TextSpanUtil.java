package com.baigu.util;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;

/**
 * 在一段文字里显示不同样式字符得工具
 * 
 */
public class TextSpanUtil {

	/**
	 * 
	 * @param span
	 * @param start
	 *            特殊字符得起始位置
	 * @param end
	 *            特使字符的截止位置
	 * @param color
	 *            特殊字符的颜色,为0时不改变特殊字符得颜色
	 * @param size
	 *            特殊字符得大小，为0时不改变特殊字符得大小
	 * @return
	 */
	public static Spannable spanText(String span, int start, int end,
			int color, int size, boolean hasUnderLine) {
		Spannable spannable = new SpannableString(span);
		if (color != 0) {
			spannable.setSpan(new ForegroundColorSpan(color), start, end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		if (size != 0) {
			spannable.setSpan(new AbsoluteSizeSpan(size), start, end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		if (hasUnderLine) {
			spannable.setSpan(new UnderlineSpan(), start, end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return spannable;
	}
}
