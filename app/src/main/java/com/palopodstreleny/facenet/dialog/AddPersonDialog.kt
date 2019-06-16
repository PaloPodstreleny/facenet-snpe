package com.palopodstreleny.facenet.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.palopodstreleny.facenet.R
import com.palopodstreleny.facenet.databinding.DialogLayoutAddPersonBinding

/**
 *
 * Dialog for new user creation
 *
 */
class AddPersonDialog : DialogFragment() {

    var myListener: AddPersonClickListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val binding: DialogLayoutAddPersonBinding =
                DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_layout_add_person, null, false)

            //Set up builder
            val builder = AlertDialog.Builder(it)
            builder.setView(binding.root)
            builder.setTitle(R.string.add_person)
                .setPositiveButton(R.string.button_add) { _, _ ->
                    val name = binding.personName.text.toString()
                    myListener?.onClick(name)
                }
                .setNegativeButton(R.string.button_cancel) { dialog, _ ->
                    dialog.dismiss()
                }
            builder.create()

        } ?: throw IllegalStateException("Activity can not be null")
    }


    /**
     *
     * Setter for AddPersonClickListener listener
     *
     * @param listener AddPersonClickListener
     *
     */
    fun setListener(listener: AddPersonClickListener) {
        myListener = listener
    }


}


interface AddPersonClickListener {
    fun onClick(name: String)
}
