package com.sayone.omidyar.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sayone.omidyar.BaseActivity;
import com.sayone.omidyar.R;
import com.sayone.omidyar.adapter.SurveyAdapter;
import com.sayone.omidyar.model.Survey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class UnitsActivity extends BaseActivity implements View.OnClickListener{

    TextView enterNewUnits,editExistingUnits;
    private Context context;
    int flag=1;
    private UnitAdapter unitAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_units);
        context=this;

        enterNewUnits=(TextView)findViewById(R.id.enter_new_units);
        editExistingUnits=(TextView)findViewById(R.id.edit_existing_units);

        enterNewUnits.setOnClickListener(this);
        editExistingUnits.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.enter_new_units:
                Spinner unitSpinner1,unitSpinner2;
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_units);
                dialog.setTitle("Enter new units");
                dialog.setCancelable(false);

                 LinearLayout layout1 = null,layout2 = null;

                Button popupCancel = (Button) dialog.findViewById(R.id.popup_cancel);
                Button saveParticipant = (Button) dialog.findViewById(R.id.save_participant);
                ImageView swapButton1=(ImageView) dialog.findViewById(R.id.button_swap_1);
                ImageView swapButton2=(ImageView) dialog.findViewById(R.id.button_swap_2);

                final EditText editTextSpecifyUnit1 = (EditText) dialog.findViewById(R.id.edit_text_specify_unit_1_1);
                final EditText editTextSpecifyUnit2 = (EditText) dialog.findViewById(R.id.edit_text_specify_unit_1_2);
                final EditText editTextSetUnit1=(EditText)dialog.findViewById(R.id.edit_text_set_unit_1);
                final EditText editTextSetUnit2=(EditText)dialog.findViewById(R.id.edit_text_set_unit_2);
                final EditText editTextSpecifyUnit=(EditText)dialog.findViewById(R.id.edit_text_specify_unit);


                layout1=(LinearLayout)dialog.findViewById(R.id.layout_1);
                layout2=(LinearLayout)dialog.findViewById(R.id.layout_2);

                unitSpinner1=(Spinner)dialog.findViewById(R.id.spinner_unit_1);
                unitSpinner2=(Spinner)dialog.findViewById(R.id.spinner_unit_2);


                ArrayAdapter<CharSequence> unit_adapter1 = ArrayAdapter.createFromResource(this,
                        R.array.unit_array, android.R.layout.simple_spinner_item);
                unit_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ArrayAdapter<CharSequence> unit_adapter2 = ArrayAdapter.createFromResource(this,
                        R.array.unit_array, android.R.layout.simple_spinner_item);
                unit_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                unitSpinner1.setAdapter(unit_adapter1);
                unitSpinner2.setAdapter(unit_adapter2);

                final LinearLayout finalLayout = layout1;
                final LinearLayout finalLayout1 = layout2;
                swapButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(flag==0){
                            finalLayout.setVisibility(View.VISIBLE);
                            finalLayout1.setVisibility(View.GONE);
                            flag=1;
                        }
                        else{
                            finalLayout.setVisibility(View.GONE);
                            finalLayout1.setVisibility(View.VISIBLE);
                            flag=0;
                        }
                    }
                });
                swapButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(flag==0){
                            finalLayout.setVisibility(View.VISIBLE);
                            finalLayout1.setVisibility(View.GONE);
                            flag=1;
                        }
                        else{
                            finalLayout.setVisibility(View.GONE);
                            finalLayout1.setVisibility(View.VISIBLE);
                            flag=0;
                        }
                    }
                });


                unitSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int pos, long id) {
                       String unit= parent.getItemAtPosition(pos).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });
                unitSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int pos, long id) {
                        String unit= parent.getItemAtPosition(pos).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });




                popupCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                break;

            case R.id.edit_existing_units:
                final Dialog dialog1 = new Dialog(context);
                dialog1.setContentView(R.layout.edit_unit);
                dialog1.setTitle("Edit units");
                dialog1.setCancelable(false);
                RecyclerView recyclerView = null;

                popupCancel = (Button) dialog1.findViewById(R.id.popup_cancel);
                saveParticipant = (Button) dialog1.findViewById(R.id.save_participant);
                recyclerView = (RecyclerView)dialog1.findViewById(R.id.recycler_unit_list);


                List<String> unitList = Arrays.asList(getResources().getStringArray(R.array.unit_array));


                unitAdapter = new UnitAdapter(unitList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(unitAdapter);


                popupCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.cancel();
                    }
                });
                dialog1.show();


                break;

        }
    }


}
