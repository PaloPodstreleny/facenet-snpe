package com.palopodstreleny.facenet.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.palopodstreleny.facenet.EmbeddedUser
import com.palopodstreleny.facenet.R
import com.palopodstreleny.facenet.databinding.ActivityFacenetRecorderBinding
import com.palopodstreleny.facenet.db.UserEmbedding
import com.palopodstreleny.facenet.db.entity.User
import com.palopodstreleny.facenet.dialog.AddPersonClickListener
import com.palopodstreleny.facenet.dialog.AddPersonDialog
import com.palopodstreleny.facenet.dialog.DeleteAllEmbeddingsListener
import com.palopodstreleny.facenet.dialog.DeleteEmbeddingsDialog
import com.palopodstreleny.facenet.ui.adapter.UserEmbeddingsAdapter
import com.palopodstreleny.facenet.viewmodels.FaceNetEmbeddingViewModel
import com.qualcomm.qti.snpe.NeuralNetwork
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import org.koin.android.viewmodel.ext.android.viewModel


class FaceNetRecordingEmbeddingsActivity : BaseActivity<ActivityFacenetRecorderBinding>() {


    //Create variables for option menu inflation
    private var isDeleteItemMenuCreated = true
    private val deleteItemMenuID = 2123

    private val viewModel: FaceNetEmbeddingViewModel by viewModel()

    //Values for selected user
    private var selectedUserName: String? = null
    private val listOfEmbeddedUsers: ArrayList<User> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this


        //AddEmbedding button, if there is no selected user show Notification about selecting user otherwise pick image
        binding.buttonEmbeddingAdd.setOnClickListener {
            if (selectedUserName != null) {
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this)
            } else {
                showSnackBar(getString(R.string.user_not_selected))
            }
        }

        //When user click add button show dialog
        binding.buttonAddUser.setOnClickListener {
            createDialog()
        }

        //Create Adapter for spinner
        val adapter: ArrayAdapter<User> = ArrayAdapter(this, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            //If nothing selected set selected user to null otherwise get current value
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedUserName = null
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (view != null) {
                    val textView = view as TextView
                    selectedUserName = textView.text.toString()
                }
            }
        }

        //Get List of users for spinner
        viewModel.getAllUsers().observe(this, Observer {
            if (it != null) {
                adapter.clear()
                adapter.addAll(it)
                listOfEmbeddedUsers.clear()
                listOfEmbeddedUsers.addAll(it)
            }
        })


        //Show change of runtime when new network is created
        viewModel.runtime.observe(this, Observer {
            showSnackBar(getString(R.string.runtime_changed, it.name))
        })


        //Create RecyclerViewAdapter and set up recycler view for embeddings counting
        val userAdapter = UserEmbeddingsAdapter()
        val recycler = binding.recycler
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = userAdapter
        recycler.setHasFixedSize(true)

        //Get List of user embeddings
        viewModel.getUserEmbeddings().observe(this, Observer {
            if (it != null) {
                userAdapter.add(it as ArrayList<UserEmbedding>)
            }
        })

        //When face was successfully added show img
        viewModel.cropedImage.observe(this, Observer {
            if (it != null) {
                binding.imageRGBBitmap.setImageBitmap(it.bitmap)
            }
        })

        //Show runtime, how long it take to add new user, measure of speed of nna
        viewModel.embeddingCreated.observe(this, Observer {
            if (it != null) {
                showSnackBar(getString(R.string.inference_time,it.networkInference.toString()))
            }
        })


    }


    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        //Add person button to appbar
        if (isDeleteItemMenuCreated) {
            menu.add(2000, deleteItemMenuID, Menu.NONE, R.string.facenet_delete_icon).icon =
                getDrawable(R.drawable.ic_delete)

            val item = menu[menu.size() - 1]
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            isDeleteItemMenuCreated = false
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return item?.let {
            when (it.itemId) {
                deleteItemMenuID -> {
                    createDeleteEmbeddingDialog()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

        } ?: super.onOptionsItemSelected(item)

    }

    private fun createDeleteEmbeddingDialog() {
        val dialog = DeleteEmbeddingsDialog()

        dialog.setListener(object : DeleteAllEmbeddingsListener {
            override fun onClick() {
                viewModel.deleteAllEmbeddings()
            }
        })

        dialog.show(supportFragmentManager, "custom_delete_dialog")
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                if (selectedUserName != null) {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, result.uri)
                    rescaleFace(bitmap)
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    private fun rescaleFace(bitmap: Bitmap) {
        if(selectedUserName != null) {
            val bitm = Bitmap.createScaledBitmap(bitmap, 96, 96, true)
            viewModel.setEmbeddedUser(EmbeddedUser(selectedUserName!!,bitm))
        }
    }


    override fun setUpBinding(): ActivityFacenetRecorderBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_facenet_recorder)
    }

    override fun changeRuntime(runtime: NeuralNetwork.Runtime) {
        if (!viewModel.changeRuntime(runtime)) {
            showSnackBar(getString(R.string.runtime_selected,runtime.name))
        }
    }

    override fun setToolbarView(): View {
        return binding.myToolbar
    }

    override fun setTitleOfToolbar(): String {
        return getString(R.string.facenet_face_recording)
    }


    private fun createDialog() {
        val dialog = AddPersonDialog()

        dialog.setListener(object : AddPersonClickListener {
            override fun onClick(name: String) {
                val user = User(name)
                if (user !in listOfEmbeddedUsers) {
                    viewModel.saveUser(user)
                    selectedUserName = user.userName
                    showSnackBar(getString(R.string.user_add_to_database, user.userName))
                } else {
                    showSnackBar(getString(R.string.user_already_exists))
                }
            }
        })

        dialog.show(supportFragmentManager, "custom_add_dialog")
    }


}
