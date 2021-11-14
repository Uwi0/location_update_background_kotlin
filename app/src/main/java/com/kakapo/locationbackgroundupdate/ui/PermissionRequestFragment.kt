package com.kakapo.locationbackgroundupdate.ui

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.kakapo.locationbackgroundupdate.R
import com.kakapo.locationbackgroundupdate.databinding.FragmentPermissionRequestBinding


class PermissionRequestFragment : Fragment() {

    private var _binding: FragmentPermissionRequestBinding? = null
    private val binding get() = _binding!!
    private var permissionRequestType: PermissionRequestType? = null
    private var activityListener: CallBacks? = null

    private val fineLocationRationalSnackbar by lazy{
        Snackbar.make(
            binding.frameLayout,
            R.string.fine_location_permission_rationale,
            Snackbar.LENGTH_LONG
        ).setAction(R.string.ok){
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FINE_LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private val backgroundRationalSnackbar by lazy {
        Snackbar.make(
            binding.frameLayout,
            R.string.background_location_permission_rationale,
            Snackbar.LENGTH_LONG
        ).setAction(R.string.ok){
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                REQUEST_BACKGROUND_LOCATION_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is CallBacks){
            activityListener = context
        }else{
            throw RuntimeException("$context must implement PermissionRequestFragment.Callbacks")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionRequestType =
            arguments?.getSerializable(ARG_PERMISSION_REQUEST_TYPE) as PermissionRequestType
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPermissionRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    interface CallBacks{
        fun displayLocationUi()
    }

    companion object{
        const val TAG = "PermissionRequestFrag"
        private const val ARG_PERMISSION_REQUEST_TYPE =
            "com.kakapo.locationbackgroundupdate.PERMISSION_REQUEST_TYPE"
        private const val REQUEST_FINE_LOCATION_PERMISSION_REQUEST_CODE = 34
        private const val REQUEST_BACKGROUND_LOCATION_PERMISSIONS_REQUEST_CODE = 56

        @JvmStatic
        fun newInstance(permissionRequestType: PermissionRequestType) =
            PermissionRequestFragment.apply {

            }
    }
}

enum class PermissionRequestType{
    FINE_LOCATION,
    BACK_GROUND_LOCATION
}