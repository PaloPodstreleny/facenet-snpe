package com.palopodstreleny.facenet.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.palopodstreleny.facenet.R
import com.palopodstreleny.facenet.databinding.DialogLayoutBinding
import com.qualcomm.qti.snpe.NeuralNetwork.Runtime.*

/**
 *
 * Dialog for runtime availability information
 *
 */
class RuntimeDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val binding: DialogLayoutBinding =
                DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_layout, null, false)

            val arguments = arguments

            //Get data from bundle if bundle exists
            if (arguments != null) {
                binding.cpu = arguments.getBoolean(CPU.name)
                binding.gpu = arguments.getBoolean(GPU.name)
                binding.gpu16 = arguments.getBoolean(GPU_FLOAT16.name)
                binding.dsp = arguments.getBoolean(DSP.name)
                binding.aip = arguments.getBoolean(AIP.name)
            }

            //Set up builder
            val builder = AlertDialog.Builder(it)
            builder.setView(binding.root)
            builder.setTitle(R.string.available_runtimes)
                .setPositiveButton(R.string.button_done) { dialog, _ -> dialog.dismiss() }
            builder.create()

        } ?: throw IllegalStateException("Activity can not be null")
    }


}