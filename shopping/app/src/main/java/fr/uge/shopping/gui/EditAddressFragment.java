package fr.uge.shopping.gui;

import android.os.Bundle;
import android.util.Log;
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
import fr.uge.shopping.MainActivity;
import fr.uge.shopping.PreferencesManager;
import fr.uge.shopping.R;
import fr.uge.shopping.model.ShippingAddress;

public class EditAddressFragment extends Fragment {
    private String infoName;
    private PreferencesManager preferencesManager;
    private ShippingAddress address;


    private EditText name;
    private EditText street;
    private EditText city;
    private EditText country;
    private Button back;
    private Button save;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.infoName = getArguments().getString("name");
        this.preferencesManager = PreferencesManager.getPreferencesManager(getContext());
        this.address = this.preferencesManager.getShoppingInfo(this.infoName).address;
        return inflater.inflate(R.layout.edit_address_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.name = getView().findViewById(R.id.editAddressFragmentName);
        this.name.setText(this.address.name);

        this.street = getView().findViewById(R.id.editAddressFragmentStreet);
        this.street.setText(this.address.street);

        this.city = getView().findViewById(R.id.editAddressFragmentCity);
        this.city.setText(this.address.city);

        this.country = getView().findViewById(R.id.editAddressFragmentCountry);
        this.country.setText(this.address.country);

        this.back = getView().findViewById(R.id.editAddressFragmentBack);
        this.back.setOnClickListener( ev -> {
            close();
        });
        this.save = getView().findViewById(R.id.editAddressFragmentSave);
        this.save.setOnClickListener( ev -> {
            ((EditActivity) getActivity()).updateAddress(
                    new ShippingAddress(
                            name.getText().toString(),
                            street.getText().toString(),
                            city.getText().toString(),
                            country.getText().toString())
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
