package kcc.soccernetwork.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import kcc.soccernetwork.R;

/**
 * Created by Administrator on 4/12/2016.
 */
public class SearchMatchDialog extends DialogFragment implements View.OnClickListener {
    public interface SearchMatchListener {
        public void onSearchMatch(DialogFragment dialog);
    }
    SearchMatchListener mListener;

    public SearchMatchDialog(SearchMatchListener listener) {
        super();
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_search_match, null);
        Button searchMatchBtn = (Button) view.findViewById(R.id.btn_accept_search_match);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_accept_search_match:
                mListener.onSearchMatch(this);
                dismiss();
                break;
        }
    }
}
