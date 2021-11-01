package cytech.android.mathinspector;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class Fragment1 extends Fragment {
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnDel, btnClear, btn0;
    int idLastState = R.id.btn1;
    private OnFragment1Listener mCallback;

    public void clearView(){
        btn0.setTextColor(Color.parseColor("WHITE"));
        btn1.setTextColor(Color.parseColor("WHITE"));
        btn2.setTextColor(Color.parseColor("WHITE"));
        btn3.setTextColor(Color.parseColor("WHITE"));
        btn4.setTextColor(Color.parseColor("WHITE"));
        btn5.setTextColor(Color.parseColor("WHITE"));
        btn6.setTextColor(Color.parseColor("WHITE"));
        btn7.setTextColor(Color.parseColor("WHITE"));
        btn8.setTextColor(Color.parseColor("WHITE"));
        btn9.setTextColor(Color.parseColor("WHITE"));
    }

    public void setBtnListener(Button button, String num){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.messageFromFragment1(num, 0);
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mCallback.messageFromFragment1(num, 1);
                clearView();
                button.setTextColor(Color.parseColor("RED"));
                idLastState = button.getId();
                return true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_1, container, false);

        btn1 = view.findViewById(R.id.btn1);
        setBtnListener(btn1, "1");
        btn2 = view.findViewById(R.id.btn2);
        setBtnListener(btn2, "2");
        btn3 = view.findViewById(R.id.btn3);
        setBtnListener(btn3, "3");
        btn4 = view.findViewById(R.id.btn4);
        setBtnListener(btn4, "4");
        btn5 = view.findViewById(R.id.btn5);
        setBtnListener(btn5, "5");
        btn6 = view.findViewById(R.id.btn6);
        setBtnListener(btn6, "6");
        btn7 = view.findViewById(R.id.btn7);
        setBtnListener(btn7, "7");
        btn8 = view.findViewById(R.id.btn8);
        setBtnListener(btn8, "8");
        btn9 = view.findViewById(R.id.btn9);
        setBtnListener(btn9, "9");
        btn0 = view.findViewById(R.id.btn0);
        setBtnListener(btn0, "0");
        btnDel = view.findViewById(R.id.btnDel);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.messageFromFragment1("", 2);
            }
        });
        btnClear = view.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.messageFromFragment1("", 3);
            }
        });
        return view;
    }

    // This is the interface that the Activity will implement
    // so that this Fragment can communicate with the Activity.
    public interface OnFragment1Listener {
        void messageFromFragment1(String data, int type);
    }

    // This method insures that the Activity has actually implemented our
    // listener and that it isn't null.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragment1Listener) {
            mCallback = (OnFragment1Listener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragment1Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's state here
        outState.putInt("idLastState", idLastState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        if (savedInstanceState != null){
            idLastState = savedInstanceState.getInt("idLastState");
            Button btnActivated = view.findViewById(idLastState);
            clearView();
            btnActivated.setTextColor(Color.parseColor("RED"));
        }
    }
}
