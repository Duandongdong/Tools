package com.bestgood.commons.thirdparty.amap;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.text.Html;
import android.text.Spanned;
import android.widget.EditText;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.DPoint;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveStep;
import com.bestgood.commons.util.log.Logger;

/**
 * AMap 相关工具类
 *
 * @author ddc
 * @date: Jun 14, 2014 3:20:45 PM
 */
public class AMapUtil {
    /**
     * 地图初始化 时的 缩放级别
     **/
    public final static int MAP_INIT_ZOOM = 17;

    /**
     * http://inkanet.pateo.com.cn/payment/?action=tmc_tips_no_auth
     * 红色代表拥堵、黄色代表缓行、绿色则代表畅通
     * 道路等级             拥堵阀值	 缓行阀值	    畅通阀值
     * 高速	                V≤40	40＜V＞60	  V≥60
     * 快速	                V≤20	20＜V＞40	  V≥40
     * 城市主干道/城市次干道	V≤12	12＜V＞25	  V≥25
     * 其他道路	            V≤10	10＜V＞20	  V≥20
     *
     * @param step
     * @return
     */
    public static int getDriveColor(DriveStep step) {
        int color = Color.GREEN;
        if (step == null) {
            return color;
        }

        StringBuilder sb = logDriveStep(step);

        float distance = step.getDistance();//返回驾车路段的距离，单位米。
        float duration = step.getDuration();//返回驾车路段的预计时间，单位秒。
        float tolls = step.getTolls();//返回驾车路段的收费价格，单位元。

        float speed = distance / duration * 3.6f;//1/s=3.6km/h
        sb.append("speed:");
        sb.append(speed);
        sb.append("; ");

        if (tolls == 0) {//驾车路段的收费价格为0,当做 城市主干道/城市次干道
            sb.append("城市主干道/城市次干道; ");
            if (speed <= 12) {
                sb.append("拥堵");
                color = Color.parseColor("#E94B37");
            } else if (speed < 25) {
                sb.append("缓行");
                color = Color.parseColor("#FFC700");
            } else {
                sb.append("畅通");
                color = Color.parseColor("#1BAC2E");
            }
        } else {//驾车路段的收费价格不为0,当做 高速
            sb.append("高速; ");
            if (speed <= 40) {
                sb.append("拥堵");
                color = Color.parseColor("#E94B37");
            } else if (speed < 60) {
                sb.append("缓行");
                color = Color.parseColor("#FFC700");
            } else {
                sb.append("畅通");
                color = Color.parseColor("#1BAC2E");
            }
        }

        Logger.e(sb.toString());
        return color;

        //return Color.parseColor("#537edc");
    }

    private static StringBuilder logDriveStep(DriveStep step) {
        StringBuilder sb = new StringBuilder();
        sb.append("Action:");
        sb.append(step.getAction());
        sb.append("\n");
        sb.append("AssistantAction:");
        sb.append(step.getAssistantAction());
        sb.append("\n");
        sb.append("Distance:");
        sb.append(step.getDistance());
        sb.append("\n");
        sb.append("Duration:");
        sb.append(step.getDuration());
        sb.append("\n");
        sb.append("Instruction:");
        sb.append(step.getInstruction());
        sb.append("\n");
        sb.append("Orientation:");
        sb.append(step.getOrientation());
        sb.append("\n");
        sb.append("Road:");
        sb.append(step.getRoad());
        sb.append("\n");
        sb.append("TollDistance:");
        sb.append(step.getTollDistance());
        sb.append("\n");
        sb.append("TollRoad:");
        sb.append(step.getTollRoad());
        sb.append("\n");
        sb.append("Tolls:");
        sb.append(step.getTolls());
        sb.append("\n");
        sb.append("PolylineSize:");
        int polylineSize = step.getPolyline() == null ? 0 : step.getPolyline().size();
        sb.append(polylineSize);
        sb.append(" --> ");
        sb.append(step.getDistance() / polylineSize);
        sb.append("米");
        sb.append("\n");
        return sb;
    }

    /**
     * 获取驾车路线的总长度,单位 米.
     *
     * @param drivePath
     * @return
     */
    public static float getDrivePathDistance(DrivePath drivePath) {
        float distance = 0;
        if (drivePath == null || drivePath.getSteps() == null) {
            return distance;
        }
        for (DriveStep step : drivePath.getSteps()) {
            if (step == null) {
                continue;
            }
            distance += step.getDistance();
        }
        return distance;
    }

    /**
     * 返回驾车路段的预计时间，单位秒。
     *
     * @param drivePath
     * @return
     */
    public static float getDrivePathDuration(DrivePath drivePath) {
        float duration = 0;
        if (drivePath == null || drivePath.getSteps() == null) {
            return duration;
        }
        for (DriveStep step : drivePath.getSteps()) {
            if (step == null) {
                continue;
            }
            duration += step.getDuration();
        }
        return duration;
    }

    /**
     * 根据传入的List<LatLng> 获取一个 CameraUpdate
     *
     * @param latlngs
     * @return
     */
    public static CameraUpdate newCameraUpdate(List<LatLng> latlngs) {
        if (latlngs == null || latlngs.isEmpty()) {
            return null;
        }
        // ------------------------------------------------------------------------------
        List<LatLng> list = new ArrayList<LatLng>();
        for (LatLng latlng : latlngs) {
            if (latlng == null) {
                continue;
            }
            if (list.contains(latlng)) {
                continue;
            }
            list.add(latlng);
        }
        // ------------------------------------------------------------------------------
        if (list.size() == 1) {
            LatLng latlng = list.get(0);
            if (latlng == null) {
                return null;
            }
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    latlng, AMapUtil.MAP_INIT_ZOOM);
            return cameraUpdate;
        }
        // ------------------------------------------------------------------------------
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latlng : list) {
            if (latlng == null) {
                continue;
            }
            builder.include(latlng);
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(
                builder.build(), 20);
        return cameraUpdate;
    }

    /**
     * 从 AMapLocation 获取 LatLng
     *
     * @param location
     * @return
     */
    public static LatLng getLatLng(AMapLocation location) {
        if (location == null) {
            return null;
        }
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        LatLng latlng = new LatLng(lat, lng);
        return latlng;
    }

    /**
     * @param latlng
     * @return
     */
    public static boolean isValidDPoint(LatLng latlng) {
        if (latlng == null) {
            return false;
        }
        int lat = (int) Math.abs(latlng.latitude);
        int lon = (int) Math.abs(latlng.longitude);
        if (lat > 90) {
            return false;
        }
        if (lon > 180) {
            return false;
        }
        return true/* lat < lon */;
    }

    /**
     * int 型 坐标，转换为 float String 型坐标
     *
     * @param data
     * @return
     */
    public static String e6Int2FloatStr(int data) {
        String str = String.valueOf(data);
        int data1 = data / 1000000;
        // int data2 = data % 1000000;
        return data1 + "." + str.replaceFirst(String.valueOf(data1), "");
    }

    /**
     * Integer 型 坐标，转换为 float String 型坐标
     *
     * @param data
     * @return
     */
    public static String e6Int2FloatStr(Integer data) {
        if (data == null) {
            return "";
        }
        return e6Int2FloatStr(data.intValue());
    }

    /**
     * long 型 坐标，转换为 float String 型坐标
     *
     * @param data
     * @return
     */
    public static String e6Long2FloatStr(long data) {
        String str = String.valueOf(data);
        int data1 = (int) (data / 1000000l);
        // int data2 = data % 1000000;
        return data1 + "." + str.replaceFirst(String.valueOf(data1), "");
    }

    /**
     * Long 型 坐标，转换为 float String 型坐标
     *
     * @param data
     * @return
     */
    public static String e6Long2FloatStr(Long data) {
        if (data == null) {
            return "";
        }
        return e6Long2FloatStr(data.longValue());
    }

    /**
     * long 型 坐标，转换为 float 型坐标
     *
     * @param data
     * @return
     */
    public static float e6Long2Float(long data) {
        String str = String.valueOf(data);
        int data1 = (int) (data / 1000000l);
        // int data2 = data % 1000000;
        String data2 = data1 + "."
                + str.replaceFirst(String.valueOf(data1), "");
        try {
            return Float.parseFloat(data2);
        } catch (Exception e) {
            return 0.0f;
        }
    }

    /**
     * Long 型 坐标，转换为 float 型坐标
     *
     * @param data
     * @return
     */
    public static float e6Long2Float(Long data) {
        if (data == null) {
            return 0.0f;
        }
        return e6Long2Float(data.longValue());
    }

    /**
     * int 型 坐标，转换为 float 型坐标
     *
     * @param data
     * @return
     */
    public static float e6Int2Float(int data) {
        String str = e6Int2FloatStr(data);
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Integer 型 坐标，转换为 float 型坐标
     *
     * @param data
     * @return
     */
    public static float e6Int2Float(Integer data) {
        String str = e6Int2FloatStr(data);
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * float String 型 坐标，转换为 int 型坐标
     *
     * @param str
     * @return
     */
    public static int floatStr2E6Int(String str) {
        int data = 0;
        if (str == null) {
            return data;
        }
        try {
            Double d = Double.parseDouble(str) * 1000000;
            data = d.intValue();
        } catch (Exception e) {
        }

        // str = str.replace(".", "");
        // try {
        // data = Integer.valueOf(str);
        // } catch (Exception e) {
        // }
        return data;
    }

    /**
     * 将带小数点的字符串 转为 double
     *
     * @param str
     * @return
     */
    public static double floatStr2E6Double(String str) {
        double data = 0;
        if (str == null) {
            return data;
        }
        try {
            data = Double.parseDouble(str);
        } catch (Exception e) {
        }
        return data;
    }

    /**
     * float 型 坐标，转换为 int 型坐标
     *
     * @param data
     * @return
     */
    public static int float2E6Int(float data) {
        return (int) (data * 1E6);
    }

    /**
     * Float 型 坐标，转换为 int 型坐标
     *
     * @param data
     * @return
     */
    public static int float2E6Int(Float data) {
        if (data == null) {
            return 0;
        }
        return float2E6Int(data.floatValue());
    }

    /**
     * double 型 坐标，转换为 int 型坐标
     *
     * @param data
     * @return
     */
    public static int double2E6Int(double data) {
        return (int) (data * 1E6);
    }

    /**
     * Double 型 坐标，转换为 int 型坐标
     *
     * @param data
     * @return
     */
    public static int double2E6Int(Double data) {
        if (data == null) {
            return 0;
        }
        return double2E6Int(data.doubleValue());
    }

    public static LatLng double2LatLng(double lat, double lon) {
        return new LatLng(lat, lon);
    }

    public static LatLng float2LatLng(float lat, float lon) {
        return new LatLng(lat, lon);
    }

    /**
     * 设置地图缩放级别，使其在当前屏幕上刚好显示所给的距离范围
     *
     * @param activity
     * @param amap
     * @param distance 在当前屏幕上要显示距离，单位 m
     */
    public static void setMapViewZoom(Activity activity, AMap amap,
                                      int distance, LatLng latlng) {
        if (activity == null || amap == null) {
            return;
        }
        Projection projection = amap.getProjection();
        Point center = projection.toScreenLocation(latlng);
        Logger.d("center = %s", center.toString());

        float radius = distance / amap.getScalePerPixel();
        Logger.d("amap.getScalePerPixel() = %s", amap.getScalePerPixel());
        Logger.d("distance = %s", distance);
        Logger.d("radius = %s", radius);

        Point southwest = new Point((int) (center.x - radius), (int) (center.y + radius));
        Point northeast = new Point((int) (center.x + radius), (int) (center.y - radius));

        Logger.d("southwest = %s", southwest.toString());
        Logger.d("northeast = %s", northeast.toString());

        LatLng southwestLatLng = projection.fromScreenLocation(southwest);
        LatLng northeastLatLng = projection.fromScreenLocation(northeast);

        Logger.d("southwestLatLng = %s", southwestLatLng.toString());
        Logger.d("northeastLatLng = %s", northeastLatLng.toString());

        // 使用传入的西南角坐标和东北角坐标创建一个矩形区域。
        // LatLngBounds(LatLng southwest, LatLng northeast)
        LatLngBounds bounds = new LatLngBounds(southwestLatLng, northeastLatLng);
        amap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));

        // LatLngBounds.Builder builder = new LatLngBounds.Builder();
        // builder.include(latlng);
        // builder.include(southwestLatLng);
        // builder.include(northeastLatLng);
        // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(
        // builder.build(), 20);
        // amap.moveCamera(cameraUpdate);
        // ---------------------------------------------------------------------------------------
        // Bitmap southwestBitMap =
        // ViewUtils.writeTextOnBitmap(activity.getResources(),
        // R.drawable.ic_poi_dot_unfocused, "sw");
        // Bitmap northeastBitMap =
        // ViewUtils.writeTextOnBitmap(activity.getResources(),
        // R.drawable.ic_poi_dot_unfocused, "ne");
        // // amap.clear();// 清理之前的图标
        // amap.addMarker(new MarkerOptions().title("southwest").
        // position(southwestLatLng).icon(BitmapDescriptorFactory.fromBitmap(southwestBitMap)));
        // amap.addMarker(new MarkerOptions().title("northeast").
        // position(northeastLatLng).icon(BitmapDescriptorFactory.fromBitmap(northeastBitMap)));
        // amap.invalidate();
    }

    /**
     * 设置地图缩放级别，使其在当前屏幕上刚好显示所给的距离范围
     *
     * @param activity
     * @param mapView
     * @param distance
     *            在当前屏幕上要显示距离，单位 m
     */
    // public static void setMapViewZoom(Activity activity, AMap amap,
    // int distance, LatLng latlng) {
    // if (activity == null || amap == null) {
    // return;
    // }
    // int width = AppUtils.getWindowPixels(activity)[0];
    //
    // int maxZoomLevel = (int) amap.getMaxZoomLevel();
    // int minZoomLevel = (int) amap.getMinZoomLevel();
    //
    // for (int level = maxZoomLevel; level >= minZoomLevel; level--) {
    // if (getScalePerPixel(level) * width > distance * 2) {
    // amap.moveCamera(latlng == null ? CameraUpdateFactory
    // .zoomTo(level) : CameraUpdateFactory.newLatLngZoom(
    // latlng, level));
    // return;
    // }
    // }
    // }

    /**
     * 获取比例尺数据。当前缩放级别下，地图上1像素点对应的长度，单位米。
     *
     * @param zoomLevel
     * @return 当前缩放级别下，地图上1像素点对应的长度，单位米。
     */
    public static float getScalePerPixel(int zoomLevel) {
        float scalePerPixel = 0.11485534f;
        if (zoomLevel >= 18) {
            scalePerPixel = 0.45609862f;
        } else if (zoomLevel < 4) {
            scalePerPixel = 2575.9216f;
        } else {
            switch (zoomLevel) {
                case 20: {
                    scalePerPixel = 0.45609862f;
                    break;
                }
                case 19: {
                    scalePerPixel = 0.45609862f;
                    break;
                }
                case 18: {
                    scalePerPixel = 0.45609862f;
                    break;
                }
                case 17: {
                    scalePerPixel = 0.9122842f;
                    break;
                }
                case 16: {
                    scalePerPixel = 1.8253888f;
                    break;
                }
                case 15: {
                    scalePerPixel = 3.6501603f;
                    break;
                }
                case 14: {
                    scalePerPixel = 7.298323f;
                    break;
                }
                case 13: {
                    scalePerPixel = 14.588655f;
                    break;
                }
                case 12: {
                    scalePerPixel = 29.145332f;
                    break;
                }
                case 11: {
                    scalePerPixel = 58.16276f;
                    break;
                }
                case 10: {
                    scalePerPixel = 115.81257f;
                    break;
                }
                case 9: {
                    scalePerPixel = 229.56657f;
                    break;
                }
                case 8: {
                    scalePerPixel = 450.85217f;
                    break;
                }
                case 7: {
                    scalePerPixel = 868.2911f;
                    break;
                }
                case 6: {
                    scalePerPixel = 1601.9377f;
                    break;
                }
                case 5: {
                    scalePerPixel = 2675.5293f;
                    break;
                }
                case 4: {
                    scalePerPixel = 3511.7449f;
                    break;
                }
                case 3: {
                    scalePerPixel = 2575.9216f;
                    break;
                }
                case 2: {
                    scalePerPixel = 2575.9216f;
                    break;
                }
                case 1: {
                    scalePerPixel = 2575.9216f;
                    break;
                }
            }
        }
        return scalePerPixel;
    }

    // / <summary>
    // / 由经伟度转长整
    // / </summary>
    // / <param name="para"></param>
    // / <returns></returns>
    public static long EnCodeCoordinate(double para) {
        long dCoordinate = 0;// 度
        long mCoordinate = 0;// 分
        long sCoordinate = 0;// 秒
        long hCorrdinate = 0;// 毫秒
        double dResdiue = 0.0;
        // 度
        dCoordinate = (long) para;
        dResdiue = para - dCoordinate; // 度的余数

        // 分
        mCoordinate = (long) (dResdiue * 60);
        dResdiue = (dResdiue * 60) - mCoordinate; // 分的余数

        // 秒
        sCoordinate = (long) (dResdiue * 60); // Math.Round(dResdiue * 60, 3);//
        // 精确到毫秒
        // 毫秒
        hCorrdinate = (long) (((dResdiue * 60) - sCoordinate) * 1000);

        long result = (dCoordinate * 60 * 60 * 1000)
                + (mCoordinate * 60 * 1000) + (sCoordinate * 1000)
                + hCorrdinate;

        return result;
    }

    public static double EnCodeCoordinate(Double para) {
        if (para == null) {
            return EnCodeCoordinate(0);
        }
        return EnCodeCoordinate(para.doubleValue());
    }

    // / <summary>
    // / 长整转经伟度
    // / </summary>
    // / <param name="para"></param>
    // / <returns></returns>
    public static double DeCodeCoordinate(long totalMilliarcseconds) {
        // double mCorrdinate = totalMilliarcseconds % 1000; // 毫秒数

        // double sTotalCoordinate = totalMilliarcseconds / 1000; // 总秒数
        // double sCoordinate = sTotalCoordinate % 60; // 秒数

        // double mTotalCoordinate = sTotalCoordinate / 60; // 总分数
        // double mCoordinate = mTotalCoordinate % 60; // 分数

        // double dCoordinate = mTotalCoordinate / 60; // 度数

        // //dCoordinate+ mCoordinate / 60+ sCoordinate / 3600 + mCorrdinate /
        // 60000

        // double result = dCoordinate + (mCoordinate / 60) + (sCoordinate /
        // 3600) + (mCorrdinate /(60* 60000));

        // return result;
        double result;
        long du = (long) totalMilliarcseconds / (60 * 60 * 1000);// 度数
        long fen = (long) (totalMilliarcseconds - du * (60 * 60 * 1000))
                / (60 * 1000);// 分
        long miao = (long) (totalMilliarcseconds - fen * 60 * 1000 - du * 60 * 60 * 1000) / 1000;// 秒
        long haomiao = totalMilliarcseconds - miao * 1000 - fen * 60 * 1000
                - du * 60 * 60 * 1000;

        result = (double) du + Math.round((double) fen / 60 * 1E7) / 10000000.0
                + Math.round((double) miao / 60 / 60 * 1E7) / 10000000.0
                + Math.round((double) haomiao / 1000 / 60 / 60 * 1E7)
                / 10000000.0;

        // result = (double) du + Math.Round((double) fen / 60, 7)
        // + Math.Round((double) miao / 60 / 60, 7)
        // + Math.Round((double) haomiao / 1000 / 60 / 60, 7);
        return result;
    }

    public static double DeCodeCoordinate(Long totalMilliarcseconds) {
        if (totalMilliarcseconds == null) {
            return DeCodeCoordinate(0);
        }
        return DeCodeCoordinate(totalMilliarcseconds.longValue());
    }

//	/**
//	 * 设置地图缩放级别，使其在当前屏幕上刚好显示所给的距离范围
//	 *
//	 * @param activity
//	 * @param point1
//	 * @param point2
//	 * @param mapView
//	 */
    // public static void setMapViewZoom(Activity activity, MapView mapView,
    // List<DPoint> points) {
    // if (activity == null || mapView == null) {
    // return;
    // }
    // if (points == null || points.isEmpty()) {
    // return;
    // }
    // if (points.size() == 1) {
    // DPoint point = points.get(0);
    // if (point != null) {
    // mapView.getController().setCenter(point);
    // }
    // return;
    // }
    //
    // //
    // -----------------------------------------------------------------------
    // MPoint ltPoint = null;
    // MPoint rbPoint = null;
    // for (DPoint point : points) {
    // if (point == null) {
    // continue;
    // }
    // MPoint p = new MPoint(point);
    //
    // if (ltPoint == null || rbPoint == null) {
    // ltPoint = new MPoint(point);
    // rbPoint = new MPoint(point);
    // }
    //
    // if (p.x < ltPoint.x) {
    // ltPoint.setX(p.x);
    // } else if (p.x > rbPoint.x) {
    // rbPoint.setX(p.x);
    // }
    // if (p.y < ltPoint.y) {
    // ltPoint.setY(p.y);
    // } else if (p.y > rbPoint.y) {
    // rbPoint.setY(p.y);
    // }
    // }
    //
    // //
    // -----------------------------------------------------------------------
    // if (ltPoint == null || rbPoint == null) {
    // return;
    // }
    // //
    // -----------------------------------------------------------------------
    // float distance = MapController.calculateDistance(ltPoint.toDPoint(),
    // rbPoint.toDPoint());
    // int width = DeviceInfoUtil.getWindowPixels(activity)[0];
    //
    // int zoomLevel = mapView.getMaxZoomLevel();
    // for (int level = zoomLevel; level >= 3; level--) {
    // if (mapView.getMetersPerPixel(level) * width > distance * 2) {
    // mapView.getController().setZoom(level);
    // return;
    // }
    // }
    // //
    // -----------------------------------------------------------------------
    //
    // // mapView.getController().setCenter(new MPoint(x, y).toDPoint());
    // }

    public static DPoint DeCode2DPoint(int latitude, int longitude) {
        double lat = AMapUtil.DeCodeCoordinate(latitude);
        double lon = AMapUtil.DeCodeCoordinate(longitude);
        DPoint geoPoint = new DPoint(AMapUtil.double2E6Int(lat), AMapUtil.double2E6Int(lon));
        return geoPoint;
    }

    public static DPoint DeCode2DPoint(Integer latitude, Integer longitude) {
        int lat = latitude == null ? 0 : latitude;
        int lon = longitude == null ? 0 : longitude;
        return DeCode2DPoint(lat, lon);
    }

    public static DPoint DeCode2DPoint(long latitude, long longitude) {
        double lat = AMapUtil.DeCodeCoordinate(latitude);
        double lon = AMapUtil.DeCodeCoordinate(longitude);
        DPoint geoPoint = new DPoint(AMapUtil.double2E6Int(lat), AMapUtil.double2E6Int(lon));
        return geoPoint;
    }

    public static DPoint DeCode2DPoint(Long latitude, Long longitude) {
        long lat = latitude == null ? 0 : latitude;
        long lon = longitude == null ? 0 : longitude;
        return DeCode2DPoint(lat, lon);
    }

    public static LatLng DeCode2LatLng(long latitude, long longitude) {
        double lat = AMapUtil.DeCodeCoordinate(latitude);
        double lon = AMapUtil.DeCodeCoordinate(longitude);
        LatLng latlng = new LatLng(lat, lon);
        return latlng;
    }

    public static LatLng DeCode2LatLng(Long latitude, Long longitude) {
        long lat = latitude == null ? 0 : latitude;
        long lon = longitude == null ? 0 : longitude;
        return DeCode2LatLng(lat, lon);
    }

    // ===============================================================================================
    // ===============================================================================================

    /**
     * 判断edittext是否null
     */
    public static String checkEditText(EditText editText) {
        if (editText != null && editText.getText() != null
                && !(editText.getText().toString().trim().equals(""))) {
            return editText.getText().toString().trim();
        } else {
            return "";
        }
    }

    public static Spanned stringToSpan(String src) {
        return src == null ? null : Html.fromHtml(src.replace("\n", "<br />"));
    }

    public static String colorFont(String src, String color) {
        StringBuffer strBuf = new StringBuffer();

        strBuf.append("<font color=").append(color).append(">").append(src)
                .append("</font>");
        return strBuf.toString();
    }

    public static String makeHtmlNewLine() {
        return "<br />";
    }

    public static String makeHtmlSpace(int number) {
        final String space = "&nbsp;";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < number; i++) {
            result.append(space);
        }
        return result.toString();
    }

    public static String getFriendlyLength(int lenMeter) {
        if (lenMeter > 10000) // 10 km
        {
            int dis = lenMeter / 1000;
            return dis + ChString.Kilometer;
        }

        if (lenMeter > 1000) {
            float dis = (float) lenMeter / 1000;
            DecimalFormat fnum = new DecimalFormat("##0.0");
            String dstr = fnum.format(dis);
            return dstr + ChString.Kilometer;
        }

        if (lenMeter > 100) {
            int dis = lenMeter / 50 * 50;
            return dis + ChString.Meter;
        }

        int dis = lenMeter / 10 * 10;
        if (dis == 0) {
            dis = 10;
        }

        return dis + ChString.Meter;
    }

    public static boolean isEmptyOrNullString(String s) {
        return (s == null) || (s.trim().length() == 0);
    }

    /**
     * 把LatLng对象转化为LatLonPoint对象
     */
    public static LatLonPoint convertToLatLonPoint(LatLng latlon) {
        return new LatLonPoint(latlon.latitude, latlon.longitude);
    }

    /**
     * 把LatLonPoint对象转化为LatLon对象
     */
    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    /**
     * 把集合体的LatLonPoint转化为集合体的LatLng
     */
    public static ArrayList<LatLng> convertArrList(List<LatLonPoint> shapes) {
        ArrayList<LatLng> lineShapes = new ArrayList<LatLng>();
        for (LatLonPoint point : shapes) {
            LatLng latLngTemp = AMapUtil.convertToLatLng(point);
            lineShapes.add(latLngTemp);
        }
        return lineShapes;
    }

    /**
     * long类型时间格式化
     */
    public static String convertToTime(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA);
        Date date = new Date(time);
        return df.format(date);
    }

    public static final String HtmlBlack = "#000000";
    public static final String HtmlGray = "#808080";
}
