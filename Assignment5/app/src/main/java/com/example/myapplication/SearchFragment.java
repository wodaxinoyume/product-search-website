package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchFragment extends Fragment {

    private View view;
    private EditText keyword;
    private Spinner spinner;
    private CheckBox checkBox0;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private CheckBox checkBox5;
    private LinearLayout hiddenLayout;
    private EditText distance;
    private RadioButton radioButtonCurrent;
    private RadioButton radioButtonZipcode;
    private AutoCompleteTextView zipcode;
    private TextView keywordWarning;
    private TextView zipcodeWarning;
    private Button searchButton;
    private Button clearButton;
    private String currentLocation;


    public SearchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search, container, false);
        keyword = view.findViewById(R.id.keyword);
        spinner = view.findViewById(R.id.spinner);
        checkBox0 = view.findViewById(R.id.checkBox0);
        checkBox1 = view.findViewById(R.id.checkBox1);
        checkBox2 = view.findViewById(R.id.checkBox2);
        checkBox3 = view.findViewById(R.id.checkBox3);
        checkBox4 = view.findViewById(R.id.checkBox4);
        checkBox5 = view.findViewById(R.id.checkBox5);
        hiddenLayout = view.findViewById(R.id.hiddenLayout);
        distance = view.findViewById(R.id.distance);
        radioButtonCurrent = view.findViewById(R.id.radioButtonCurrent);
        radioButtonZipcode = view.findViewById(R.id.radioButtonZipcode);
        zipcode = view.findViewById(R.id.autoCompleteTextView);
        keywordWarning = view.findViewById(R.id.keywordwarning);
        zipcodeWarning = view.findViewById(R.id.zipcodewarning);
        searchButton = view.findViewById(R.id.searchButton);
        clearButton = view.findViewById(R.id.clearButton);

        radioButtonCurrent.setChecked(true);

        addSpinnerFunctionality();
        addHiddenLayerFunctionality();
        addRadioButtonFunctionality();
        addAutoCompleteFunctionality();
        addSearchButtonFunctionality();
        addClearButtonFunctionality();
        getCurrentLocationZipcode();

        return view;
    }

    private void addSpinnerFunctionality() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.Category,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }

    private void addHiddenLayerFunctionality() {
        hiddenLayout.setVisibility(View.GONE);

        checkBox5.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                radioButtonCurrent.setChecked(true);
                distance.setText("");
                hiddenLayout.setVisibility(View.VISIBLE);
            } else {
                radioButtonCurrent.setChecked(true);
                distance.setText("");
                hiddenLayout.setVisibility(View.GONE);
                zipcodeWarning.setVisibility(View.GONE);
            }
        });
    }

    private void addRadioButtonFunctionality() {
        radioButtonCurrent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    zipcode.setEnabled(false);
                    zipcode.setText("");
                    zipcodeWarning.setVisibility(View.GONE);
                    getCurrentLocationZipcode();
                }
            }
        });

        radioButtonZipcode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    zipcode.setText("");
                    zipcode.setEnabled(true);
                }
            }
        });
    }

    private void getCurrentLocationZipcode() {
        String apiUrl = "https://ipinfo.io/json?token=28bfb37bda3238";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String postalCode = response.getString("postal");
                            currentLocation = postalCode;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest);
    }

    private void addAutoCompleteFunctionality() {
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(5);
        zipcode.setFilters(filters);

        zipcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String userInput = charSequence.toString();
                performAutoCompleteApiCall(userInput);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void performAutoCompleteApiCall(String userInput) {
        // String apiUrl = "https://rugged-shuttle-402803.wn.r.appspot.com/IPSuggest?IP=" + userInput;
        String apiUrl = "https://stone-timing-405020.wl.r.appspot.com/IPSuggest?IP=" + userInput;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String[] suggestions = parseAutoCompleteResponse(response);

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, suggestions);
                        zipcode.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest);
    }

    private String[] parseAutoCompleteResponse(JSONObject response) {
        List<String> suggestionList = new ArrayList<>();

        try {
            JSONArray postalCodes = response.getJSONArray("postalCodes");

            for (int i = 0; i < postalCodes.length(); i++) {
                JSONObject postalCodeObject = postalCodes.getJSONObject(i);
                String postalCode = postalCodeObject.getString("postalCode");
                suggestionList.add(postalCode);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return suggestionList.toArray(new String[0]);
    }

    private void addSearchButtonFunctionality() {
        zipcodeWarning.setVisibility(View.GONE);
        keywordWarning.setVisibility(View.GONE);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean keywordValid = true;
                boolean zipcodeValid = true;
                if (keyword.getText().toString().equals("")) {
                    keywordValid = false;
                    keywordWarning.setVisibility(View.VISIBLE);
                } else {
                    keywordWarning.setVisibility(View.GONE);
                }

                if (checkBox5.isChecked() && radioButtonZipcode.isChecked()) {
                    if (zipcode.getText().toString().length() < 5) {
                        zipcodeValid = false;
                        zipcodeWarning.setVisibility(View.VISIBLE);
                    } else {
                        zipcodeWarning.setVisibility(View.GONE);
                    }
                }

                if (keywordValid && zipcodeValid) {
                    search();
                } else {
                    showToast();
                }
            }
        });
    }

    private void search() {
        String[] categoryID = {"0", "550", "2984", "267", "11450", "58058", "26395", "11233", "1249"};
        String endpoint = "https://stone-timing-405020.wl.r.appspot.com/search?";
        endpoint += "keywords=" + keyword.getText().toString();
        endpoint += "&category=" + categoryID[spinner.getSelectedItemPosition()];
        if (checkBox0.isChecked()) {
            endpoint += "&new=true";
        }
        if (checkBox1.isChecked()) {
            endpoint += "&used=true";
        }
//        if (checkBox2.isChecked()) {
//            endpoint += "&unspecified=true";
//        }
        if (checkBox3.isChecked()) {
            endpoint += "&localPickup=true";
        }
        if (checkBox4.isChecked()) {
            endpoint += "&freeShipping=true";
        }
        if (distance.getText().toString().isEmpty()) {
            endpoint += "&distance=10";
        } else {
            endpoint += "&distance=" + distance.getText().toString();
        }

        if (zipcode.getText().toString().isEmpty()) {
            endpoint += "&postalcode=" + currentLocation;
        } else {
            endpoint += "&postalcode=" + zipcode.getText().toString();
        }

        Intent intent = new Intent(requireContext(), MainSearchActivity.class);
        intent.putExtra("searchUrl", endpoint);

        // for debug purpose start
//        String note = endpoint.substring(endpoint.length() - 60, endpoint.length());
//        Toast.makeText(requireContext(), note, Toast.LENGTH_LONG).show();
        // for debug purpose end
        startActivity(intent);
    }

    private void showToast() {
        String message = "Please fix all fields with errors";
        Toast toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void addClearButtonFunctionality() {
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword.setText("");
                spinner.setSelection(0);
                checkBox0.setChecked(false);
                checkBox1.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                keywordWarning.setVisibility(View.GONE);
                zipcodeWarning.setVisibility(View.GONE);
            }
        });
    }
}