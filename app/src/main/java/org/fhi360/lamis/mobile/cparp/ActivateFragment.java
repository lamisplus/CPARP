package org.fhi360.lamis.mobile.cparp;


import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.shashank.sony.fancytoastlib.FancyToast;

import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.dao.UserDAO;
import org.fhi360.lamis.mobile.cparp.model.UsernameAndPassword;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActivateFragment extends Fragment implements View.OnClickListener {
    private View layout;
    private Button activateButton;
    private EditText userNameEditText;
    private EditText pinEditText;

    public ActivateFragment() {
        // Required empty public constructor
    }

    OnActivateButtonClickListener listener;

    public interface OnActivateButtonClickListener {
         void activate(String userName, String pin);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_activate, container, false);


        //Get reference to the EditView
        userNameEditText = layout.findViewById(R.id.user_name);
        pinEditText =  layout.findViewById(R.id.pin);
        //Get reference to the Configure Button
        activateButton =  layout.findViewById(R.id.activate_button);
        activateButton.setOnClickListener(this);
        // Inflate the layout for this fragment
        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //This makes sure that the container activity has implemented the callback interface
        try {
            this.listener = (OnActivateButtonClickListener) context;
        } catch (ClassCastException exception) {
            throw new ClassCastException(context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onClick(View view) {
        if (validateInput(userNameEditText.getText().toString(), pinEditText.getText().toString())) {
            String userName = userNameEditText.getText().toString();
            String pin = pinEditText.getText().toString();
            String deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            String accountUserName = "";
            String accountPassword = "";
            UsernameAndPassword usernameAndPasswords = new UserDAO(getContext()).getUsrnameAndPassword();
            if (usernameAndPasswords != null) {
                accountUserName = usernameAndPasswords.getAccountUserName();
                accountPassword = usernameAndPasswords.getAccountPassword();
            }
            String url = "resources/webservice/mobile/activate/" + userName.trim() + "/" + pin + "/" + deviceId + "/" + accountUserName + "/" + accountPassword;
            listener.activate("GET", url);
        }
    }


    private boolean validateInput(String username, String pin) {
        if (username.isEmpty()) {
            userNameEditText.setError("Enter username");
            return false;
        } else if (pin.isEmpty()) {
            pinEditText.setError("Enter Activation code");
            return false;
        }
        return true;


    }


}
