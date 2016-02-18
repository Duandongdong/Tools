package com.bestgood.commons.util;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * ViewUtils
 * <ul>
 * <strong>get view height</strong>
 * <li>{@link ViewUtils#getListViewHeightBasedOnChildren(ListView)}</li>
 * <li>{@link ViewUtils#getAbsListViewHeightBasedOnChildren(AbsListView)}</li>
 * </ul>
 * <ul>
 * <strong>set view height</strong>
 * <li>{@link ViewUtils#setViewHeight(View, int)} set view height</li>
 * <li>{@link ViewUtils#setListViewHeightBasedOnChildren(ListView)}</li>
 * <li>{@link ViewUtils#setAbsListViewHeightBasedOnChildren(AbsListView)}</li>
 * </ul>
 * <ul>
 * <strong>get other info</strong>
 * <li>{@link ViewUtils#getGridViewVerticalSpacing(GridView)} get GridView vertical spacing</li>
 * </ul>
 * <ul>
 * <strong>set other info</strong>
 * <li>{@link ViewUtils#setSearchViewOnClickListener(View, OnClickListener)}</li>
 * </ul>
 */
public class ViewUtils {

    private ViewUtils() {
        throw new AssertionError();
    }

    /**
     * get ListView height according to every children
     *
     * @param view
     * @return
     */
    public static int getListViewHeightBasedOnChildren(ListView view) {
        int height = getAbsListViewHeightBasedOnChildren(view);
        ListAdapter adapter;
        int adapterCount;
        if (view != null && (adapter = view.getAdapter()) != null && (adapterCount = adapter.getCount()) > 0) {
            height += view.getDividerHeight() * (adapterCount - 1);
        }
        return height;
    }

    // /**
    // * get GridView height according to every children
    // *
    // * @param view
    // * @return
    // */
    // public static int getGridViewHeightBasedOnChildren(GridView view) {
    // int height = getAbsListViewHeightBasedOnChildren(view);
    // ListAdapter adapter;
    // int adapterCount, numColumns = getGridViewNumColumns(view);
    // if (view != null && (adapter = view.getAdapter()) != null && (adapterCount = adapter.getCount()) > 0
    // && numColumns > 0) {
    // int rowCount = (int)Math.ceil(adapterCount / (double)numColumns);
    // height = rowCount * (height / adapterCount + getGridViewVerticalSpacing(view));
    // }
    // return height;
    // }
    //
    // /**
    // * get GridView columns number
    // *
    // * @param view
    // * @return
    // */
    // public static int getGridViewNumColumns(GridView view) {
    // if (view == null || view.getChildCount() <= 0) {
    // return 0;
    // }
    // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
    // return getNumColumnsCompat11(view);
    //
    // } else {
    // int columns = 0;
    // int children = view.getChildCount();
    // if (children > 0) {
    // int width = view.getChildAt(0).getMeasuredWidth();
    // if (width > 0) {
    // columns = view.getWidth() / width;
    // }
    // }
    // return columns;
    // }
    // }
    //
    // @TargetApi(11)
    // private static int getNumColumnsCompat11(GridView view) {
    // return view.getNumColumns();
    // }

    private static final String CLASS_NAME_GRID_VIEW = "android.widget.GridView";
    private static final String FIELD_NAME_VERTICAL_SPACING = "mVerticalSpacing";

    /**
     * get GridView vertical spacing
     *
     * @param view
     * @return
     */
    public static int getGridViewVerticalSpacing(GridView view) {
        // get mVerticalSpacing by android.widget.GridView
        Class<?> demo = null;
        int verticalSpacing = 0;
        try {
            demo = Class.forName(CLASS_NAME_GRID_VIEW);
            Field field = demo.getDeclaredField(FIELD_NAME_VERTICAL_SPACING);
            field.setAccessible(true);
            verticalSpacing = (Integer) field.get(view);
            return verticalSpacing;
        } catch (Exception e) {
            /**
             * accept all exception, include ClassNotFoundException, NoSuchFieldException, InstantiationException,
             * IllegalArgumentException, IllegalAccessException, NullPointException
             */
            e.printStackTrace();
        }
        return verticalSpacing;
    }

    /**
     * get AbsListView height according to every children
     *
     * @param view
     * @return
     */
    public static int getAbsListViewHeightBasedOnChildren(AbsListView view) {
        ListAdapter adapter;
        if (view == null || (adapter = view.getAdapter()) == null) {
            return 0;
        }

        int height = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View item = adapter.getView(i, null, view);
            if (item instanceof ViewGroup) {
                item.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            }
            item.measure(0, 0);
            height += item.getMeasuredHeight();
        }
        height += view.getPaddingTop() + view.getPaddingBottom();
        return height;
    }

    /**
     * set view height
     *
     * @param view
     * @param height
     */
    public static void setViewHeight(View view, int height) {
        if (view == null) {
            return;
        }

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
    }

    // /**
    // * set GistView height which is calculated by {@link # getGridViewHeightBasedOnChildren(GridView)}
    // *
    // * @param view
    // * @return
    // */
    // public static void setGridViewHeightBasedOnChildren(GridView view) {
    // setViewHeight(view, getGridViewHeightBasedOnChildren(view));
    // }

    /**
     * set ListView height which is calculated by {@link # getListViewHeightBasedOnChildren(ListView)}
     *
     * @param view
     * @return
     */
    public static void setListViewHeightBasedOnChildren(ListView view) {
        setViewHeight(view, getListViewHeightBasedOnChildren(view));
    }

    /**
     * set AbsListView height which is calculated by {@link # getAbsListViewHeightBasedOnChildren(AbsListView)}
     *
     * @param view
     * @return
     */
    public static void setAbsListViewHeightBasedOnChildren(AbsListView view) {
        setViewHeight(view, getAbsListViewHeightBasedOnChildren(view));
    }

    /**
     * set SearchView OnClickListener
     *
     * @param v
     * @param listener
     */
    public static void setSearchViewOnClickListener(View v, OnClickListener listener) {
        if (v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) v;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = group.getChildAt(i);
                if (child instanceof LinearLayout || child instanceof RelativeLayout) {
                    setSearchViewOnClickListener(child, listener);
                }

                if (child instanceof TextView) {
                    TextView text = (TextView) child;
                    text.setFocusable(false);
                }
                child.setOnClickListener(listener);
            }
        }
    }

    /**
     * get descended views from parent.
     *
     * @param parent
     * @param filter          Type of views which will be returned.
     * @param includeSubClass Whether returned list will include views which are subclass of filter or not.
     * @return
     */
    public static <T extends View> List<T> getDescendants(ViewGroup parent, Class<T> filter, boolean includeSubClass) {
        List<T> descendedViewList = new ArrayList<T>();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            Class<? extends View> childsClass = child.getClass();
            if ((includeSubClass && filter.isAssignableFrom(childsClass))
                    || (!includeSubClass && childsClass == filter)) {
                descendedViewList.add(filter.cast(child));
            }
            if (child instanceof ViewGroup) {
                descendedViewList.addAll(getDescendants((ViewGroup) child, filter, includeSubClass));
            }
        }
        return descendedViewList;
    }

    public static void setEnabled(View view, boolean enabled) {
        if (view == null) {
            return;
        }
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            for (int i = 0; i < parent.getChildCount(); i++) {
                setEnabled(parent.getChildAt(i), enabled);
            }
        }
    }

    /**
     * 把一个view转化成bitmap对象
     */
    public static Bitmap getViewBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    /**
     * 将给定文字画在Bitmap上
     *
     * @param res
     * @param id
     * @param text
     * @return
     */
    public static Bitmap writeTextOnBitmap(Resources res, int id, String text) {
        return writeTextOnDrawable(res, id, text);
    }

    /**
     * 将给定文字画在drawable上
     *
     * @param res
     * @param id
     * @param text
     * @return
     */
    public static Bitmap writeTextOnDrawable(Resources res, int id, String text) {
        Bitmap bitmap = BitmapFactory.decodeResource(res, id).copy(
                Bitmap.Config.ARGB_8888, true);
        try {
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            Typeface font = Typeface.create(text, Typeface.BOLD);
            paint.setColor(Color.WHITE);
            paint.setTypeface(font);
            paint.setTextSize(30);

            Rect bounds = new Rect();
            paint.getTextBounds(text, 0, text.length(), bounds);
            int width = bounds.right - bounds.left;
            int height = bounds.bottom - bounds.top;
            canvas.drawText(text, (canvas.getWidth() - width) / 2,
                    canvas.getHeight() - (canvas.getHeight() / 2 - height / 2)
                            - 10, paint);
        } catch (Exception e) {
        }
        // return new BitmapDrawable(bitmap);
        // return new BitmapDrawable(res, bitmap);
        return bitmap;
    }

    /**
     * Sets the background for a view while preserving its current padding. If
     * the background drawable has its own padding, that padding will be added
     * to the current padding.
     *
     * @param view               View to receive the new background.
     * @param backgroundDrawable Drawable to set as new background.
     */
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static void setBackgroundAndKeepPadding(View view,
                                                   Drawable backgroundDrawable) {
        Rect drawablePadding = new Rect();
        backgroundDrawable.getPadding(drawablePadding);
        int top = view.getPaddingTop() + drawablePadding.top;
        int left = view.getPaddingLeft() + drawablePadding.left;
        int right = view.getPaddingRight() + drawablePadding.right;
        int bottom = view.getPaddingBottom() + drawablePadding.bottom;
        view.setBackgroundDrawable(backgroundDrawable);
        view.setPadding(left, top, right, bottom);
    }

    /**
     * 当前位置图标旋转 一个角度
     *
     * @param bearing
     * @return
     */
    // public static Bitmap rotateBitmap(Resources res, int id, float angle) {
    // Bitmap bmpOriginal = BitmapFactory.decodeResource(res, id);
    // Bitmap bmResult = Bitmap.createBitmap(bmpOriginal.getWidth(),
    // bmpOriginal.getHeight(), Bitmap.Config.ARGB_8888);
    //
    // Canvas tempCanvas = new Canvas(bmResult);
    // tempCanvas.rotate(angle, bmpOriginal.getWidth() / 2,
    // bmpOriginal.getHeight() / 2);
    // tempCanvas.drawBitmap(bmpOriginal, 0, 0, null);
    //
    // // Drawable drawable = new BitmapDrawable(res, bmResult);
    // Drawable drawable = new BitmapDrawable(bmResult);
    // return drawableToBitmap(drawable);
    // }

    /**
     * 旋转 Bitmap 一个方向 angle
     *
     * @param id
     * @param angle
     * @return
     */
    public static Bitmap rotateBitmap(Resources res, int id, float angle) {
        Bitmap bitmap = BitmapFactory.decodeResource(res, id);

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap bmResult = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);

        // Drawable drawable = new BitmapDrawable(res, bmResult);
        // Drawable drawable = new BitmapDrawable(bmResult);
        // return drawableToBitmap(drawable);
        return bmResult;
    }

    /**
     * 图片倒影处理
     *
     * @param bitmap
     * @return
     */
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 0;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
                width, height / 2, matrix, false);
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
                (height + height / 4), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);
        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
        Paint paint = new Paint();
        // 创建线性渐变LinearGradient对象
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
                0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
                + reflectionGap, paint);
        return bitmapWithReflection;
    }

    /**
     * drawable转换成bitmap类型
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        // canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 图片缩放成指定的大小 csj start
     *
     * @param drawable
     * @param w
     * @param h
     * @return
     */
    public static Bitmap zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        newbmp = getRCB(newbmp, 20f);
        return newbmp;
    }

    /**
     * 图片圆角处理
     *
     * @param bitmap
     * @param roundPX
     * @return
     */
    public static Bitmap getRCB(Bitmap bitmap, float roundPX) {
        Bitmap dstbmp = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dstbmp);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xff424242);
        canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return dstbmp;
    }

    /**
     * dip转换成px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 对图片进行压缩
     *
     * @param filePath
     * @return
     */
    public static void resizeBitMapImage(String filePath) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        // 获取这个图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);// 此时返回bm为空
        options.inJustDecodeBounds = false;

        // 计算缩放比
        int be = (int) (options.outHeight / 400.0f);
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，注意这次要把options.inJustDecodeBounds设为false哦
        bitmap = BitmapFactory.decodeFile(filePath, options);

        // System.out.println(w + " " + h);

        // myImageView.setImageBitmap(bitmap);

        // 保存入sdCard
        File file2 = new File(filePath);
        try {
            FileOutputStream out = new FileOutputStream(file2);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
        }

        // 读取sd卡
        // File file = new File("/sdcard/dcim/Camera/test.jpg");
        // int maxBufferSize = 16 * 1024;
        //
        // int len = 0;
        // ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // BufferedInputStream bufferedInputStream;
        // try {
        // bufferedInputStream = new BufferedInputStream(new FileInputStream(
        // file));
        // int bytesAvailable = bufferedInputStream.available();
        // int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        // byte[] buffer = new byte[bufferSize];
        // while ((len = bufferedInputStream.read(buffer)) != -1) {
        // outStream.write(buffer, 0, bufferSize);
        // }
        // data = outStream.toByteArray();
        // outStream.close();
        // bufferedInputStream.close();
        //
        // } catch (FileNotFoundException e) {
        // e.printStackTrace();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
    }
}
