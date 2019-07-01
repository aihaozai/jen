package com.example.cycleview.tools;



import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

public class DrawableUtils {
	/**
	 * 创建 shape 图片资源
	 * @param radius 圆角
	 * @param color 颜色
	 * @return
	 */
	public static Drawable createShape(int radius, int color) {
		GradientDrawable normalDrawable = new GradientDrawable();
		normalDrawable.setCornerRadius(radius);
		normalDrawable.setColor(color);
		return normalDrawable;
	}

	/**
	 * 创建 selector 图片资源
	 * @return
	 */
	public static  Drawable createEnableDisableSelector(int radius, int disableColor, int enableColor) {
		// 设置 selector
		StateListDrawable stateListDrawable = new StateListDrawable();
		stateListDrawable.addState(new int[] { android.R.attr.state_enabled },
				createShape(radius, enableColor ));
		stateListDrawable.addState(new int[] {}, createShape(radius,disableColor ) );
		return stateListDrawable;
	}

}
