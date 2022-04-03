package com.caffeine.dlabloodfamily.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.TextView
import com.caffeine.dlabloodfamily.R

object SuccessDialog {

    fun showAlertDialog(context: Context, msg : String, action : String){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.success_dialog)

        val message = dialog.findViewById<TextView>(R.id.popup_message)
        val actionBtn = dialog.findViewById<TextView>(R.id.action_ok)

        message?.text = msg
        actionBtn?.text = action

        message?.setTextColor(context.resources.getColor(R.color.colorDarkGrey))
        actionBtn?.setTextColor(context.resources.getColor(R.color.colorDarkGrey))

        actionBtn?.setOnClickListener{
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}