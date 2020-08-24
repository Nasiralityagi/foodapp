package com.android.tlb.auth.ui

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.android.tlb.BuildConfig
import com.android.tlb.R
import com.android.tlb.auth.data.listener.AuthListener
import com.android.tlb.auth.data.viewmodel.SignUpViewModel
import com.android.tlb.databinding.ActivitySignupBinding
import com.android.tlb.factory.ViewModelFactory
import com.android.tlb.utils.Constants
import com.android.tlb.utils.goActivity
import com.android.tlb.utils.toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.iceteck.silicompressorr.SiliCompressor
import com.tirade.android.widget.BottomSheetCameraGallery
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger

class SignUpActivity : AppCompatActivity(),
    AuthListener,
    BottomSheetCameraGallery.BottomSheetListener {

    private val TAG = "PublicLogin"
    private lateinit var viewModel: SignUpViewModel
    private lateinit var binding: ActivitySignupBinding
    private val CAMERA_PIC_REQUEST = 111
    private val IMAGE_PICK_CODE = 222
    private val REQUEST__PERMISSION = 100
    private val MEDIA_TYPE_IMAGE = 333
    private lateinit var fileUri: Uri
    private lateinit var mImageFileLocation: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        viewModel = ViewModelProvider(this@SignUpActivity, ViewModelFactory()).get(SignUpViewModel::class.java)
        binding.model = viewModel
        registerObservables()

        //fname.leftDrawable(R.drawable.ic_username, R.dimen.drawable_icon_size)
        //mobile.leftDrawable(R.drawable.ic_call, R.dimen.drawable_icon_size)
        //email.leftDrawable(R.drawable.ic_mail, R.dimen.drawable_icon_size)
        //password.leftDrawable(R.drawable.ic_pass, R.dimen.drawable_icon_size)
        //zip_code.leftDrawable(R.drawable.ic_pass, R.dimen.drawable_icon_size)
        //age.leftDrawable(R.drawable.ic_mail, R.dimen.drawable_icon_size)

    }

    /**With this No observer needed in activity*/
    private fun registerObservables() {
        viewModel.getNavigationEvent().setEventReceiver(this, this)
    }

    override fun pgVisibility(visibility: Int) {
        binding.progress.visibility = visibility
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onSuccess(message: String) {
        this.toast(message)
        this.goActivity(SignUpActivity::class.java)
        finish()
    }

    override fun onFailure(message: String) {
        this.toast(message)
    }

    override fun onImageSelection() {
        Log.i(TAG, "Camera Clicked")
        val permissionCam = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        )
        val permissionStorage = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (permissionCam == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to storage accepted")
                val bottomSheet = BottomSheetCameraGallery()
                bottomSheet.show(supportFragmentManager, "BottomSheetCameraGallery")
        } else if (permissionCam == PackageManager.PERMISSION_DENIED || permissionStorage == PackageManager.PERMISSION_DENIED) {
            Log.i(TAG, "Permission to storage always denied")
            val message: String = "We noticed you have disabled permission.\n " +
                    "Please enable  CAMERA and STORAGE permission from,\n" +
                    "Tirade Application settings\n"
            this.toast(message, true)
            setupPermissions()
        } else {
            setupPermissions()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST__PERMISSION -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user")
                    binding.camera.isEnabled = false
                } else {
                    Log.i(TAG, "Permission has been granted by user")
                    binding.camera.isEnabled = true
                }
            }
        }
    }

    private fun setupPermissions() {
        if (ContextCompat.checkSelfPermission(this@SignUpActivity, Manifest.permission.CAMERA) !== PackageManager.PERMISSION_GRANTED) {
            binding.camera.isEnabled = false
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST__PERMISSION
            )
        }
    }


    override fun openDatePicker() {
        showDialog()
    }

    private fun showDialog() {
        var date = ""
        val dialog = Dialog(this@SignUpActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.date_picker)
        val datePicker = dialog.findViewById<DatePicker>(R.id.date_Picker)
        val today = Calendar.getInstance()
        datePicker.maxDate = System.currentTimeMillis()
        datePicker.init(
            today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->
            val month = month + 1
            date = "$month/$day/$year"

        }

        val yesBtn = dialog.findViewById(R.id.select) as Button
        val noBtn = dialog.findViewById(R.id.cancel) as Button
        yesBtn.setOnClickListener {
            binding.age.setText(date)
            dialog.dismiss()
        }
        noBtn.setOnClickListener {
            binding.age.setText("Birth date")
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onBackPressed() {
        this.goActivity(LoginActivity::class.java)
        super.onBackPressed()
    }

    override fun onOptionClick(text: String) {
        when (text) {
            "CAMERA" -> {
                if (Build.VERSION.SDK_INT > 21) {
                    //use this if Lollipop_Mr1 (API 22) or above
                    val intent = Intent()
                    intent.action = MediaStore.ACTION_IMAGE_CAPTURE

                    // We give some instruction to the intent to save the image
                    var photoFile: File? = null
                    try {
                        // If the createImageFile will be successful, the photo file will have the address of the file
                        photoFile = createImageFile()
                        // Here we call the function that will try to catch the exception made by the throw function
                    } catch (e: IOException) {
                        Logger.getAnonymousLogger()
                            .info("Exception error in generating the file")
                        e.printStackTrace()
                    }
                    // Here we add an extra file to the intent to put the address on to. For this purpose we use the FileProvider, declared in the AndroidManifest.
                    val outputUri: Uri = FileProvider.getUriForFile(
                        this, BuildConfig.APPLICATION_ID.toString() + ".provider",
                        photoFile!!
                    )
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)

                    // The following is a new line with a trying attempt
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    Logger.getAnonymousLogger().info("Calling the camera App by intent")

                    // The following strings calls the camera app and wait for his file in return.
                    startActivityForResult(intent, CAMERA_PIC_REQUEST)
                } else {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)

                    // start the image capture Intent
                    startActivityForResult(intent, CAMERA_PIC_REQUEST)
                }
            }
            "GALLERY" -> {
                pickImageFromGallery()
            }
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        //intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select a photo"), IMAGE_PICK_CODE)
    }

    /**
     * Creating file uri to store image/video
     */
    private fun getOutputMediaFileUri(type: Int): Uri {
        return Uri.fromFile(
            getOutputMediaFile(
                type
            )
        )
    }

    /**
     * returning image / video
     */
    private fun getOutputMediaFile(type: Int): File? {
        // External sdcard location
        val mediaStorageDir = File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), Constants.IMAGE_DIRECTORY)
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create ${Constants.IMAGE_DIRECTORY} directory")
                return null
            }
        }

        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val mediaFile: File
        mediaFile = if (type == MEDIA_TYPE_IMAGE) {
            File(mediaStorageDir.path + File.separator + "IMG_" + ".jpg")
        } else {
            return null
        }
        return mediaFile
    }

    @Throws(IOException::class)
    fun createImageFile(): File? {
        Logger.getAnonymousLogger().info("Generating the image - method started")

        // Here we create a "non-collision file name", alternatively said, "an unique filename" using the "timeStamp" functionality
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmSS").format(Date())
        val imageFileName = "IMAGE_$timeStamp"
        // Here we specify the environment location and the exact path where we want to save the so-created file
        val storageDirectory = File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), Constants.IMAGE_DIRECTORY)
        Logger.getAnonymousLogger().info("Storage directory set")

        // Then we create the storage directory if does not exists
        if (!storageDirectory.exists()) storageDirectory.mkdir()

        // Here we create the file using a prefix, a suffix and a directory
        val image = File(storageDirectory, "$imageFileName.jpg")
        // File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);

        // Here the location is saved into the string mImageFileLocation
        Logger.getAnonymousLogger().info("File name and path set")
        mImageFileLocation = image.absolutePath
        // fileUri = Uri.parse(mImageFileLocation);
        // The file is returned to the previous intent across the camera application
        return image
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == IMAGE_PICK_CODE) {
                if (data != null) {
                    val image = data.data
                    val projection = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = contentResolver.query(image!!, projection, null, null, null)
                    assert(cursor != null)
                    cursor!!.moveToFirst()

                    val columnIndex = cursor.getColumnIndex(projection[0])
                    val mediaPath = cursor.getString(columnIndex)
                    cursor.close()

                    val fileDirSave = File(
                        this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                            .toString() + "/TIRADE_IMAGES"
                    )
                    val filePath = SiliCompressor.with(this).compress(mediaPath, fileDirSave)
                    setUpImojiIcon(filePath)
                    viewModel.profileImg.set(filePath)
                }
            } else if (requestCode == CAMERA_PIC_REQUEST) {
                if (Build.VERSION.SDK_INT > 21) {
                    setUpImojiIcon(mImageFileLocation)
                    val fileDirSave = File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() +  "/TIRADE_IMAGES")
                    val filePath = SiliCompressor.with(this).compress(mImageFileLocation, fileDirSave)
                    viewModel.profileImg.set(filePath)
//                        viewModel.profileImg.set(mImageFileLocation)
                } else {
                    setUpImojiIcon(fileUri)
//                        viewModel.profileImg.set(fileUri.path)
                    val fileDirSave = File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() +  "/TIRADE_IMAGES")
                    val filePath = SiliCompressor.with(this).compress(fileUri.path, fileDirSave)
                    viewModel.profileImg.set(filePath)
                }
            }
        }
    }

    private fun setUpImojiIcon(imoji: Any) {
        Glide.with(this)
            .asBitmap().load(imoji)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    bitmap: Bitmap?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    updateBitmapImage(bitmap)
                    return false
                }
            }
            ).submit()
    }

    fun updateBitmapImage(bitmap: Bitmap?) {
        this@SignUpActivity.runOnUiThread(java.lang.Runnable {
            binding.profile.setImageBitmap(bitmap)
        })
    }
}
