package com.example.myapplication.frangment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.DBdao;
import com.example.myapplication.MysqlUtils.School_commentdb;
import com.example.myapplication.R;
import com.example.myapplication.SchoolHomeActivity;
import com.example.myapplication.TencentSDK.BaseMapActivity;
import com.example.myapplication.table.School_comment;
import com.example.myapplication.table.userinfo;
import com.example.myapplication.utils.GameView;
import com.example.myapplication.utils.MyItemClick;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment  {
    private  Context mContext ;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ViewFlipper viewFlipper;
    private final static int MIN_MOVE = 200;   //最小距离
    SchoolHomeActivity.MyOnTouchListener myOnTouchListener;
    //private MyGestureListener mgListener;
    private GestureDetector mDetector;
    private  RecyclerView recyclerView;
    private  RecyclerAdapter recyclerAdapter;
    private List<userinfo> user;
    private ImageButton location_school,shop_school;
    private static GameView mGameView = null;
    //定位信息显示
    public TextView tvLocationInfo;
    public Button location;
    static List<School_comment> school_commentList ;
    //权限
    private RxPermissions rxPermissions;
    //定位管理
    private TencentLocationManager mLocationManager;
    //定位请求
    private TencentLocationRequest locationRequest;
    private int update=0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context=getActivity();
    private Call call;
    private Request request;
    private OkHttpClient client;
    ListDate listDate=new ListDate();
    private int[] resId = {R.drawable.ligon1,R.drawable.ligong2};
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //super.handleMessage(msg);
            List<School_comment> school_commentList1 =new ArrayList<>();
            if (msg.what==1000){
                school_commentList1.addAll((Collection<? extends School_comment>) msg.obj);
//                recyclerAdapter=new RecyclerAdapter(school_commentList1);
//                recyclerView.setAdapter(recyclerAdapter);
            }
            //调用方法,传入一个接口回调
            recyclerAdapter.setItemClickListener(new MyItemClick.MyItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(mContext.getApplicationContext(), "item点击事件"+position,Toast.LENGTH_LONG).show();
                }
            });
            school_commentList1.clear();
            school_commentList1.addAll((Collection<? extends School_comment>) msg.obj);
            //局部更新recylerview
            recyclerAdapter.notifyItemChanged(msg.what,msg.obj);
        }
    };
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        viewFlipper=view.findViewById(R.id.view_home);
        location_school= (ImageButton) view.findViewById(R.id.location_school);
        shop_school=view.findViewById(R.id.shop_school);
        mContext= getContext().getApplicationContext();
        mGameView=new GameView(mContext);
        mDetector = new GestureDetector(getActivity(),
                new MyGestureListener());

        myOnTouchListener = new SchoolHomeActivity.MyOnTouchListener() {
            @Override
            public boolean onTouch(MotionEvent ev) {
                return mDetector.onTouchEvent(ev);
            }
        };
        ((SchoolHomeActivity)getActivity()).registerMyOnTouchListener(myOnTouchListener);

        //动态导入添加子View
        for(int i = 0;i < resId.length;i++){
            viewFlipper.addView(getImageView(resId[i]));
        }
        viewFlipper.startFlipping();
        recyclerView=view.findViewById(R.id.recyle_school);
        recyclerView.scrollToPosition(0);
        //布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        initData();
        initDataU();
        location=view.findViewById(R.id.lication);
        tvLocationInfo=view.findViewById(R.id.location_tv);
        //设置增加删除条目动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
       // recyclerView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL));

        location_school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), BaseMapActivity.class));
            }
        });
/*        recyclerAdapter.setItemClickListener(new RecyclerAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), "点击了" + position, Toast.LENGTH_SHORT).show();
            }
        });
        String str = (String)getArguments().get("address");
        tvLocationInfo.setText(new Location().getName());*/

        return view;
    }

    private void initDataU() {

    }

    private void initData() {

         school_commentList=new ArrayList<>();
         client=new OkHttpClient();
         request=new Request.Builder().url("http://192.168.123.74:8080/hello/selectInformations").build();
         call=client.newCall(request);
         call.enqueue(new Callback() {
             @Override
             public void onFailure(@NonNull Call call, @NonNull IOException e) {

             }

             @Override
             public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
             String data=response.body().string();
             Gson gson =new Gson();
             School_comment schoolComment;
                 try {
                     JSONArray jsonArray=new JSONArray(data);
                     for (int i=0;i<jsonArray.length();i++) {
                         JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                         schoolComment=gson.fromJson(String.valueOf(jsonObject),School_comment.class);
                         school_commentList.add(schoolComment);
                     }
                     listDate.setSchool_commentList(school_commentList);
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }
         });
         Log.d("----------11", String.valueOf(listDate.getSchool_commentList()));
          new Thread(new Runnable() {
              @Override
              public void run() {
                 School_commentdb school_commentdb=new School_commentdb();
                 school_commentList.addAll(school_commentdb.selectAll());
                 List<userinfo> userinfoList=new ArrayList<>();
                 userinfoList.addAll(new DBdao().SelectAll());
                  getActivity().runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          recyclerAdapter=new RecyclerAdapter(school_commentList,userinfoList);
                          recyclerView.setAdapter(recyclerAdapter);
                          recyclerAdapter.setItemClickListener(new MyItemClick.MyItemClickListener() {
                              @Override
                              public void onItemClick(View view, int position) {
                                  Toast.makeText(mContext.getApplicationContext(), "item点击事件"+position,Toast.LENGTH_LONG).show();
                              }
                          });
                      }
                  });
              }
          }).start();

    }


    //重写onTouchEvent触发MyGestureListener里的方法

    //自定义一个GestureListener,这个是View类下的，别写错哦！！！
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        //OnFling中根据X轴方向移动的距离和速度来判断当前用户是向左滑还是向右滑，
        // 从而利用showPrevious()或者showNext()来显示上一张或者下一张图片
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX() - e2.getX() > MIN_MOVE){
                viewFlipper.setInAnimation(mContext,R.anim.right_in);
                viewFlipper.setOutAnimation(mContext, R.anim.right_out);
                viewFlipper.showNext();
            }else if(e2.getX() - e1.getX() > MIN_MOVE){
                viewFlipper.setInAnimation(mContext,R.anim.left_in);
                viewFlipper.setOutAnimation(mContext, R.anim.left_out);
                viewFlipper.showPrevious();
            }

            return true;
        }
    }
    private ImageView getImageView(int resId){
        ImageView img = new ImageView(mContext);
        img.setBackgroundResource(resId);
        return img;
    }

     private   class RecyclerAdapter extends  RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
         private MyItemClick.MyItemClickListener mItemClickListener;
         private List<School_comment> uesrAdapter;
         private List<userinfo> userinfoList1;

         public RecyclerAdapter(List<School_comment> school_commentList2,List<userinfo> list) {
             this.userinfoList1=list;
             this.uesrAdapter = school_commentList2;
         }

         @NonNull
         @Override
         public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
             View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyleview_item, parent, false);
             ViewHolder viewHolder = new ViewHolder(itemView);
             return viewHolder;

         }

         @Override
         public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
             //获取系统时间
             SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
             Date date = new Date(System.currentTimeMillis());
             holder.name.setText(uesrAdapter.get(position).getContent());
             new Thread(new Runnable() {
                 @Override
                 public void run() {
                     int count=new School_commentdb().SelectUser("liweichao1",uesrAdapter.get(position).getId());
                     getActivity().runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                            if (count==1){
                                holder.btn_praise.setImageResource(R.drawable.praise1);
                            }else{
                             holder.btn_praise.setImageResource(R.drawable.praise);
                         }
                         }
                     });
                 }
             }).start();
             holder.btn_praise.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                 Log.d("id----", String.valueOf(uesrAdapter.get(position).getId()));
                 new Thread(new Runnable() {
                     @Override
                     public void run() {
                         int count=new School_commentdb().SelectUser("liweichao1",uesrAdapter.get(position).getId());
                         if (count!=1) {
                             new School_commentdb().UpdatePraise(uesrAdapter.get(position).getId());
                             new School_commentdb().InsertPraise(uesrAdapter.get(position).getId(), "liweichao1");
                             School_commentdb school_commentdb = new School_commentdb();
                             school_commentList.clear();
                             school_commentList.addAll(school_commentdb.selectAll());
                             Message msg = new Message();
                             msg.what = position;
                             msg.obj = school_commentList;
                             handler.sendMessage(msg);
                              getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                        holder.btn_praise.setImageResource(R.drawable.praise1);
                                }
                            });
//                             holder.btn_praise.setImageResource(R.drawable.praise1);
                         }else{
                             new School_commentdb().UpdatePraise1(uesrAdapter.get(position).getId());
                             new School_commentdb().deletePraise("liweichao1",uesrAdapter.get(position).getId());
                             School_commentdb school_commentdb = new School_commentdb();
                             school_commentList.clear();
                             school_commentList.addAll(school_commentdb.selectAll());
                             Message msg = new Message();
                             msg.what = position;
                             msg.obj = school_commentList;
                             handler.sendMessage(msg);
                             getActivity().runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                         holder.btn_praise.setImageResource(R.drawable.praise);
                                 }
                             });
                         }
                     }
                 }).start();

                 }
             });
             //holder.number.setText(uesrAdapter.get(position).getPhone());
             if (uesrAdapter.get(position).getIndex_user() == 2) {
                 Uri imgUri = Uri.parse(uesrAdapter.get(position).getImagepath());
                 try {
                     Bitmap bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(imgUri));
                     holder.number.setImageBitmap(bitmap);
                 } catch (FileNotFoundException e) {
                     e.printStackTrace();
                 }
             } else if (uesrAdapter.get(position).getIndex_user() == 1) {
                 Bitmap bitmap = BitmapFactory.decodeFile(uesrAdapter.get(position).getImagepath());
                 holder.number.setImageBitmap(bitmap);
             } else {
                 Glide.with(mContext).load(uesrAdapter.get(position).getImagepath()).into(holder.number);
             }
             if (userinfoList1.get(position).getIndex_user() == 2) {
                 Uri imgUri = Uri.parse(userinfoList1.get(position).getImagepath());
                 try {
                     Bitmap bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(imgUri));
                     holder.user_img.setImageBitmap(bitmap);
                 } catch (FileNotFoundException e) {
                     e.printStackTrace();
                 }
             } else if (userinfoList1.get(position).getIndex_user() == 1) {
                 Bitmap bitmap = BitmapFactory.decodeFile(userinfoList1.get(position).getImagepath());
                 holder.user_img.setImageBitmap(bitmap);
             } else if (userinfoList1.get(position).getIndex_user() == 0){
                 Glide.with(mContext).load(userinfoList1.get(position).getImagepath()).into(holder.user_img);
             }else{
                 holder.user_img.setImageResource(R.drawable.manimage);
             }
             holder.trump.setText(simpleDateFormat.format(uesrAdapter.get(position).getComment_time()));
             //holder.trump.setText(String.valueOf(uesrAdapter.get(position).getPraise()));
         }

         @Override
         public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
             super.onBindViewHolder(holder, position, payloads);
             if (payloads!=null && payloads.size()>0){
                 holder.trump.setText(String.valueOf(school_commentList.get(position).getPraise()));
             }else
                 onBindViewHolder(holder,position);
         }

         @Override
         public int getItemCount() {
             return uesrAdapter.size();
         }

         public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
             //private MyItemClickListener mListener;
             TextView name;
             ImageView number,user_img;
             TextView trump;
             private ImageButton btn_share,btn_praise,btn_textsms;

             public ViewHolder(@NonNull View itemView) {
                 super(itemView);
                 this.name = itemView.findViewById(R.id.tv_name);
                 this.number = itemView.findViewById(R.id.iv_school);
                 this.trump = itemView.findViewById(R.id.tv_trump);
                 this.btn_praise=itemView.findViewById(R.id.btn_praise);
                 this.btn_share=itemView.findViewById(R.id.btn_share);
                 this.btn_textsms=itemView.findViewById(R.id.btn_textsms);
                 this.user_img=itemView.findViewById(R.id.user_img);
                 //this.mListener = mItemClickListener;
                 itemView.setOnClickListener(this);
             }

             @Override
             public void onClick(View view) {
                 mItemClickListener.onItemClick(view,getAdapterPosition());
                 Toast.makeText(getActivity(),"position",Toast.LENGTH_LONG).show();
             }

         }
         private class GameThread implements Runnable {

             @Override
             public void run() {
                 // TODO Auto-generated method stub
                 while(!Thread.currentThread().isInterrupted()){
                     try{
                         Thread.sleep(400);
                     } catch(Exception e){

                         Thread.currentThread().interrupt();
                     }
                     //使用postInvalidate可以直接在线程中刷新
                     mGameView.postInvalidate();
                 }
             }

         }
         /**
          * 创建一个回调接口
          */
//         public interface MyItemClickListener {
//             void onItemClick(View view, int position);
//         }

         /**
          * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
          *
          * @param myItemClickListener
          */
         public void setItemClickListener(MyItemClick.MyItemClickListener myItemClickListener) {
             this.mItemClickListener = myItemClickListener;
         }
     }
     public class  ListDate{
        private List<School_comment> school_commentList;
        private List<userinfo> userinfoList;

         public List<School_comment> getSchool_commentList() {
             return school_commentList;
         }

         public void setSchool_commentList(List<School_comment> school_commentList) {
             this.school_commentList = school_commentList;
         }

         public List<userinfo> getUserinfoList() {
             return userinfoList;
         }

         public void setUserinfoList(List<userinfo> userinfoList) {
             this.userinfoList = userinfoList;
         }
     }
}