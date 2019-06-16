package com.palopodstreleny.facenet.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.palopodstreleny.facenet.R

/**
 *
 * Dialog for user and embeddings deletion
 *
 */
class DeleteEmbeddingsDialog : DialogFragment() {

    private var myListener: DeleteAllEmbeddingsListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            //Set up builder
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.delete_embeddings)
                .setPositiveButton(R.string.button_done) { dialog, _ -> myListener?.onClick() }
                .setNegativeButton(R.string.button_cancel) { dialog, _ -> dialog.dismiss() }
            builder.setMessage(R.string.embeddings_delete_message)
            builder.create()

        } ?: throw IllegalStateException("Activity can not be null")
    }

    /**
     *
     * Setter for DeleteAllEmbeddingsListener listener
     *
     * @param listener DeleteAllEmbeddingsListener
     *
     */
    fun setListener(listener: DeleteAllEmbeddingsListener) {
        myListener = listener
    }


}


interface DeleteAllEmbeddingsListener {
    fun onClick()
}