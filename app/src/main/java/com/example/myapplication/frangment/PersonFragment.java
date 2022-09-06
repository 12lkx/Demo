package com.example.myapplication.frangment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.myapplication.CourseActivity;
import com.example.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView tv2,tv3;
    private Button btn_praise,btn_invitation,btn_mulfunction,btn_course,btn_set;
    public PersonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonFragment newInstance(String param1, String param2) {
        PersonFragment fragment = new PersonFragment();
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
        View view= inflater.inflate(R.layout.fragment_person, container, false);
        tv2=view.findViewById(R.id.tv2);
        tv3=view.findViewById(R.id.tv3);
        btn_course=view.findViewById(R.id.btn_scourse);
        btn_praise=view.findViewById(R.id.btn_praise);
        btn_invitation=view.findViewById(R.id.btn_invitation);
        btn_set=view.findViewById(R.id.btn_set);
        btn_mulfunction=view.findViewById(R.id.btn_mulfunction);
        btn_course.setOnClickListener(this);
        btn_praise.setOnClickListener(this);
        btn_invitation.setOnClickListener(this);
        btn_set.setOnClickListener(this);
        btn_mulfunction.setOnClickListener(this);
        initView();
        return view;
    }

    private void initView() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_scourse:
                startActivity(new Intent(getActivity(), CourseActivity.class));
                break;
        }

    }
}