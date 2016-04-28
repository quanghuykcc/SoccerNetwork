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
 * Created by Administrator on 4/13/2016.
 */
public class VerifyDialog extends DialogFragment implements View.OnClickListener {

    Activity mActivity;
    EditText mVerifyCodeEt;
    public VerifyDialog(Activity activity) {
        mActivity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_verification, null);
        Button confirmBtn = (Button) view.findViewById(R.id.bt_ok_verify);
        confirmBtn.setOnClickListener(this);
        mVerifyCodeEt = (EditText) view.findViewById(R.id.et_code_verify);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_ok_verify:
                break;
        }
    }
}
