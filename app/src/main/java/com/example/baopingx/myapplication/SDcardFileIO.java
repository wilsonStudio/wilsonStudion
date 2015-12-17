package com.example.baopingx.myapplication;


import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.CallableStatement;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by baopingx on 15-12-2.
 * function copy sdcard file
 */

public class SDcardFileIO {
    CallableStatement EncodingUtils;
    private static final String TAG = "wilson_SD";
    //读文件
    public  String readSDFile(String fileName) throws IOException {

        File file = new File(fileName);
        String res;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int length = fis.available();

        byte[] buffer = new byte[length];
        fis.read(buffer);

        res = new String(buffer);

        fis.close();
        return res;
    }

    //写文件
    public  void writeSDFile(String fileName, String write_str) {

        try {
            File file = new File(fileName);

            FileOutputStream fos = new FileOutputStream(file);

            byte[] bytes = write_str.getBytes();

            fos.write(bytes);

            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //写数据到SD中的文件
    public  void writeFileSdcardFile(String fileName, String write_str) {
        try {

            FileOutputStream fout = new FileOutputStream(fileName);
            byte[] bytes = write_str.getBytes();

            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //读SD中的文件
    public  String readFileSdcardFile(String fileName) {
        String res = "";
        try {
            FileInputStream fin = new FileInputStream(fileName);

            int length = fin.available();

            byte[] buffer = new byte[length];
            fin.read(buffer);
            //res = EncodingUtils.getString(buffer, "UTF-8");

            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
     *
     * @return
     */
    public  boolean isSdCardExist() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) return true;
        else return false;
    }

    /**
     * 获取SD卡根目录路径
     *
     * @return
     */
    public  String getSdCardPath() {
        boolean exist = isSdCardExist();
        String sdpath = "";
        if (exist) {
            sdpath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
        } else {
            sdpath = "不适用";
        }
        return sdpath;

    }

    public int copy(String fromFile, String toFile)
        {
            //要复制的文件目录
            File[] currentFiles;
            File root = new File(fromFile);
            //如同判断SD卡是否存在或者文件是否存在
            //如果不存在则 return出去
            if(!root.exists())
            {
                return -1;
            }
            //如果存在则获取当前目录下的全部文件 填充数组
            currentFiles = root.listFiles();
            Log.d(TAG, "currentFiles"+" "+currentFiles);
            assertNotNull(currentFiles);
            //目标目录
            File targetDir = new File(toFile);
            //创建目录
            if(!targetDir.exists())
            {
                targetDir.mkdirs();
            }
            //遍历要复制该目录下的全部文件
            for(int i= 0;i<currentFiles.length;i++)
            {
                if(currentFiles[i].isDirectory())//如果当前项为子目录 进行递归
                {
                    copy(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");

                }else//如果当前项为文件则进行文件拷贝
                {
                    CopySdcardFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
                }
            }
            return 0;
        }

        //文件拷贝
        //要复制的目录下的所有非子目录(文件夹)文件拷贝
    public int CopySdcardFile(String fromFile, String toFile)
        {
            try
            {
                InputStream fosfrom = new FileInputStream(fromFile);
                OutputStream fosto = new FileOutputStream(toFile);
                byte bt[] = new byte[1024];
                int c;
                Log.d(TAG, "begin");
                while ((c = fosfrom.read(bt)) > 0)
                {
                    fosto.write(bt, 0, c);
                }
                Log.d(TAG, "doing");
                fosfrom.close();
                fosto.close();
                Log.d(TAG, "done");
                return 0;

            } catch (Exception ex)
            {
                return -1;
            }
        }


    }
