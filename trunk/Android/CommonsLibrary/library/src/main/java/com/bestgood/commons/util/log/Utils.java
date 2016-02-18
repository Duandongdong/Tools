package com.bestgood.commons.util.log;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

final class Utils {
    static final String BLANK_SPACE = "\t";


    // 基本数据类型
    private final static String[] types = {"int", "java.lang.String", "boolean", "char",
            "float", "double", "long", "short", "byte"};

    /**
     * 将对象转化为String
     *
     * @param object
     * @return
     */
    static <T> String objectToString(T object, String blankSpace) {
        if (object == null) {
            return blankSpace + "Object{object is null}";
        }
        if (object.toString().startsWith(object.getClass().getName() + "@")) {
            StringBuilder builder = new StringBuilder(object.getClass().getSimpleName() + "{");
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                boolean flag = false;
                for (String type : types) {
                    if (field.getType().getName().equalsIgnoreCase(type)) {
                        flag = true;
                        Object value = null;
                        try {
                            value = field.get(object);
                        } catch (IllegalAccessException e) {
                            value = e;
                        } finally {
                            builder.append(String.format("%s=%s, ", field.getName(),
                                    value == null ? "null" : value.toString()));
                        }
                    }
                }
                if (!flag) {
                    builder.append(String.format("%s=%s, ", field.getName(), "Object"));
                }
            }
            return builder.replace(builder.length() - 2, builder.length() - 1, "}").toString();
        } else {
            return blankSpace + object.toString();
        }
    }


    static Pair<Pair<Integer, Integer>, String> arrayToObject(Object object, String blankSpace) {
        StringBuilder builder = new StringBuilder();
        int cross = 0, vertical = 0;
        if (object instanceof int[][]) {
            int[][] ints = (int[][]) object;
            cross = ints.length;
            vertical = cross == 0 ? 0 : ints[0].length;
            for (int[] ints1 : ints) {
                builder.append(arrayToString(ints1, blankSpace).second + "\n");
            }
        } else if (object instanceof byte[][]) {
            byte[][] ints = (byte[][]) object;
            cross = ints.length;
            vertical = cross == 0 ? 0 : ints[0].length;
            for (byte[] ints1 : ints) {
                builder.append(arrayToString(ints1, blankSpace).second + "\n");
            }
        } else if (object instanceof short[][]) {
            short[][] ints = (short[][]) object;
            cross = ints.length;
            vertical = cross == 0 ? 0 : ints[0].length;
            for (short[] ints1 : ints) {
                builder.append(arrayToString(ints1, blankSpace).second + "\n");
            }
        } else if (object instanceof long[][]) {
            long[][] ints = (long[][]) object;
            cross = ints.length;
            vertical = cross == 0 ? 0 : ints[0].length;
            for (long[] ints1 : ints) {
                builder.append(arrayToString(ints1, blankSpace).second + "\n");
            }
        } else if (object instanceof float[][]) {
            float[][] ints = (float[][]) object;
            cross = ints.length;
            vertical = cross == 0 ? 0 : ints[0].length;
            for (float[] ints1 : ints) {
                builder.append(arrayToString(ints1, blankSpace).second + "\n");
            }
        } else if (object instanceof double[][]) {
            double[][] ints = (double[][]) object;
            cross = ints.length;
            vertical = cross == 0 ? 0 : ints[0].length;
            for (double[] ints1 : ints) {
                builder.append(arrayToString(ints1, blankSpace).second + "\n");
            }
        } else if (object instanceof boolean[][]) {
            boolean[][] ints = (boolean[][]) object;
            cross = ints.length;
            vertical = cross == 0 ? 0 : ints[0].length;
            for (boolean[] ints1 : ints) {
                builder.append(arrayToString(ints1, blankSpace).second + "\n");
            }
        } else if (object instanceof char[][]) {
            char[][] ints = (char[][]) object;
            cross = ints.length;
            vertical = cross == 0 ? 0 : ints[0].length;
            for (char[] ints1 : ints) {
                builder.append(arrayToString(ints1, blankSpace).second + "\n");
            }
        } else {
            Object[][] objects = (Object[][]) object;
            cross = objects.length;
            vertical = cross == 0 ? 0 : objects[0].length;
            for (Object[] objects1 : objects) {
                builder.append(arrayToString(objects1, blankSpace).second + "\n");
            }
        }
        return Pair.create(Pair.create(cross, vertical), builder.toString());
    }

    /**
     * 数组转化为字符串
     *
     * @param object
     * @return
     */
    static Pair arrayToString(Object object, String blankSpace) {
        StringBuilder builder = new StringBuilder(blankSpace);
        builder.append(BLANK_SPACE);
        builder.append("[");
        int length = 0;
        if (object instanceof int[]) {
            int[] ints = (int[]) object;
            length = ints.length;
            for (int i : ints) {
                builder.append(i + ",");
            }
        } else if (object instanceof byte[]) {
            byte[] bytes = (byte[]) object;
            length = bytes.length;
            for (byte item : bytes) {
                builder.append(item + ",");
            }
        } else if (object instanceof short[]) {
            short[] shorts = (short[]) object;
            length = shorts.length;
            for (short item : shorts) {
                builder.append(item + ",");
            }
        } else if (object instanceof long[]) {
            long[] longs = (long[]) object;
            length = longs.length;
            for (long item : longs) {
                builder.append(item + ",");
            }
        } else if (object instanceof float[]) {
            float[] floats = (float[]) object;
            length = floats.length;
            for (float item : floats) {
                builder.append(item + ",");
            }
        } else if (object instanceof double[]) {
            double[] doubles = (double[]) object;
            length = doubles.length;
            for (double item : doubles) {
                builder.append(item + ",");
            }
        } else if (object instanceof boolean[]) {
            boolean[] booleans = (boolean[]) object;
            length = booleans.length;
            for (boolean item : booleans) {
                builder.append(item + ",");
            }
        } else if (object instanceof char[]) {
            char[] chars = (char[]) object;
            length = chars.length;
            for (char item : chars) {
                builder.append(item + ",");
            }
        } else {
            Object[] objects = (Object[]) object;
            length = objects.length;
            for (Object item : objects) {
                builder.append(objectToString(item, "") + ",");
            }
        }
        return Pair.create(length, builder.replace(builder.length() - 1, builder.length(), "]").toString());
    }


    /**
     * 遍历数组
     *
     * @param result
     * @param object
     */
    private static void traverseArray(StringBuilder result, Object object) {
        if (!isArray(object)) {
            result.append(object.toString());
            return;
        }
        if (getArrayDimension(object) == 1) {
            switch (getType(object)) {
                case 'I':
                    result.append(Arrays.toString((int[]) object)).append("\n");
                    return;
                case 'D':
                    result.append(Arrays.toString((double[]) object)).append("\n");
                    return;
                case 'Z':
                    result.append(Arrays.toString((boolean[]) object)).append("\n");
                    return;
                case 'B':
                    result.append(Arrays.toString((byte[]) object)).append("\n");
                    return;
                case 'S':
                    result.append(Arrays.toString((short[]) object)).append("\n");
                    return;
                case 'J':
                    result.append(Arrays.toString((long[]) object)).append("\n");
                    return;
                case 'F':
                    result.append(Arrays.toString((float[]) object)).append("\n");
                    return;
                case 'L':
                    result.append(Arrays.toString((Object[]) object)).append("\n");
                default:
                    return;
            }
        }
        for (int i = 0; i < ((Object[]) object).length; i++) {
            traverseArray(result, ((Object[]) object)[i]);
        }
    }

    static String traverseArray(Object object) {
        StringBuilder result = new StringBuilder();
        traverseArray(result, object);
        return result.toString();
    }

    /**
     * 是否为数组
     *
     * @param object
     * @return
     */
    static boolean isArray(Object object) {
        return object.getClass().isArray();
    }

    /**
     * 获取数组类型
     *
     * @param object 如L为int型
     * @return
     */
    static char getType(Object object) {
        if (isArray(object)) {
            String str = object.toString();
            return str.substring(str.lastIndexOf("[") + 1, str.lastIndexOf("[") + 2).charAt(0);
        }
        return 0;
    }

    /**
     * 获取数组的纬度
     *
     * @param objects
     * @return
     */
    static int getArrayDimension(Object objects) {
        int dim = 0;
        for (int i = 0; i < objects.toString().length(); ++i) {
            if (objects.toString().charAt(i) == '[') {
                ++dim;
            } else {
                break;
            }
        }
        return dim;
    }

    static String buildObject(Object object, String blankSpace) {
        if (object == null) {
            return objectToString(object, blankSpace);
        }

        final String simpleName = blankSpace + object.getClass().getSimpleName();
        if (object.getClass().isArray()) {
            String msg;
            int dim = getArrayDimension(object);
            switch (dim) {
                case 1:
                    Pair pair = arrayToString(object, blankSpace);
                    msg = simpleName.replace("[]", "[" + pair.first + "] {\n");
                    msg += pair.second + "\n";
                    break;
                case 2:
                    Pair pair1 = arrayToObject(object, blankSpace);
                    Pair pair2 = (Pair) pair1.first;
                    msg = simpleName.replace("[][]", "[" + pair2.first + "][" + pair2.second + "] {\n");
                    msg += pair1.second;
                    break;
                default:
                    msg = blankSpace + "Temporarily not support more than two dimensional Array!";
                    break;
            }
            return msg + blankSpace + "}";
        } else if (object instanceof Collection) {
            Collection collection = (Collection) object;
            String msg = "%s size = %d [\n";
            msg = String.format(msg, simpleName, collection.size());
            if (!collection.isEmpty()) {
                Iterator<Object> iterator = collection.iterator();
                int flag = 0;
                while (iterator.hasNext()) {
                    String itemString = blankSpace + BLANK_SPACE + "[%d]:%s%s";
                    Object item = iterator.next();
                    msg += String.format(itemString, flag, objectToString(item, ""), flag++ < collection.size() - 1 ? ",\n" : "");
                }
            }
            return msg + "\n" + blankSpace + "]";
        } else if (object instanceof Map) {
            String msg = simpleName + "{\n";
            Map<Object, Object> map = (Map<Object, Object>) object;
            Set<Object> keys = map.keySet();
            for (Object key : keys) {
                String itemString = blankSpace + BLANK_SPACE + "[%s : %s]\n";
                Object value = map.get(key);
                msg += String.format(itemString, objectToString(key, ""), objectToString(value, ""));
            }
            return msg + blankSpace + "}";
        } else {
            return objectToString(object, blankSpace);
        }
    }

    static String buildIntent(Intent intent) {
        StringBuffer sb = new StringBuffer();
        if (intent == null) {
            sb.append("Intent is null.");
            return sb.toString();
        }
        sb.append("Intent action:");
        sb.append(intent.getAction());
        sb.append('\n');

        // build extras
        Bundle extras = intent.getExtras();
        sb.append("Intent Bundle Extras:");
        sb.append(buildBundle(extras, BLANK_SPACE));
        return sb.toString();
    }

    static StringBuffer buildBundle(Bundle bundle, String blankSpace) {
        StringBuffer sb = new StringBuffer();
        sb.append('\n');

        if (bundle == null) {
            sb.append(blankSpace);
            sb.append("bundle:null");
            return sb;
        }
        Set<String> keys = bundle.keySet();
        if (keys == null) {
            sb.append(blankSpace);
            sb.append("bundle:bundle.keySet() == null");
            return sb;
        }
        if (keys.isEmpty()) {
            sb.append(blankSpace);
            sb.append("bundle:bundle.keySet().isEmpty()");
            return sb;
        }
        //sb.append("extras:\n");
        for (String key : keys) {
            sb.append(blankSpace);
            Object value = bundle.get(key);
            sb.append(key);
            sb.append("=");
            if (value == null) {
                sb.append("null");
            } else {
                sb.append(value.getClass().getSimpleName());
                if (value instanceof Bundle) {
                    sb.append(buildBundle((Bundle) value, blankSpace + BLANK_SPACE));
                } else {
                    sb.append(":\n");
                    String preBlank = blankSpace + key + "=" + value.getClass().getSimpleName() + ":";
                    preBlank = preBlank.replaceAll("[^\\s]", " ");
                    sb.append(buildObject(value, preBlank) + BLANK_SPACE);
                }
//                sb.append(value instanceof Bundle ? sb.append(buildBundle((Bundle) value, blankSpace + BLANK_SPACE)) : sb.append(value));
            }
            sb.append('\n');
        }
        return sb;
    }

    /**
     * 自动生成tag
     *
     * @return
     */
    static String generateTag() {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        return caller.getFileName().substring(0, caller.getFileName().lastIndexOf("."));
    }

    static void appendComma(StringBuffer sb, String str) {
        if (sb == null || TextUtils.isEmpty(str)) {
            return;
        }
        sb.append(str);
        sb.append(',');
    }

    static void appendBlankSpace(StringBuffer sb, String str) {
        if (sb == null || TextUtils.isEmpty(str)) {
            return;
        }
        sb.append(str);
        sb.append(BLANK_SPACE);
    }

    static void appendLineBreak(StringBuffer sb, String str) {
        if (sb == null || TextUtils.isEmpty(str)) {
            return;
        }
        sb.append(str);
        sb.append('\n');
    }
}
