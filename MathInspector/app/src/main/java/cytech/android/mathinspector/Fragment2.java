package cytech.android.mathinspector;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class Fragment2 extends Fragment {
    EditText txtNumber;
    Button btnCheck, btnUserGuide;
    int target = 1;
    String str_value;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_2, container, false);

        txtNumber = view.findViewById(R.id.txtNumber);
        txtNumber.setEnabled(false);
        btnCheck = view.findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_value = txtNumber.getText().toString();
                if (!str_value.equals("")){
                    int value = Integer.parseInt(str_value);
                    if ((target != 0) && (value % target == 0)){
                        int temp = value / target;
                        Toast.makeText(view.getContext(), "YES, "+ value +" is a multiple of "+target +"\n"+target+" * "+temp+" = "+value, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(view.getContext(), "NO, "+ value +" is not a multiple of "+target, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        btnUserGuide = view.findViewById(R.id.btnUserGuide);
        btnUserGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View formElementsView = inflater.inflate(R.layout.userguide, null, false);
                //Create a dialog
                new AlertDialog.Builder(context)
                        .setView(formElementsView)
                        .setTitle(R.string.userguide_title)
                        .setPositiveButton("Close",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                        .show();
            }
        });

        return view;
    }

    // This is a public method that the Activity can use to communicate
    // directly with this Fragment
    public void receiveMessage(String data, int type) {
        switch (type){
            case 1:
                String text = "MULTIPLE OF " + data + "?";
                btnCheck.setText(text);
                target = Integer.parseInt(data);
                break;
            case 2:
                //TODO: Fix delete button
                int length = txtNumber.getText().length();
                if (length > 0) {
                    txtNumber.getText().delete(length - 1, length);
                }
                break;
            case 3:
                txtNumber.setText("");
                break;
            default:
                txtNumber.append(data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's state here
        outState.putInt("target", target);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        if (savedInstanceState != null){
            target = savedInstanceState.getInt("target");
            String text = "MULTIPLE OF " + target + "?";
            btnCheck.setText(text);
        }
    }
}
