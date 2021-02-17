package com.jedischool.skinder.ui.activities

import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jedischool.skinder.R
import com.jedischool.skinder.data.api.ApiHelper
import com.jedischool.skinder.data.api.RetrofitBuilder
import com.jedischool.skinder.data.model.UserLeaderboard
import com.jedischool.skinder.ui.base.ViewModelFactory
import com.jedischool.skinder.ui.viewmodel.MainViewModel
import com.jedischool.skinder.utils.Status
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AddPostActivity : AppCompatActivity() {

    private val FINAL_TAKE_PHOTO = 1
    private val FINAL_CHOOSE_PHOTO = 2
    private var picture: ImageView? = null
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addpost)

        val viewModel = ViewModelProvider(
                this,
                ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)

        picture = findViewById(R.id.image_add_post)
        val postTitle: EditText = findViewById(R.id.title_add_post)
        val postDesc: EditText = findViewById(R.id.desc_add_post)
        val uploadImage: Button = findViewById(R.id.upload_add_post)
        val cameraButton: Button = findViewById(R.id.camera_add_post)
        val addButton: Button = findViewById(R.id.add_post_button)

        cameraButton.setOnClickListener{
            val outputImage = File(externalCacheDir, "output_image.jpg")
            if(outputImage.exists()) {
                outputImage.delete()
            }
            outputImage.createNewFile()
            imageUri = if(Build.VERSION.SDK_INT >= 24){
                FileProvider.getUriForFile(this, "com.jedischool.skinder.fileprovider", outputImage)
            } else {
                Uri.fromFile(outputImage)
            }

            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, FINAL_TAKE_PHOTO)
        }

        uploadImage.setOnClickListener {
            val checkSelfPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                }
            else{
                openAlbum()
            }
        }

        addButton.setOnClickListener {
            val outputImage = File(externalCacheDir, "output_image.jpg")
            if(postTitle.text.toString()=="" || postDesc.text.toString()==""){
                Toast.makeText(this, "Please enter all fields!", Toast.LENGTH_LONG).show()
            }
            else if(outputImage.exists()) {
                val img = MultipartBody.Part.createFormData("myFile",outputImage.name,
                        outputImage.asRequestBody("image/*".toMediaTypeOrNull()))
                val title = MultipartBody.Part.createFormData("Title",postTitle.text.toString())
                val caption = MultipartBody.Part.createFormData("Caption",postDesc.text.toString())
                viewModel.addPostImage(img,title,caption).observe(this, Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                Toast.makeText(this, resource.data?.Message, Toast.LENGTH_SHORT).show()
                            }
                            Status.ERROR -> {
                                Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                                Log.e("ERR", resource.message.toString())
                                if(resource.message.toString().contains("401",ignoreCase = true)) {

                                }
                            }
                            Status.LOADING -> {
                            }
                        }
                    }
                })
            }

            else {
                val title = MultipartBody.Part.createFormData("Title",postTitle.text.toString())
                val caption = MultipartBody.Part.createFormData("Caption",postDesc.text.toString())
                viewModel.addPost(title,caption).observe(this, Observer {
                    it?.let { resource ->
                        when (resource.status) {
                                Status.SUCCESS -> {
                                    Toast.makeText(this, resource.data?.Message, Toast.LENGTH_SHORT).show()
                                }
                                Status.ERROR -> {
                                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                                    Log.e("ERR", resource.message.toString())
                                    if(resource.message.toString().contains("401",ignoreCase = true)) {

                                    }
                            }
                            Status.LOADING -> {
                            }
                        }
                    }
                })
            }
        }
    }

    private fun openAlbum(){
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        startActivityForResult(intent, FINAL_CHOOSE_PHOTO)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1 ->
            if (grantResults.isNotEmpty() && grantResults.get(0) ==PackageManager.PERMISSION_GRANTED){
                openAlbum()
            }
            else {
                Toast.makeText(this, "You deied the permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            FINAL_TAKE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri!!))
                    picture!!.setImageBitmap(bitmap)
                }
            FINAL_CHOOSE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    handleImageOnKitkat(data)
                }
        }
    }

    private fun handleImageOnKitkat(data: Intent?) {
        var imagePath: String? = null
        val uri = data!!.data
        if (DocumentsContract.isDocumentUri(this, uri)){
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri?.authority){
                val id = docId.split(":")[1]
                val selsetion = MediaStore.Images.Media._ID + "=" + id
                imagePath = imagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selsetion)
            }
            else if ("com.android.providers.downloads.documents" == uri?.authority){
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(docId))
                imagePath = imagePath(contentUri, null)
            }
        }
        else if ("content".equals(uri?.scheme, ignoreCase = true)){
            imagePath = imagePath(uri, null)
        }
        else if ("file".equals(uri?.scheme, ignoreCase = true)){
            imagePath = uri?.path
        }
        displayImage(imagePath)
    }

    private fun imagePath(uri: Uri?, selection: String?): String {
        var path: String? = null
        val cursor = contentResolver.query(uri!!, null, selection, null, null )
        if (cursor != null){
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path!!
    }

    private fun displayImage(imagePath: String?){
        if (imagePath != null) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            picture?.setImageBitmap(bitmap)
        }
        else {
            Toast.makeText(this, "Failed to get image", Toast.LENGTH_SHORT).show()
        }
    }
}