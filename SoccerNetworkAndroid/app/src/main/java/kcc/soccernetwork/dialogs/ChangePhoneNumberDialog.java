package kcc.soccernetwork.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import kcc.soccernetwork.R;

/**
 * Created by Administrator on 4/23/2016.
 */
public class ChangePhoneNumberDialog extends DialogFragment implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_change_phone:
                break;
        }
    }
    Activity activity;
    EditText newPhoneNumber;
    EditText retypeNewPhoneNumber;

    public ChangePhoneNumberDialog() {
        super();
    }

    public ChangePhoneNumberDialog(Activity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_pass,null);
        newPhoneNumber =(EditText)view.findViewById(R.id.et_new_phone);
        retypeNewPhoneNumber =(EditText)view.findViewById(R.id.et_comfirm_phone);
        Button okButton = (Button) view.findViewById(R.id.bt_change_phone);
        okButton.setOnClickListener(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
