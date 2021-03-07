package fr.uge.shopping.gui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import fr.uge.shopping.EditActivity;
import fr.uge.shopping.PreferencesManager;
import fr.uge.shopping.R;
import fr.uge.shopping.model.BillingDetails;
import fr.uge.shopping.model.ShippingAddress;

public class EditBillingFragment extends Fragment {
    private String infoName;
    private PreferencesManager preferencesManager;
    private BillingDetails details;


    private EditText cardHolder;
    private EditText cardNumber;
    private EditText expirationMonth;
    private EditText expirationYear;
    private EditText cryptogram;
    private Button back;
    private Button save;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.infoName = getArguments().getString("name");
        this.preferencesManager = PreferencesManager.getPreferencesManager(getContext());
        this.details = this.preferencesManager.getShoppingInfo(this.infoName).billing;
        return inflater.inflate(R.layout.edit_address_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.cardHolder = getView().findViewById(R.id.editBillingFragmentCardHolder);
        this.cardHolder.setText(this.details.cardHolder);

        this.cardNumber = getView().findViewById(R.id.editBillingFragmentCardNumber);
        this.cardNumber.setText(this.details.cardNumber);

        this.expirationMonth = getView().findViewById(R.id.editBillingFragmentExpirationMonth);
        this.expirationMonth.setText(this.details.expirationMonth);

        this.expirationYear = getView().findViewById(R.id.editBillingFragmentExpirationYear);
        this.expirationYear.setText(this.details.expirationYear);

        this.cryptogram = getView().findViewById(R.id.editBillingFragmentCryptogram);
        this.cryptogram.setText(this.details.cryptogram);

        this.back = getView().findViewById(R.id.editBillingFragmentBack);
        this.back.setOnClickListener( ev -> {
            close();
        });
        this.save = getView().findViewById(R.id.editBillingFragmentSave);
        this.save.setOnClickListener( ev -> {
            ((EditActivity) getActivity()).updateBilling(
                    new BillingDetails(
                            cardHolder.getText().toString(),
                            cardNumber.getText().toString(),
                            Integer.parseInt(expirationMonth.getText().toString()),
                            Integer.parseInt(expirationYear.getText().toString()),
                            Integer.parseInt(cryptogram.getText().toString()))
            );
            close();
        });
    }

    private void close() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .remove(this)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .commit();
    }
}
