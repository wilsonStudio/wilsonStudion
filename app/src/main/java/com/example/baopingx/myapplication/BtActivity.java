package com.example.baopingx.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baopingx.myapplication.SDcardFileIO;

import static android.app.PendingIntent.getActivity;

import static android.os.Environment.*;
import static android.widget.Toast.makeText;

public class BtActivity extends Activity {
    private static final String TAG = "wilson_bt";
    private static final int FILE_SELECT_CODE = 0;
    private static String SDcard_base =Environment.getExternalStorageDirectory().getAbsolutePath();
    private static String strWilson_studio = "wilson";
    private short REQUEST_WRITE_EXTERNAL_STORAGE =0;
    private short REQUEST_READ_EXTERNAL_STORAGE =0;
    private Handler handler = new Handler();

    SDcardFileIO mSDcardFileIO = new SDcardFileIO();
    BluetoothDevice device = null;
    TextView mText = null;
    BluetoothAdapter adapter = null;
    private static List<String> permissions = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt);
        mText = (TextView) findViewById(R.id.textView2);
        Log.d(TAG, "now enter to BtActivity");
    }


    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
        else
        {
            Log.d(TAG, "not M build.do nothing");
            return true;
        }
    }


    public void openBtAdapter(View view)
    {
        Log.d(TAG, "now begin to getBtDevices");

        //得到BluetoothAdapter对象
        adapter = BluetoothAdapter.getDefaultAdapter();
        //判断BluetoothAdapter对象是否为空，如果为空，则表明本机没有蓝牙设备
        if(adapter != null)
        {
            Log.d(TAG, "BtAdapter was found in devcies");
            //调用isEnabled()方法判断当前蓝牙设备是否可用
            if(!adapter.isEnabled())
            {
                //如果蓝牙设备不可用的话,创建一个intent对象,该对象用于启动一个Activity,提示用户启动蓝牙适配器
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(intent);
                Log.d(TAG, "BtAdapter service was opened");
            }
            Log.d(TAG, "now we can getBtDevices");
            findViewById(R.id.button5).setClickable(true);
            //得到所有已经配对的蓝牙适配器对象
            Set<BluetoothDevice> devices = adapter.getBondedDevices();
            Log.d(TAG, "devices.size()="+devices.size());
            if(devices.size()>0)
            {
                for(Iterator<BluetoothDevice> it = devices.iterator();it.hasNext();)
                    //得到BluetoothDevice对象,也就是说得到配对的蓝牙适配器
                    device = (BluetoothDevice) it.next();
                //得到远程蓝牙设备的地址
                Log.d(TAG, "now BtAdapter begin to get");
                mText.setText(device.getName());
                Log.d(TAG, "now BtAdapter end to get");
            }
            else
            {
                Log.d(TAG, "devices.size()<0");
            }
        }
        else
        {
            Log.d(TAG, "no BtAdapter found in devcies");
        }
    }

    public void mBackActivity(View view){
        Log.d(TAG, "begin to back main activity");
        Intent intent = new Intent();
        intent.setClass(BtActivity.this, MainActivity.class);
        mText.setText("begin to banck main activity");
        startActivity(intent);
    }

    public void closeBtAdapter(View view)
    {
        if(null != adapter &&BluetoothAdapter.STATE_ON  == adapter.getState())
        {
            adapter.disable();
            Log.d(TAG, "begin to close bt function");
        }
        Log.d(TAG, "close yet or not on pls check");
    }

    public void CreateStudioDir(View view)
    {
        if(!getExternalStorageState().equals(MEDIA_MOUNTED))
        {
            Log.e(TAG, "no SDCard was found");
        }
        String state = Environment.getExternalStorageState();
        Log.d(TAG, "Environment.getExternalStorageState = "+Environment.getExternalStorageState());
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            Log.d(TAG, "SDCard can read and write");
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            Log.d(TAG, "SDCard can read");
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            Log.d(TAG, "SDCard can not only read but also write");
        }
        // Here, thisActivity is the current activity
        if (!hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)&&!hasPermission(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
        createDirIfNotExists(SDcard_base + File.separator + strWilson_studio);
    }


    public static boolean createDirIfNotExists(String path) {
        File file = new File(path);
        if (!file.exists()) {
            if(!file.mkdirs()) {
                Log.d(TAG, file + " " + "create failed");
                return false;
            }
            Log.d(TAG, file + " " + "create success");
            return true;
        }
        Log.d(TAG, file + " " + " was exists");
        return false;
    }


    public static void deleteDir(String path) {
        File dir = new File(path);
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;

        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDir(path); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    public void mDeletefile(View view) {
        if(!mSDcardFileIO.isSdCardExist()) return;
        String delFile = SDcard_base + File.separator +strWilson_studio;
        File file =new File(delFile);
        if(!file.exists()) {
        Log.e(TAG,"not exits");
            return;
        }
        if(file.isFile()) file.delete();
        else
        {
            for (File dir : file.listFiles()) {
                if (dir.isFile())
                    dir.delete(); // 删除所有文件
                else if (dir.isDirectory())
                    deleteDir(delFile); // 递规的方式删除文件夹
            }
            file.delete();// 删除目录本身
            Log.i(TAG, file.getName()+" delete done");
        }
    }

    private void sendMsg(int flag)
    {
        Message msg = new Message();
        msg.what = flag;
        handler.sendMessage(msg);
    }

    public int CopySdcardFile(String fromFile, String toFile)
    {
        try
        {
            long  totalSize = 0;
            Log.d(TAG, "begin");
            FileInputStream fosfrom = new FileInputStream(fromFile);
            File fileform =new File(fromFile);
            totalSize = fileform.length();//get Files Total Size
            ProgressDialog progressDlg = new ProgressDialog(this);


            FileOutputStream fosto = new FileOutputStream(toFile+File.separator+fileform.getName());
            byte bt[] = new byte[1024];
            int copyingSize = 0;
            int c= 0;

            while ((c = fosfrom.read(bt)) > 0)
            {
                fosto.write(bt, 0, c);
                copyingSize += c;
                sendMsg(copyingSize);
            }
            fosfrom.close();
            fosto.close();
            Toast.makeText(this, "copy file done", Toast.LENGTH_SHORT).show();
            return 0;

        } catch (Exception ex)
        {
            Toast.makeText(this, "copy file Exception", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Exception :"+ex);
            return -1;
        }
    }
    /** 调用文件选择软件来选择文件 **/
    public void mShowFileChooser(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //CreateStudioDir();//create work space
        try {
            startActivityForResult(intent, FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            makeText(this, "请安装文件管理器", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /** 根据返回选择的文件，来进行上传操作 **/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == Activity.RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
            ContentResolver resolver = getContentResolver();
            String fileType = resolver.getType(uri);
            assert fileType != null;
            Log.d(TAG,fileType);
            String workspace =SDcard_base + File.separator + strWilson_studio;

            if(fileType.startsWith("image"))
            {
                Cursor cursor = resolver.query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                cursor.moveToFirst();
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                Log.d(TAG, "now begin copy " + path +" to "+workspace);
                if (!hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (!permissions.isEmpty()) {
                        requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_READ_EXTERNAL_STORAGE);
                    }
                }
                CopySdcardFile(path, workspace);

            }
            else if(fileType.startsWith("video"))
            {
                Cursor cursor = resolver.query(uri, new String[]{MediaStore.Video.Media.DATA}, null, null, null);
                cursor.moveToFirst();
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                Log.d(TAG, "now begin copy " + path +" to "+workspace);
                CopySdcardFile(path, workspace);
            }
            else if(fileType.startsWith("audio"))
            {
                Cursor cursor = resolver.query(uri, new String[]{MediaStore.Audio.Media.DATA}, null, null, null);
                cursor.moveToFirst();
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                Log.d(TAG, "now begin copy " + path +" to"+workspace);
                CopySdcardFile(path, workspace);
            }

            else if(fileType.startsWith("text"))
            {
                Cursor cursor = resolver.query(uri, new String[]{MediaStore.Audio.Media.DATA}, null, null, null);
                cursor.moveToFirst();
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                Log.d(TAG, "now begin copy " + path +" to"+workspace);
                CopySdcardFile(path, workspace);
            }
            else
            {
                Log.d(TAG,"do noting");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}