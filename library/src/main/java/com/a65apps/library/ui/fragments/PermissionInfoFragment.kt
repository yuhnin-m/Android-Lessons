package com.a65apps.library.ui.fragments

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.a65apps.library.Constants
import com.a65apps.library.R
import kotlinx.android.synthetic.main.fragment_request_permisson.*

class PermissionInfoFragment : Fragment(R.layout.fragment_request_permisson) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnRequestPermissions.setOnClickListener(View.OnClickListener {
            ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.READ_CONTACTS,
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION),
                    Constants.CODE_PERMISSION_REQUEST_CODE)
        })
    }
}