package com.caffeine.dlabloodfamily.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.TextView
import com.caffeine.dlabloodfamily.R

object ProgressDialog {

    private lateinit var dialog: Dialog

    fun showProgressDialog(context: Context){
        dialog = Dialog(context)
        dialog.setContentView(R.layout.progress_layout)

        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    fun dismiss(){
        dialog.dismiss()
    }
}