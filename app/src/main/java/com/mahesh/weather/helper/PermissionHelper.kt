package com.mahesh.weather.helper

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.mahesh.weather.app.TAG
import com.mahesh.weather.util.REQUEST_PERMISSION_CODE
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class PermissionHelper @Inject constructor(private val activityView: AppCompatActivity) {

    private val deniedPermissions: MutableList<String> = mutableListOf()
    private val granted: MutableList<String> = mutableListOf()
    private var permissionListener: PermissionsListener? = null

    fun setPermissionListener(permissionListener: PermissionsListener) {
        this.permissionListener = permissionListener
    }

    interface PermissionsListener {
        fun onPermissionGranted()
        fun onPermissionRejectedManyTimes(@NonNull rejectedPerms: List<String>)
    }

    fun isPermissionGranted(permissionList: MutableList<String>): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionList.forEach {
                if (activityView.checkSelfPermission(it) == PackageManager.PERMISSION_DENIED)
                    return false
            }
        }
        return true
    }

    /**
     * Request permissions.
     *
     * @param permissions - List of String of permissions to request, for eg: List<String>{PermissionManager.CAMERA} or multiple List<String>{PermissionManger.CAMERA, PermissionManager.CONTACTS}
     */
    fun requestPermission(@NonNull permissions: List<String>) {
        deniedPermissions.clear()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var allPermissionGranted = true

            for (permission in permissions) {
                try {
                    if (activityView.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
                        allPermissionGranted = false
                        deniedPermissions.add(permission)
                        Log.d(TAG, "denied $permission")
                    }
                } catch (ignored: Exception) {
                }
            }

            if (!allPermissionGranted) {
                activityView.requestPermissions(deniedPermissions.toTypedArray(), REQUEST_PERMISSION_CODE)
            } else {
                permissionListener?.onPermissionGranted()
            }
        } else {
            permissionListener?.onPermissionGranted()
        }
    }

    /***
     * After the permissions are requested, pass the results from Activity/fragments onRequestPermissionsResult to this function for processing
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<out String>, @NonNull grantResults: IntArray) {
        val permissionName = StringBuilder()
        var requestPermissionRationale = false
        granted.clear()

        for (permission in deniedPermissions) {
            if (activityView.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                granted.add(permission)
            } else {
                if (activityView.shouldShowRequestPermissionRationale(permission)) {
                    requestPermissionRationale = true
                }
                permissionName.append(", ")
                permissionName.append(PermissionHelper.getNameFromPermission(permission))
            }
        }
        var res = permissionName.toString()
        deniedPermissions.removeAll(granted)
        if (deniedPermissions.size > 0) {
            res = res.substring(1)
            if (requestPermissionRationale) {
                getRequestAgainAlertDialog(activityView, res)
            } else {//Never ask again
                goToSettingsAlertDialog(activityView, res)
            }
        } else {
            permissionListener?.onPermissionGranted()
        }
    }

    private fun goToSettingsAlertDialog(view: Activity?, permissionName: String): AlertDialog {
        return AlertDialog.Builder(view).setTitle("Permission Required")
            .setMessage("We need $permissionName permissions")
            .setPositiveButton("GO TO SETTINGS") { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                intent.data = Uri.parse("package:" + view?.packageName)
                view?.startActivity(intent)
            }
            .setNegativeButton("NO") { _, _ ->
                permissionListener?.onPermissionRejectedManyTimes(
                    ArrayList<String>(
                        deniedPermissions
                    )
                )
            }
            .show()
    }

    private fun getRequestAgainAlertDialog(view: Activity?, permissionName: String): AlertDialog {
        val permissionList = ArrayList<String>(deniedPermissions)
        return AlertDialog.Builder(view).setTitle("Permission Required")
            .setMessage("We need $permissionName permissions")
            .setPositiveButton("OK") { _, _ -> requestPermission(permissionList) }
            .setNegativeButton("NO") { _, _ -> permissionListener?.onPermissionRejectedManyTimes(permissionList) }
            .show()
    }

    companion object {

        @JvmStatic
        private var labelsMap: MutableMap<String, String>? = null

        @JvmStatic
        fun getNameFromPermission(permission: String): String {
            if (labelsMap == null) {
                labelsMap = HashMap()
                labelsMap!![Manifest.permission.READ_EXTERNAL_STORAGE] = "Read Storage"
                labelsMap!![Manifest.permission.WRITE_EXTERNAL_STORAGE] = "Write Storage"
                labelsMap!![Manifest.permission.CAMERA] = "Camera"
                labelsMap!![Manifest.permission.CALL_PHONE] = "Call"
                labelsMap!![Manifest.permission.READ_SMS] = "SMS"
                labelsMap!![Manifest.permission.RECEIVE_SMS] = "Receive SMS"
                labelsMap!![Manifest.permission.ACCESS_FINE_LOCATION] = "Exact Location"
                labelsMap!![Manifest.permission.ACCESS_COARSE_LOCATION] = "Close Location"
            }
            val value = labelsMap!![permission]
            return if (value == null) {
                val split = permission.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                split[split.size - 1]
            } else {
                value
            }
        }
    }

}