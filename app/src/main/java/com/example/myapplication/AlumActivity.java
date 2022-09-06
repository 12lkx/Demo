package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlumActivity extends AppCompatActivity {
    public static final int TAKE_PHOTO =1;
    public static final int CHOOSE_PHOTO=2;
    private ImageView picture;
    private Button tiao;
    private Uri imageUri;
    int index ;		//	作为拍照还是相册的标识
    String info = "";		//	uri变成字符串

    public AlumActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alum);
        Button takePhoto = findViewById(R.id.take_photo);
        Button chooseFromAlbum = findViewById(R.id.choose_from_album);
        picture = findViewById(R.id.picture);

        //  拍照
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //          获取本地时间，作为图片的名字，防止拍了多张照片时，出现图片覆盖导致之前图片消失的问题
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());
                String str = format.format(curDate);

                /**
                 *          创建File对象，用于存储拍照后的照片
                 *          第一个参数：  是这张照片存放在手机SD卡的对应关联缓存应用
                 *          第二个参数：  这张图片的命名
                 */
                File outputImage = new File(getExternalCacheDir(),str+".jpg");
                try {
                    if (outputImage.exists()){          //  检查与File对象相连接的文件和目录是否存在于磁盘中
                        outputImage.delete();           //  删除与File对象相连接的文件和目录
                    }
                    outputImage.createNewFile();        //  如果与File对象相连接的文件不存在，则创建一个空文件
                } catch ( IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24){       //  如果运行设备的系统版本高于 Android7.0
                    /**
                     *          将File对象转换成一个封装过的Uri对象
                     *          第一个参数：  要求传入Context参数
                     *          第二个参数：  可以是任意唯一的字符串
                     *          第三个参数：  我们刚刚创建的File对象
                     */
                    imageUri = FileProvider.getUriForFile(AlumActivity.this,"com.example.myapplication.fileprovider",outputImage);
                }else{                  //  如果运行设备的系统版本低于 Android7.0
                    //  将File对象转换成Uri对象，这个Uri对象表示着output_image.jpg 这张图片的本地真实路径
                    imageUri = Uri.fromFile(outputImage);
                }
                /**
                 *      启动相机程序
                 */
                //  将Intent的action指定为 拍照到指定目录 —— android.media.action.IMAGE_CAPTURE
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                //  指定图片的输出地址
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                //  在通过startActivityForResult()，来启动活动，因此拍完照后会有结果返回到 onActivityResult()方法中
                startActivityForResult(intent,TAKE_PHOTO);  //  打开相机，用自定义常量 —— TAKE_PHOTO来作为case处理图片的标识
            }
        });

        //  相册
        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  动态申请WRITE_EXTERNAL_STORAGE 这个危险权限。因为相册中的照片时存储在SD卡上的，我们从SD卡中读取照片就需要申请这个权限
                //  WRITE_EXTERNAL_STORAGE  ——  同时授权程序对SD卡读和写的能力
                if (ContextCompat.checkSelfPermission(AlumActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AlumActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();        //  用户授权了权限申请之后就会调用该方法
                }
            }
        });
        tiao=findViewById(R.id.btn_tiao);
        tiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                   DBdao dBdao=new DBdao();
                   dBdao.insertImageview(info,index);
                    }
                }).start();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //  根据Uri找到这张照片的位置，将它解析成Bitmap对象，然后将把它设置到imageView中显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        info = "" + imageUri;
                        index = 2;
                        picture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                /**
                 *          之所以这样做是是因为，Android 系统从4.4版本开始，选取相册图片不再返回图片真实的Uri了，而是一个封装过的Uri，因此
                 *          如果是4.4版本以上的手机就需要对这个Uri进行解析才行
                 */
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {      //  如果是在4.4及以 上 系统的手机就调用该方法来处理图片
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);      //  如果是在4.4以 下 系统的手机就调用该方法来处理图片
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     *          如何解析这个封装过的Uri
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)){
            //  如果返回的Uri是 document 类型的话，那就取出 document id 进行处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];        //  解析出数字格式id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //  如果是 content 类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            //  如果是 file 类型的 Uri ，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);        //  拿到图片路径后，在调用 displayImage() 方法将图片显示到界面上
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    private void displayImage(String imagePath) {
        if (imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            index = 1;
            info = imagePath;
            picture.setImageBitmap(bitmap);
            Log.d("测试照片路径",imagePath);
        }else{
            Toast.makeText(this,"failed to gei image",Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //  通过 Uri 和 selection 来获取真实图片的路径
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null){
            if (cursor.moveToNext()){
                //  MediaStore.Images.Media.insertImage —— 得到保存图片的原始路径
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


    private void openAlbum() {
        /**
         *      启动相册程序
         */
        //  action —— android.intent.action.GET_CONTENT
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        //  该函数表示要查找文件的mime类型（如*/*），这个和组件在manifest里定义的相对应，但在源代码里
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);    //打开相册,用自定义常量 —— CHOOSE_PHOTO来作为case处理图片的标识
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}

