package tbc.uncagedmist.aadhaarotp.Utility;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import tbc.uncagedmist.aadhaarotp.R;

public class CustomProgressDialog {

    Context context;
    Dialog dialog;

    public CustomProgressDialog(Context context) {
        this.context = context;
        dialog = new Dialog(context);
    }

    public void showProgressDialog() {

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.load_layout);

        ImageView gifImageView = dialog.findViewById(R.id.gifImageView);

        Glide
                .with(context)
                .load(R.drawable.loading)
                .into(gifImageView);

        dialog.show();
    }

    public void hideProgressDialog(){
        dialog.dismiss();
    }
}
