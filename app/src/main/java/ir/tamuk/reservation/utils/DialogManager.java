package ir.tamuk.reservation.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Window;

import androidx.core.content.ContextCompat;

import ir.tamuk.reservation.Interfaces.DialogCommunicationInterface;
import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.LayoutDialogBinding;

public class DialogManager {

    public static void showExitDialog(Context context , DialogCommunicationInterface dialogCommunicationInterface){
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutDialogBinding binding = LayoutDialogBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        binding.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_logout));
        binding.title.setText(context.getString(R.string.exit_title));
        binding.description.setText(context.getString(R.string.exit_desc));
        binding.yes.setText("خروج");
        binding.no.setText("انصراف");

        binding.yes.setOnClickListener(view -> {
            dialogCommunicationInterface.yesAction(dialog);
        });

        binding.no.setOnClickListener(view -> {
            dialogCommunicationInterface.noAction(dialog);
        });

        dialog.show();

    }
}
