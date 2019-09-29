package com.ryzhikov.lattandroid

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onSelectPhotoClick(view: View) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_STORAGE_PERMISSION_REQUEST)
        } else {
            openGalleryForResult()
        }

    }

    private fun openGalleryForResult() {

        val galleryIntent =
            Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*")
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(Intent.createChooser(galleryIntent, "Select photos !"), GALLERY_REQUEST_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {

            if (data.clipData != null) {
                val resultSize = data.clipData!!.itemCount
                val result = arrayOfNulls<Uri>(resultSize)
                for (index in 0 until resultSize) {
                    result[index] = data.clipData!!.getItemAt(index).uri
                }
                showPhotosQuantity(result.size)

            } else if (data.data != null) {
                val photos = data.data
                showPhotosQuantity(1)
            }

        }

    }

    private fun showPhotosQuantity(quantity: Int) {
        quantityText.text = getString(R.string.u_select_photos, quantity)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_REQUEST && grantResults.all { item -> item == PackageManager.PERMISSION_GRANTED }) {
            openGalleryForResult()
        }
    }

    companion object {

        const val READ_STORAGE_PERMISSION_REQUEST = 1000
        const val GALLERY_REQUEST_CODE = 2000
    }
}
