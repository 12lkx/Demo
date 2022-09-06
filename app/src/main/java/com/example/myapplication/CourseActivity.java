package com.example.myapplication;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.MysqlUtils.School_Coursedb;
import com.example.myapplication.table.School_Course;
import com.example.myapplication.utils.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CourseActivity extends AppCompatActivity {
    /** 第一个无内容的格子 */
    protected TextView empty;
    /** 星期一的格子 */
    protected TextView monColum;
    /** 星期二的格子 */
    protected TextView tueColum;
    /** 星期三的格子 */
    protected TextView wedColum;
    /** 星期四的格子 */
    protected TextView thrusColum;
    /** 星期五的格子 */
    protected TextView friColum;
    /** 星期六的格子 */
    protected TextView satColum;
    /** 星期日的格子 */
    protected TextView sunColum;
    /** 课程表body部分布局 */
    protected RelativeLayout course_table_layout;
    /** 屏幕宽度 **/
    protected int screenWidth;
    /** 课程格子平均宽度 **/
    protected int aveWidth;
    private Toolbar couser_toolbar;
    int gridHeight1 = 0;
    DisplayMetrics dm = new DisplayMetrics();
    String spinnerText;
    //(0)对应12节；(2)对应34节；(4)对应56节；(6)对应78节；(8)对应于9 10节
    int[] jieci = {0,2,4,6,8};
    List<String> spinnerList ;
    private BaseAdapter adapter;
    private static final String[] cities = {"1", "2", "3", "4", "5", "6", "7"};
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        initThread();
        //Log.d("ces", String.valueOf(spinnerList));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            window.setStatusBarColor(Color.parseColor("#FDA92C"));
        }
        couser_toolbar=findViewById(R.id.course_toolbar);

        //spinner.setPrompt("第一周");

        String title="第一周";
        //couser_toolbar.setTitle(title);
        //couser_toolbar.setNavigationIcon();
        //获得列头的控件
        empty = (TextView) this.findViewById(R.id.test_empty);
        monColum = (TextView) this.findViewById(R.id.test_monday_course);
        tueColum = (TextView) this.findViewById(R.id.test_tuesday_course);
        wedColum = (TextView) this.findViewById(R.id.test_wednesday_course);
        thrusColum = (TextView) this.findViewById(R.id.test_thursday_course);
        friColum = (TextView) this.findViewById(R.id.test_friday_course);
        satColum  = (TextView) this.findViewById(R.id.test_saturday_course);
        sunColum = (TextView) this.findViewById(R.id.test_sunday_course);
        course_table_layout = (RelativeLayout) this.findViewById(R.id.test_course_rl);

        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //屏幕宽度
        int width = dm.widthPixels;
        //平均宽度
        int aveWidth = width / 8;
        //第一个空白格子设置为25宽
        empty.setWidth(aveWidth * 3/4);
        monColum.setWidth(aveWidth * 33/32 + 1);
        tueColum.setWidth(aveWidth * 33/32 + 1);
        wedColum.setWidth(aveWidth * 33/32 + 1);
        thrusColum.setWidth(aveWidth * 33/32 + 1);
        friColum.setWidth(aveWidth * 33/32 + 1);
        satColum.setWidth(aveWidth * 33/32 + 1);
        sunColum.setWidth(aveWidth * 33/32 + 1);
        this.screenWidth = width;
        this.aveWidth = aveWidth;

        initView();
        //设置课表界面
        //动态生成10 * maxCourseNum个textview

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                List<School_Course> list=new ArrayList<>();
//                list.addAll(new School_Coursedb().SelectCourse());
//               runOnUiThread(new Runnable() {
//                   @Override
//                   public void run() {
//
//
//
//                   }
//               });
//            }
//        }).start();
//        setCourseMessage(1,jieci[1],"地图制图基础\n9-19(3,4)\n于焕菊\nJ14-305室");
//        setCourseMessage(2,jieci[1],"大学英语(3-3)\n1-19(3,4)\n徐育新\nJ1-310室");
//        setCourseMessage(3,jieci[1],"概率论与数理统计\n1-9(3,4)\n郑艳林\nJ1-117室");
//        setCourseMessage(3,jieci[1],"概率论与数理统计\n1-9(3,4)\n郑艳林\nJ1-117室");
//        setCourseMessage(4,jieci[1],"线性代数\n1-11(3,4)\n王新赠\nJ14-121室");
//        setCourseMessage(5,jieci[1],"大学物理（B）（2-2）\n1-19(3,4)\n周明东\nJ1-307室");
//        setCourseMessage(6,jieci[0],"大学物理（B）（2-2）\n1-19(3,4)\n周明东\nJ1-307室");
//        setCourseMessage(7,jieci[2],"大学物理（B）（2-2）\n1-19(3,4)\n周明东\nJ1-307室");
//
//        setCourseMessage(1,jieci[0],"地图制图基础\n9-19(3,4)\n于焕菊\nJ14-305室");
//        setCourseMessage(2,jieci[2],"大学英语(3-3)\n1-19(3,4)\n徐育新\nJ1-310室");
//        setCourseMessage(3,jieci[3],"概率论与数理统计\n1-9(3,4)\n郑艳林\nJ1-117室");
//        setCourseMessage(4,jieci[2],"线性代数\n1-11(3,4)\n王新赠\nJ14-121室");
//        setCourseMessage(5,jieci[0],"大学物理（B）（2-2）\n1-19(3,4)\n周明东\nJ1-307室");

    }


    private void initThread() {
        spinnerList=new ArrayList<>();
        spinner=this.findViewById(R.id.spinner);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<School_Course> list=new ArrayList<>();
                list.addAll(new School_Coursedb().SelectCourse());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer max=0;
                        for(int i=0;i<list.size();i++){
                            int a= Integer.parseInt(list.get(i).getWeek().substring(2,4));
                            if(a> max){
                                max =a;
                            }

                        }
                        for(int j=1;j<=max;j++){
                            spinnerList.add("第"+j+"周");
                        }
                        adapter = new SpinnerAdapter(CourseActivity.this,spinnerList);
                        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        spinner.setPrompt("第一周");
                        int[] b = {1};

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int o, long l) {
                                deleteView();
                                initView();
                                b[0] =Integer.parseInt(spinnerList.get(o).substring(1,2));
                                spinner.setSelection(o,true);
                                for (int i=0;i<list.size();i++){
                                    String week=list.get(i).getWeek().substring(0,1);
                                    int a=Integer.parseInt(week);
                                    int j;
                                    int flag=0;
                                    if (a<= b[0]){
                                        if (list.get(i).getSchedule().equals("1-2")){
                                            j=0;
                                        }else if (list.get(i).getSchedule().equals("3-4")){
                                            j=1;
                                        }else if (list.get(i).getSchedule().equals("5-6")){
                                            j=2;
                                        }else if(list.get(i).getSchedule().equals("7-8")){
                                            j=3;
                                        }else {
                                            j=4;
                                            flag=1;
                                        }
                                        if(b[0]%2==0&&(list.get(i).getWeek().contains("单"))) {
                                            continue;
                                        }
                                        setCourseMessage(list.get(i).getXingqi(), jieci[j], list.get(i).getCname() + "\n" + list.get(i).getWeek() + "(" + list.get(i).getSchedule() + ")" + "\n" +
                                                list.get(i).getTeacher() + "\n" + list.get(i).getAddress(), flag);
                                    }
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });


                        for (int i=0;i<list.size();i++){
                            String week=list.get(i).getWeek().substring(0,1);
                            // String[] week1=week.split("-");
                            int a=Integer.parseInt(week);
                            //int b=Integer.parseInt(week1[1]);
                            int j;
                            int flag=0;

                            if (a<= b[0]){
                                if (list.get(i).getSchedule().equals("1-2")){
                                    j=0;
                                }else if (list.get(i).getSchedule().equals("3-4")){
                                    j=1;
                                }else if (list.get(i).getSchedule().equals("5-6")){
                                    j=2;
                                }else if(list.get(i).getSchedule().equals("7-8")){
                                    j=3;
                                }else {
                                    j=4;
                                    flag=1;
                                }
                                setCourseMessage(list.get(i).getXingqi(),jieci[j],list.get(i).getCname()+"\n"+list.get(i).getWeek()+"("+list.get(i).getSchedule()+")"+"\n"+
                                        list.get(i).getTeacher()+"\n"+list.get(i).getAddress(),flag);
                            }
                        }

                    }
                });
            }
        }).start();


    }

    private void initView() {
        int height = dm.heightPixels;
        int gridHeight = height / 10;
        gridHeight1 = gridHeight;
        for(int i = 1; i <= 11; i ++){
            for(int j = 1; j <= 8; j ++){

                TextView tx = new TextView(CourseActivity.this);
                tx.setId((i - 1) * 8  + j);
                //除了最后一列，都使用course_text_view_bg背景（最后一列没有右边框）
                if(j < 8)
                    tx.setBackgroundDrawable(CourseActivity.this.
                            getResources().getDrawable(R.drawable.course));
                else
                    tx.setBackgroundDrawable(CourseActivity.this.
                            getResources().getDrawable(R.drawable.course));
                //相对布局参数
                RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
                        aveWidth * 33 / 32 + 1,
                        gridHeight);
                //文字对齐方式
                tx.setGravity(Gravity.CENTER);
                //字体样式
                tx.setTextAppearance(this, R.style.badge_style);

                //如果是第一列，需要设置课的序号（1 到 12）
                if(j == 1)
                {
                    tx.setText(String.valueOf(i));

                    rp.width = aveWidth * 3/4;
                    //设置他们的相对位置
                    if(i == 1)
                        rp.addRule(RelativeLayout.BELOW, empty.getId());
                    else
                        rp.addRule(RelativeLayout.BELOW, (i - 1) * 8);
                }
                else
                {
                    rp.addRule(RelativeLayout.RIGHT_OF, (i - 1) * 8  + j - 1);
                    rp.addRule(RelativeLayout.ALIGN_TOP, (i - 1) * 8  + j - 1);
                    tx.setText("");
                }

                tx.setLayoutParams(rp);
                course_table_layout.addView(tx);
            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    public void setCourseMessage(int xingqi,int jieci,String courseMessage,int flag){

        //五种颜色的背景
        int[] background = {R.color.blue, R.color.color_gradient_1,
                R.color.color_gradient_X, R.color.color_gradient_4,
                R.color.color_gradient_7};
        // 添加课程信息
        TextView courseInfo = new TextView(this);
        courseInfo.setText(courseMessage);
        //该textview的高度根据其节数的跨度来设置
        if (flag==1){
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                    aveWidth * 31 / 32,
                    (gridHeight1 - 5) * 3);

            //textview的位置由课程开始节数和上课的时间（day of week）确定
            rlp.topMargin = 5 + jieci * gridHeight1;
            rlp.leftMargin = 1;
            // 偏移由这节课是星期几决定
            rlp.addRule(RelativeLayout.RIGHT_OF, xingqi);
            //字体剧中
            courseInfo.setGravity(Gravity.CENTER);
            // 设置一种背景
            Random random = new Random();
            courseInfo.setBackgroundResource(background[random.nextInt(5)]);
            courseInfo.setTextSize(10);
            courseInfo.setLayoutParams(rlp);
            courseInfo.setTextColor(Color.WHITE);
            //设置不透明度
            courseInfo.getBackground().setAlpha(222);
            course_table_layout.addView(courseInfo);
        }else if (flag==0){
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                    aveWidth * 31 / 32,
                    (gridHeight1 - 5) * 2);

            //textview的位置由课程开始节数和上课的时间（day of week）确定
            rlp.topMargin = 5 + jieci * gridHeight1;
            rlp.leftMargin = 1;
            // 偏移由这节课是星期几决定
            rlp.addRule(RelativeLayout.RIGHT_OF, xingqi);
            //字体剧中
            courseInfo.setGravity(Gravity.CENTER);
            // 设置一种背景
            Random random = new Random();
            courseInfo.setBackgroundResource(background[random.nextInt(5)]);
            courseInfo.setTextSize(10);
            courseInfo.setLayoutParams(rlp);
            courseInfo.setTextColor(Color.WHITE);
            //设置不透明度
            courseInfo.getBackground().setAlpha(222);
            ImageView imageView=new ImageView(this);
            course_table_layout.addView(courseInfo);


        }
    }
public void deleteView(){
        course_table_layout.removeAllViews();
}
}

