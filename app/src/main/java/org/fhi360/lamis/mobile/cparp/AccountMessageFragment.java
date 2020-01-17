package org.fhi360.lamis.mobile.cparp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import org.fhi360.lamis.mobile.cparp.dao.AccountDAO;
import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.model.Account;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountMessageFragment extends Fragment implements View.OnClickListener {
    private View layout;
    private Button okButton;
    private Context context;

    public AccountMessageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_account_message, container, false);
        context = inflater.getContext();

        Account account = new AccountDAO(context).getAccount();
        ((TextView) layout.findViewById(R.id.pharmacy)).setText(account.getPharmacy());
        ((TextView) layout.findViewById(R.id.address)).setText(account.getAddress());
        okButton =  layout.findViewById(R.id.ok_button);
        okButton.setOnClickListener(this);
        return layout;
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }

}
