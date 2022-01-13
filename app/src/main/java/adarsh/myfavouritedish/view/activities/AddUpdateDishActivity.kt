package adarsh.myfavouritedish.view.activities

import adarsh.myfavouritedish.R
import adarsh.myfavouritedish.application.FavDishApplication
import adarsh.myfavouritedish.databinding.ActivityAddUpdateDishBinding
import adarsh.myfavouritedish.databinding.DialogCustomImageSelectionBinding
import adarsh.myfavouritedish.databinding.DialogCustomListBinding
import adarsh.myfavouritedish.model.entities.FavDish
import adarsh.myfavouritedish.utils.Constants
import adarsh.myfavouritedish.view.adapters.CustomListItemAdapter
import adarsh.myfavouritedish.viewmodel.FavDishViewModel
import adarsh.myfavouritedish.viewmodel.FavDishViewModelFactory
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


class AddUpdateDishActivity : AppCompatActivity() ,View.OnClickListener {

    private lateinit var mBinding: ActivityAddUpdateDishBinding
    private var mImagePath:String = ""
    // A global variable for the custom list dialog.
    private lateinit var mCustomListDialog: Dialog

    private val mFavDishViewModel : FavDishViewModel by viewModels{
       FavDishViewModelFactory((application as FavDishApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupActionBar()

        mBinding.ivAddDishImage.setOnClickListener(this@AddUpdateDishActivity)
        mBinding.etCategory.setOnClickListener(this)
        mBinding.etType.setOnClickListener(this)
        mBinding.etCookingTime.setOnClickListener(this)
        mBinding.btnAddDish.setOnClickListener(this)
    }

    private fun setupActionBar(){
        setSupportActionBar(mBinding.toolbarAddDishActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mBinding.toolbarAddDishActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                 R.id.iv_add_dish_image ->{
                      customImageSelectionDialog()
                     return
                 }
                R.id.et_type ->{
                         customItemsDialog(resources.getString(R.string.title_select_dish_type),
                         Constants.dishTypes(),
                         Constants.DISH_TYPE)
                    return
                }
                R.id.et_category ->{
                    customItemsDialog(resources.getString(R.string.title_select_dish_category),
                        Constants.dishCategories(),
                        Constants.DISH_CATEGORY)
                    return
                }
                R.id.et_cooking_time ->{
                    customItemsDialog(resources.getString(R.string.title_select_dish_cooking_time),
                        Constants.dishCookTime(),
                        Constants.DISH_COOKING_TIME)
                    return
                }

                R.id.btn_add_dish ->{
                    // Define the local variables and get the EditText values.
                    // For Dish Image we have the global variable defined already.

                    val title = mBinding.etTitle.text.toString().trim { it <= ' ' }
                    val type = mBinding.etType.text.toString().trim { it <= ' ' }
                    val category = mBinding.etCategory.text.toString().trim { it <= ' ' }
                    val ingredients = mBinding.etIngredients.text.toString().trim { it <= ' ' }
                    val cookingTimeInMinutes = mBinding.etCookingTime.text.toString().trim { it <= ' ' }
                    val cookingDirection = mBinding.etDirectionToCook.text.toString().trim { it <= ' ' }

                    when {

                        TextUtils.isEmpty(mImagePath) -> {
                            Toast.makeText(
                                this@AddUpdateDishActivity,
                                resources.getString(R.string.err_msg_select_dish_image),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        TextUtils.isEmpty(title) -> {
                            Toast.makeText(
                                this@AddUpdateDishActivity,
                                resources.getString(R.string.err_msg_enter_dish_title),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        TextUtils.isEmpty(type) -> {
                            Toast.makeText(
                                this@AddUpdateDishActivity,
                                resources.getString(R.string.err_msg_select_dish_type),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        TextUtils.isEmpty(category) -> {
                            Toast.makeText(
                                this@AddUpdateDishActivity,
                                resources.getString(R.string.err_msg_select_dish_category),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(ingredients) -> {
                            Toast.makeText(
                                this@AddUpdateDishActivity,
                                resources.getString(R.string.err_msg_enter_dish_ingredients),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(cookingTimeInMinutes) -> {
                            Toast.makeText(
                                this@AddUpdateDishActivity,
                                resources.getString(R.string.err_msg_select_dish_cooking_time),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(cookingDirection) -> {
                            Toast.makeText(
                                this@AddUpdateDishActivity,
                                resources.getString(R.string.err_msg_enter_dish_cooking_instructions),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {

                           val favDishDetails : FavDish = FavDish(
                               mImagePath,
                               Constants.DISH_IMAGE_SOURCE_LOCAL,
                               title,
                               type,
                               category,
                               ingredients,
                               cookingTimeInMinutes,
                               cookingDirection,
                               false
                           )

                            mFavDishViewModel.insert(favDishDetails)

                            Toast.makeText(
                                this@AddUpdateDishActivity,
                                "You successfully added your favorite dish details.",
                                Toast.LENGTH_SHORT
                            ).show()

                            // You even print the log if Toast is not displayed on emulator
                            Log.e("Insertion", "Success")
                            // Finish the Activity
                            finish()
                        }
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val galleryIntent = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                resultLauncherGallery.launch(galleryIntent)
            } else {
                 showRationalDialogForPermissions()
//                 Toast.makeText(this@AddUpdateDishActivity,
//                    resources.getString(R.string.read_storage_permission_dened), Toast.LENGTH_LONG)
//                    .show()
            }
        }

        if(requestCode == READ_CAMERA_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                resultLauncherCamera.launch(intent)
            } else {
                showRationalDialogForPermissions()
            }
        }

    }

    private fun customImageSelectionDialog(){
        val dialog = Dialog(this@AddUpdateDishActivity)
        val binding : DialogCustomImageSelectionBinding =
            DialogCustomImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.tvCamera.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this@AddUpdateDishActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this@AddUpdateDishActivity, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                resultLauncherCamera.launch(intent)
            } else {
                // Requests permission
                ActivityCompat.requestPermissions(this@AddUpdateDishActivity,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA),
                   READ_CAMERA_PERMISSION_CODE)
            }
             dialog.dismiss()
        }
       binding.tvGallery.setOnClickListener {


           if (ContextCompat.checkSelfPermission(this@AddUpdateDishActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
               PackageManager.PERMISSION_GRANTED) {
               //  showErrorSnackBar("You already have the storage permission.", false)
               val galleryIntent = Intent(Intent.ACTION_PICK,
                   MediaStore.Images.Media.EXTERNAL_CONTENT_URI
               )
               resultLauncherGallery.launch(galleryIntent)
           } else {
               // Requests permission
               ActivityCompat.requestPermissions(this@AddUpdateDishActivity,
                   arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                   READ_STORAGE_PERMISSION_CODE)
           }
            dialog.dismiss()
        }
        dialog.show()
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == CAMERA){
//            data?.extras?.let {
//                val thumbnail : Bitmap = data.extras!!.get("data") as Bitmap
//                mBinding.ivDishImage.setImageBitmap(thumbnail)
//            }
//
//        }
//    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    private var resultLauncherGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            data?.let {
                val selectedPhotoUri = data.data
                Glide.with(this)
                    .load(selectedPhotoUri)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(object :RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.e("TAG","Error loading image",e)
                           return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                           resource?.let {
                               val bitmap:Bitmap = resource?.toBitmap()
                               mImagePath = saveImageToInternalStorage(bitmap)
                           }
                        return false
                        }

                    })
                    .into(mBinding.ivDishImage)

                //   mBinding.ivDishImage.setImageURI(selectedPhotoUri)
                mBinding.ivAddDishImage.setImageDrawable(ContextCompat.getDrawable(this@AddUpdateDishActivity , R.drawable.ic_vector_edit))
            }
        }
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    private var resultLauncherCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            data?.extras?.let {
                val thumbnail : Bitmap = data.extras!!.get("data") as Bitmap

                Glide.with(this)
                    .load(thumbnail)
                    .centerCrop()
                    .into(mBinding.ivDishImage)

               mImagePath = saveImageToInternalStorage(thumbnail)
             //   mBinding.ivDishImage.setImageBitmap(thumbnail)
                mBinding.ivAddDishImage.setImageDrawable(ContextCompat.getDrawable(this@AddUpdateDishActivity , R.drawable.ic_vector_edit))
            }
        }
    }

    private fun showRationalDialogForPermissions(){
        AlertDialog.Builder(this).setMessage("It Looks like you have turned off permissions " +
                "required for this feature . It can be enabled under Application Settings")
            .setPositiveButton("GO TO SETTINGS")
            {_,_ ->
                    try {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package",packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }catch (e:ActivityNotFoundException){
                        e.printStackTrace()
                    }
            }
            .setNegativeButton("Cancel"){
                dialog,_ ->
                dialog.dismiss()
            }.show()
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap):String{
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY , Context.MODE_PRIVATE)
        file = File(file , "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG , 100,stream)
            stream.flush()
            stream.close()
        }catch (e:IOException){
            e.printStackTrace()
        }
   return file.absolutePath }

    private fun customItemsDialog(title:String , itemsList:List<String>,selection:String){
        // TODO Step 2: Replace the dialog variable with the global variable.
        mCustomListDialog = Dialog(this@AddUpdateDishActivity)

        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)

        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        mCustomListDialog.setContentView(binding.root)

        binding.tvTitle.text = title

        // Set the LayoutManager that this RecyclerView will use.
        binding.rvList.layoutManager = LinearLayoutManager(this@AddUpdateDishActivity)
        // Adapter class is initialized and list is passed in the param.
        val adapter = CustomListItemAdapter(this@AddUpdateDishActivity, itemsList, selection)
        // adapter instance is set to the recyclerview to inflate the items.
        binding.rvList.adapter = adapter
        //Start the dialog and display it on screen.
        mCustomListDialog.show()
    }
    fun selectedListItem(item: String, selection: String) {
         when(selection){
             Constants.DISH_TYPE ->{
                 mCustomListDialog.dismiss()
                 mBinding.etType.setText(item)
             }
             Constants.DISH_CATEGORY->{
                 mCustomListDialog.dismiss()
                 mBinding.etCategory.setText(item)
             }
              else ->{
                 mCustomListDialog.dismiss()
                 mBinding.etCookingTime.setText(item)
             }
         }
    }

    companion object{
        const val READ_CAMERA_PERMISSION_CODE = 32
        const val READ_STORAGE_PERMISSION_CODE = 12

        private const val IMAGE_DIRECTORY = "FavDishImages"
    }

}