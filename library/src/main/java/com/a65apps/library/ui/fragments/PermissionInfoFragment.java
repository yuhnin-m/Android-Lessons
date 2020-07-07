package com.a65apps.library.ui.fragments;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.a65apps.library.Constants;
import com.a65apps.library.R;

public class PermissionInfoFragment extends Fragment {
    Button btnRequestPermission;

    public PermissionInfoFragment() {
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