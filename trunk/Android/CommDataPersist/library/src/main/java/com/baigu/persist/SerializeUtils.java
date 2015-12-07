package com.baigu.persist;

import android.content.Context;

import com.baigu.util.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

/**
 * Serialize Utils
 */
public class SerializeUtils {

    private SerializeUtils() {
        throw new AssertionError();
    }

    /**
     * 保存Serializable对象
     *
     * @param context
     * @param filename
     * @param object
     */
    public static void writeSerializableObject(Context context, String filename, Object object) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(context.openFileOutput(filename, Context.MODE_PRIVATE));
            out.writeObject(object);
            out.flush();

            // ------------------------------------
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 往保存的Serializable List 中添加一个 Item
     *
     * @param context
     * @param filename
     * @param item
     */
    public static void addSerializableListItem(Context context, String filename, Object item) {
        List<Object> list = readSerializableObject(context, filename);
        if (list == null) {
            list = new ArrayList<Object>();
        } else if (list.contains(item)) {
            return;
        }
        list.add(item);
        writeSerializableObject(context, filename, list);
    }

    /**
     * 读取Serializable对象
     *
     * @param context
     * @param filename
     * @return
     */
    public static <T extends Object> T readSerializableObject(Context context, String filename) {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(context.openFileInput(filename));
            @SuppressWarnings("unchecked")
            T readObject = (T) in.readObject();
            return readObject;
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
        }
        return null;
    }

    /**
     * 删除保存的文件
     *
     * @param context
     * @param filename
     */
    public static void deleteSerializableObject(Context context, String filename) {
        context.deleteFile(filename);
    }

    /**
     * deserialization from file
     *
     * @param filePath
     * @return
     * @throws RuntimeException if an error occurs
     */
    public static Object deserialization(String filePath) {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(filePath));
            Object o = in.readObject();
            in.close();
            return o;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("ClassNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * serialize to file
     *
     * @param filePath
     * @param obj
     * @return
     * @throws RuntimeException if an error occurs
     */
    public static void serialization(String filePath, Object obj) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeObject(obj);
            out.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }
}
