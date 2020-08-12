package com.herma.apps.drivertraining.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.snackbar.Snackbar;
import com.herma.apps.drivertraining.R;

public class Common {

    Context context;

    public Common(Context context){
        this.context = context;
    }
    /**
     * Check if there is the network connectivity
     *
     * @return true if connected to the network
     */

    public void showDialog(String title, String message, Activity context) {
        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert);
        // Set an Icon and title, and message
        builder.setIcon(R.drawable.ic_warning);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(context.getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                    context.getApplication().startActivityForResult(new Intent(Settings.ACTION_SETTINGS), REQUEST_CODE_DIALOG);
            }
        });
        builder.setNegativeButton(context.getString(R.string.cancel), null);

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
