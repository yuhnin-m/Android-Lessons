package com.a65apps.yuhnin.lesson1.ui.fragments;

import android.Manifest;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.a65apps.yuhnin.lesson1.Constants;
import com.a65apps.yuhnin.lesson1.R;

public class RequestPermissonFragment extends Fragment {
    Button btnRequestPermission;

    public RequestPermissonFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_permisson, container, false);
        btnRequestPermission = view.findViewById(R.id.btn_request_permissions);
        if (btnRequestPermission != null) {
            btnRequestPermission.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{Manifest.permission.READ_CONTACTS},
                            Constants.CODE_PERMISSION_READ_CONTACTS);
                }
            });
        }
        return view;
    }
    @Override
    public void onDestroyView() {
        btnRequestPermission = null;
        super.onDestroyView();
    }

}