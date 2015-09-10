package com.otter.gpslive;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class ErrorDialog extends DialogFragment {
    private static final String TAG = ErrorDialog.class.getSimpleName();

    public static final String FRAGMENT_TAG = "error_dialog";

    private static final String BUNDLE_MESSAGE = "message";

    private String mMessage;

    public static ErrorDialog newInstance(String message) {
        ErrorDialog fragment = new ErrorDialog();

        Bundle args = new Bundle();
        args.putString(BUNDLE_MESSAGE, message);
        fragment.setArguments(args);

        return fragment;
    }

    public ErrorDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMessage = getArguments().getString(BUNDLE_MESSAGE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.error)
                .setMessage(mMessage)
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });

        return builder.create();
    }
}
