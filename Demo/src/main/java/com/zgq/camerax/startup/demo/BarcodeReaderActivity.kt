package com.zgq.camerax.startup.demo

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zgq.camerax.startup.camerax.AnalysisType
import kotlinx.android.synthetic.main.activity_barcode_reader.*

class BarcodeReaderActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_reader)
        with(barcode_reader){
            if (requestPermissionGranted()){
                this.startCamera(this@BarcodeReaderActivity, AnalysisType.BARCODE(false, object: AnalysisType.BarcodeResultCallback{
                    override fun onFailed(e: Exception) {
                        Log.e("Result", "${e.message}")
                    }

                    override fun onSuccess(result: List<String>) {
                        Log.e("Result", "$result")
                    }
                }))
            }else{
                ActivityCompat.requestPermissions(this@BarcodeReaderActivity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            }
        }

        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if (requestPermissionGranted()) {
                barcode_reader.startCamera(this, AnalysisType.BARCODE(false, object: AnalysisType.BarcodeResultCallback{
                    override fun onSuccess(result: List<String>) {
                        TODO("Not yet implemented")
                    }

                    override fun onFailed(e: Exception) {
                        TODO("Not yet implemented")
                    }
                }))
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }
    private fun requestPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }
    companion object{
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}