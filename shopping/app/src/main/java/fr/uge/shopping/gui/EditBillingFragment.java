package fr.uge.shopping.gui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import fr.uge.shopping.EditActivity;
import fr.uge.shopping.PreferencesManager;
import fr.uge.shopping.R;
import fr.uge.shopping.model.BillingDetails;
import fr.uge.shopping.model.ShippingAddress;

import java.util.ArrayList;

public class EditBillingFragment extends Fragment {
    private String infoName;
    private PreferencesManager preferencesManager;
    private BillingDetails details;
    private ArrayList<Boolean> errors;


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
        return inflater.inflate(R.layout.edit_billing_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.errors = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            this.errors.add(true);
        }

        this.save = getView().findViewById(R.id.editBillingFragmentSave);
        this.back = getView().findViewById(R.id.editBillingFragmentBack);

        this.cardHolder = getView().findViewById(R.id.editBillingFragmentCardHolder);
        this.cardHolder.setText(this.details.cardHolder);


        this.cardNumber = getView().findViewById(R.id.editBillingFragmentCardNumber);
        Drawable backgroundCardNumber =  this.cardNumber.getBackground();
        this.cardNumber.addTextChangedListener(new TextValidator(this.cardNumber) {
            @Override
            public void validate(TextView textView, String text) {
                if(text.length() == 16 && TextUtils.isDigitsOnly(text)) {
                    textView.setBackground(backgroundCardNumber);
                    errors.set(0, false);
                } else {
                    textView.setBackgroundColor(Color.argb(50, 255, 0, 0));
                    errors.set(0, true);
                }
                save.setEnabled(checkErrors());
            }
        });
        this.cardNumber.setText(this.details.cardNumber);

        this.expirationMonth = getView().findViewById(R.id.editBillingFragmentExpirationMonth);
        Drawable backgroundExpirationMonth =  this.expirationMonth.getBackground();
        this.expirationMonth.addTextChangedListener(new TextValidator(this.expirationMonth) {
            @Override
            public void validate(TextView textView, String text) {
                if(!text.isEmpty() && TextUtils.isDigitsOnly(text)) {
                    int n = Integer.parseInt(text);
                    if (n > 0 && n <= 12) {
                        textView.setBackground(backgroundExpirationMonth);
                        errors.set(1, false);
                    } else {
                        textView.setBackgroundColor(Color.argb(50, 255, 0, 0));
                        errors.set(1, true);
                    }
                } else {
                    textView.setBackgroundColor(Color.argb(50, 255, 0, 0));
                    errors.set(1, true);
                }
                save.setEnabled(checkErrors());
            }
        });
        this.expirationMonth.setText(String.valueOf(this.details.expirationMonth));

        this.expirationYear = getView().findViewById(R.id.editBillingFragmentExpirationYear);
        Drawable backgroundExpirationYear =  this.expirationYear.getBackground();
        this.expirationYear.addTextChangedListener(new TextValidator(this.expirationYear) {
            @Override
            public void validate(TextView textView, String text) {
                if(!text.isEmpty() && TextUtils.isDigitsOnly(text)) {
                    int n = Integer.parseInt(text);
                    if (n > 2020 && n <= 2040) {
                        textView.setBackground(backgroundExpirationYear);
                        errors.set(2, false);
                    } else {
                        textView.setBackgroundColor(Color.argb(50, 255, 0, 0));
                        errors.set(2, true);
                    }
                } else {
                    textView.setBackgroundColor(Color.argb(50, 255, 0, 0));
                    errors.set(2, true);
                }
                save.setEnabled(checkErrors());
            }
        });
        this.expirationYear.setText(String.valueOf(this.details.expirationYear));

        this.cryptogram = getView().findViewById(R.id.editBillingFragmentCryptogram);
        Drawable backgroundCryptogram =  this.cryptogram.getBackground();
        this.cryptogram.addTextChangedListener(new TextValidator(this.cryptogram) {
            @Override
            public void validate(TextView textView, String text) {
                if(!text.isEmpty() &&  TextUtils.isDigitsOnly(text)) {
                    int n = Integer.parseInt(text);
                    if (n > 0 && n <= 999) {
                        textView.setBackground(backgroundCryptogram);
                        errors.set(3, false);
                    } else {
                        textView.setBackgroundColor(Color.argb(50, 255, 0, 0));
                        errors.set(3, true);
                    }
                } else {
                    textView.setBackgroundColor(Color.argb(50, 255, 0, 0));
                    errors.set(3, true);
                }
                save.setEnabled(checkErrors());
            }
        });
        this.cryptogram.setText(String.valueOf(this.details.cryptogram));


        this.back.setOnClickListener( ev -> {
            close();
        });

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

    private boolean checkErrors() {
        for(boolean error : errors){
            if (error)
                return false;
        }
        return true;
    }
}
