package com.joelzhu.miweather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class JZMiWeather extends View {
    // 画笔对象
    private Paint paint;
    // 路径对象
    private Path path;
    // Rect
    private Rect rect;

    // 适配器对象
    private WeatherAdapter adapter;

    private int widgetHeight;
    // 单个时点子项宽度
    private int itemWidth;
    // 分割线数组
    private List<Float> splits;
    // 天气数组
    private List<WeatherModel.WeatherType> weathers;
    // 天气Bitmap对象
    private Bitmap bSunny, bCloudy, bOvercast, bRain, bThunderstorm, bSnow;
    // Bitmap对象宽高
    private int bWidth, bHeight;

    // 上一次事件X坐标
    private float lastEventX;
    // X坐标当前偏移量
    private int offsetX;
    // X坐标最大偏移量
    private int maxOffsetX;
    // 屏幕宽度
    private int screenWidth;
    // 是否需要添加数据进入数组
    private boolean isNeedAdd = true;

    public JZMiWeather(Context context) {
        super(context);

        // 初始化控件
        initWidget();
    }

    public JZMiWeather(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 初始化控件
        initWidget();
    }

    public JZMiWeather(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 初始化控件
        initWidget();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 获取高度和宽度的指定模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 判断指定控件宽度的模式
        int widgetWidth;
        if (widthMode == MeasureSpec.EXACTLY) {
            // 以精确值作为宽
            widgetWidth = widthSize;
        } else {
            // 以250dp作为默认宽
            widgetWidth = dp2Px(250);
        }

        // 判断指定控件高度的模式
        if (heightMode == MeasureSpec.EXACTLY) {
            // 以精确值作为高
            widgetHeight = heightSize;
        } else {
            // 以200dp作为默认高
            widgetHeight = dp2Px(200);
        }

        // 构建高宽指定的控件
        setMeasuredDimension(widgetWidth, widgetHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制天气控件
        addWeatherView(canvas);

        // 绘制天气图标
        addWeatherIcon(canvas);

        isNeedAdd = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            // 按下事件
            case MotionEvent.ACTION_DOWN:
                lastEventX = event.getX();
                return true;

            // 移动事件
            case MotionEvent.ACTION_MOVE:
                offsetX = offsetX + (int) (lastEventX - event.getX());
                // 设置左边边界
                if (offsetX < 0)
                    offsetX = 0;
                // 设置右边边界
                if (offsetX > maxOffsetX)
                    offsetX = maxOffsetX;
                scrollTo(offsetX, 0);
                lastEventX = event.getX();
                return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        // 判断如果各个Bitmap不为空，释放资源
        if (bSunny != null) {
            bSunny.recycle();
            bSunny = null;
        }
        if (bCloudy != null) {
            bCloudy.recycle();
            bCloudy = null;
        }
        if (bOvercast != null) {
            bOvercast.recycle();
            bOvercast = null;
        }
        if (bRain != null) {
            bRain.recycle();
            bRain = null;
        }
        if (bThunderstorm != null) {
            bThunderstorm.recycle();
            bThunderstorm = null;
        }
        if (bSnow != null) {
            bSnow.recycle();
            bSnow = null;
        }
    }

    /**
     * 初始化控件
     */
    private void initWidget() {
        // 开启绘制
        setWillNotDraw(false);

        // 子项宽度设置为50dp
        itemWidth = dp2Px(50);

        // 初始化Paint对象
        paint = new Paint();
        // 初始化Path对象
        path = new Path();
        // 初始化Rect对象
        rect = new Rect();

        // 初始化分割线数组
        splits = new ArrayList<>();
        // 初始化天气数组
        weathers = new ArrayList<>();

        // 获取屏幕宽度
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
    }

    /**
     * 绘制天气控件
     *
     * @param canvas 画布对象
     */
    private void addWeatherView(Canvas canvas) {
        // 初始化path对象
        path.reset();
        // 初始化paint对象
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);

        // 获取温度最大和最小值
        final int maxTemperature = adapter.getMaxMinTemperature()[0];
        final int minTemperature = adapter.getMaxMinTemperature()[1];
        // 计算每一温度的单位高度
        float tUnitHeight = (float) (widgetHeight / 2) / (float) (maxTemperature - minTemperature);
        // 计算高度温度点偏差，如果每个单位温度的高度大于10dp，则将单位高度设置为10dp
        float offsetHeight = 0;
        if (tUnitHeight > dp2Px(10)) {
            // 高度温度点偏差
            offsetHeight = (tUnitHeight - dp2Px(10)) / 2f;
            tUnitHeight = dp2Px(10);
        }

        final int size = adapter.getCount();
        // 遍历数据源，绘制所有温度点
        for (int i = 0; i < size; i++) {
            // 计算温度点的X和Y坐标
            // X坐标：前面温度点的宽度总和 ＋ 半个温度点宽度
            final float centerX = itemWidth * i + itemWidth / 2;
            // Y坐标：从60%的高度为底开始绘制，高度基 ＋ 温度差 ＊ 单位温度高度
            final float pointY = (float) widgetHeight / 10f * 6f - offsetHeight -
                    (adapter.getData().get(i).getTemperature() - minTemperature) * tUnitHeight;
            // 设置路径对象
            if (i == 0)
                // 将path移动到第一个点上
                path.moveTo(centerX, pointY);
            else
                // 将path连线到其他点上
                path.lineTo(centerX, pointY);

            // 绘制温度点
            paint.reset();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(dp2Px(5));
            canvas.drawPoint(centerX, pointY, paint);

            // 绘制温度点温度文字
            final Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            final String temperatureString = adapter.getData().get(i).getTemperature() + "℃";
            paint.reset();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setTextSize(dp2Px(10));
            paint.getTextBounds(temperatureString, 0, temperatureString.length(), rect);
            // 计算文字的X坐标和Y坐标
            // X坐标：温度点X坐标 － 文字的一半宽度
            final float xTPosition = centerX - rect.width() / 2;
            // Y坐标：温度点Y坐标 － 二十分之一控件高
            final float yTPosition = pointY - (float) widgetHeight / 20f;
            canvas.drawText(temperatureString, xTPosition, yTPosition, paint);

            // 绘制时间点文字
            final String timeString = adapter.getData().get(i).getTime();
            paint.reset();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setTextSize(dp2Px(10));
            paint.getTextBounds(timeString, 0, timeString.length(), rect);
            // 计算文字的X坐标和Y坐标
            // X坐标：温度点X坐标 － 文字的一半宽度
            final float xPosition = centerX - rect.width() / 2;
            // Y坐标：控件底部十分之一高度的中心
            final float yPosition = rect.centerY() - fontMetrics.top / 2 - fontMetrics.bottom / 2 +
                    (float) widgetHeight / 10f * 9f;
            canvas.drawText(timeString, xPosition, yPosition, paint);

            // 如果相邻时点天气不相同，绘制分割线
            if (i == 0 || i == adapter.getCount() - 1 || adapter.getData().get(i).getWeatherType()
                    != adapter.getData().get(i - 1).getWeatherType()) {
                // 绘制从温度点到80%高度的分割线
                paint.setStrokeWidth(dp2Px(1));
                canvas.drawLine(centerX, pointY + (float) widgetHeight / 40f, centerX,
                        (float) widgetHeight / 5f * 4f, paint);

                if (isNeedAdd) {
                    // 将分割线添加到数组中
                    splits.add(centerX);
                    // 将天气添加到数组中
                    weathers.add(adapter.getData().get(i).getWeatherType());
                }
            }
        }

        // 绘制温度点连线
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(dp2Px(2));
        canvas.drawPath(path, paint);
    }

    /**
     * 绘制天气图标
     *
     * @param canvas 画布对象
     */
    private void addWeatherIcon(Canvas canvas) {
        // 初始化paint对象
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);

        final int size = splits.size() - 1;
        for (int i = 0; i < size; i++) {
            // 计算Bitmap图标的位置
            // 计算左侧分割线X坐标(如果理想左分割线超出屏幕左边界，以屏幕左边界为实际分割线)
            final float leftX = splits.get(i) < offsetX ? offsetX : splits.get(i);
            // 计算右侧分割线X坐标(如果理想右分割线超出屏幕右边界，以屏幕右边界为实际分割线)
            final float splitOffsetX = offsetX + screenWidth;
            final float rightX = splits.get(i + 1) > splitOffsetX ? splitOffsetX : splits.get(i + 1);
            // X坐标：两条分割线的中心点
            final float deltaX = rightX - leftX;
            float bitmapX = (deltaX - bWidth) / 2 + leftX;
            // 当两条分割线间距小于图标宽度时，修正图标位置
            if (deltaX < bWidth)
                // 当左侧理想分割线已经超出屏幕左侧，强制图标紧贴右侧分割线；右侧同理
                bitmapX = splits.get(i) < offsetX ? rightX - bWidth : leftX;
            // Y坐标：从60%的高度为底开始到高度基的中心点
            final float bitmapY = (float) widgetHeight * 0.6f + ((float) widgetHeight * 0.2f - bHeight) / 2;
            switch (weathers.get(i)) {
                // 晴
                case Sunny:
                    canvas.drawBitmap(bSunny, bitmapX, bitmapY, paint);
                    break;
                // 多云
                case Cloudy:
                    canvas.drawBitmap(bCloudy, bitmapX, bitmapY, paint);
                    break;
                // 阴天
                case Overcast:
                    canvas.drawBitmap(bOvercast, bitmapX, bitmapY, paint);
                    break;
                // 雨
                case Rain:
                    canvas.drawBitmap(bRain, bitmapX, bitmapY, paint);
                    break;
                // 雷阵雨
                case Thunderstorm:
                    canvas.drawBitmap(bThunderstorm, bitmapX, bitmapY, paint);
                    break;
                // 雪
                case Snow:
                    canvas.drawBitmap(bSnow, bitmapX, bitmapY, paint);
                    break;
            }
        }
    }

    /**
     * 将DP单位的值转成为PX单位的值
     *
     * @param dpValue 换算前的DP值
     * @return 换算后的PX值
     */
    private int dp2Px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 添加适配器
     *
     * @param adapter 数据适配器
     */
    public void setAdapter(WeatherAdapter adapter) {
        this.adapter = adapter;
        // 设置最大偏移量
        maxOffsetX = itemWidth * (adapter == null ? 0 : adapter.getCount()) - screenWidth;

        if (adapter != null) {
            // 设置各种天气的bitmap对象
            for (WeatherModel model : adapter.getData()) {
                switch (model.getWeatherType()) {
                    // 晴
                    case Sunny:
                        if (bSunny == null) {
                            bSunny = BitmapFactory.decodeResource(getResources(), R.drawable.sunny);
                            if (bWidth == 0 || bHeight == 0) {
                                // 获取Bitmap对象的宽高
                                bWidth = bSunny.getWidth();
                                bHeight = bSunny.getHeight();
                            }
                        }
                        break;
                    // 多云
                    case Cloudy:
                        if (bCloudy == null) {
                            bCloudy = BitmapFactory.decodeResource(getResources(), R.drawable.cloudy);
                            if (bWidth == 0 || bHeight == 0) {
                                // 获取Bitmap对象的宽高
                                bWidth = bCloudy.getWidth();
                                bHeight = bCloudy.getHeight();
                            }
                        }
                        break;
                    // 阴天
                    case Overcast:
                        if (bOvercast == null) {
                            bOvercast = BitmapFactory.decodeResource(getResources(), R.drawable.overcast);
                            if (bWidth == 0 || bHeight == 0) {
                                // 获取Bitmap对象的宽高
                                bWidth = bOvercast.getWidth();
                                bHeight = bOvercast.getHeight();
                            }
                        }
                        break;
                    // 雨
                    case Rain:
                        if (bRain == null) {
                            bRain = BitmapFactory.decodeResource(getResources(), R.drawable.rain);
                            if (bWidth == 0 || bHeight == 0) {
                                // 获取Bitmap对象的宽高
                                bWidth = bRain.getWidth();
                                bHeight = bRain.getHeight();
                            }
                        }
                        break;
                    // 雷阵雨
                    case Thunderstorm:
                        if (bThunderstorm == null) {
                            bThunderstorm = BitmapFactory.decodeResource(getResources(), R.drawable.thunderstorm);
                            if (bWidth == 0 || bHeight == 0) {
                                // 获取Bitmap对象的宽高
                                bWidth = bThunderstorm.getWidth();
                                bHeight = bThunderstorm.getHeight();
                            }
                        }
                        break;
                    // 雪
                    case Snow:
                        if (bSnow == null) {
                            bSnow = BitmapFactory.decodeResource(getResources(), R.drawable.snow);
                            if (bWidth == 0 || bHeight == 0) {
                                // 获取Bitmap对象的宽高
                                bWidth = bSnow.getWidth();
                                bHeight = bSnow.getHeight();
                            }
                        }
                        break;
                }
            }
        }

        invalidate();
    }
}