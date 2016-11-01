package com.sayone.omidyar.view;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sayone.omidyar.R;
import com.sayone.omidyar.model.Quantity;
import com.sayone.omidyar.model.Survey;

import java.util.HashSet;
import java.util.List;

import io.realm.Realm;

/**
 * Created by sayone on 24/10/16.
 */
public class UnitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<String> unitList;
    private UnitsActivity mContext;
    private int poss=0;
    Realm realm;
    public int flag;

    public UnitAdapter(List<String> unitList, UnitsActivity context) {
        this.unitList = unitList;
        mContext = context;
    }

    public class UnitViewHolder extends RecyclerView.ViewHolder {
        private TextView unitName;
        private ImageView imageEditButton,deleteButton;

        public UnitViewHolder(View itemView) {
            super(itemView);
            unitName = (TextView) itemView.findViewById(R.id.unit_name);
            imageEditButton=(ImageView) itemView.findViewById(R.id.image_edit_unit);
            deleteButton=(ImageView)itemView.findViewById(R.id.button_delete);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    Quantity quantity = realm.where(Quantity.class)
                            .equalTo("quantityName",unitName.getText().toString())
                            .findFirst();
                    quantity.deleteFromRealm();
                    realm.commitTransaction();
                    Toast toast = Toast.makeText(mContext,"Deleted", Toast.LENGTH_SHORT);
                    toast.show();
                    mContext.dialogueCancel();
                    mContext.editExistingUnit();

                }
            });

            imageEditButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Spinner unitSpinner1, unitSpinner2;
                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.add_units);
                    dialog.setTitle("Enter new units");
                    dialog.setCancelable(false);

                    LinearLayout layout1 = null, layout2 = null;

                    Button popupCancel = (Button) dialog.findViewById(R.id.popup_cancel);
                    Button saveParticipant = (Button) dialog.findViewById(R.id.save_participant);
                    ImageView swapButton1 = (ImageView) dialog.findViewById(R.id.button_swap_1);
                    ImageView swapButton2 = (ImageView) dialog.findViewById(R.id.button_swap_2);

                    final EditText editTextSpecifyUnit1 = (EditText) dialog.findViewById(R.id.edit_text_specify_unit_1_1);
                    final EditText editTextSpecifyUnit2 = (EditText) dialog.findViewById(R.id.edit_text_specify_unit_1_2);
                    final EditText editTextSetUnit1 = (EditText) dialog.findViewById(R.id.edit_text_set_unit_1);
                    final EditText editTextSetUnit2 = (EditText) dialog.findViewById(R.id.edit_text_set_unit_2);
                    final EditText editTextSpecifyUnit = (EditText) dialog.findViewById(R.id.edit_text_specify_unit);

                    editTextSpecifyUnit.setText(unitName.getText());
                    editTextSpecifyUnit1.setText(unitName.getText());
                    editTextSpecifyUnit2.setText(unitName.getText());



                    layout1 = (LinearLayout) dialog.findViewById(R.id.layout_1);
                    layout2 = (LinearLayout) dialog.findViewById(R.id.layout_2);

                    unitSpinner1 = (Spinner) dialog.findViewById(R.id.spinner_unit_1);
                    unitSpinner2 = (Spinner) dialog.findViewById(R.id.spinner_unit_2);


                    ArrayAdapter<CharSequence> unit_adapter1 = ArrayAdapter.createFromResource(mContext,
                            R.array.unit_array, android.R.layout.simple_spinner_item);
                    unit_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ArrayAdapter<CharSequence> unit_adapter2 = ArrayAdapter.createFromResource(mContext,
                            R.array.unit_array, android.R.layout.simple_spinner_item);
                    unit_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    unitSpinner1.setAdapter(unit_adapter1);
                    unitSpinner2.setAdapter(unit_adapter2);

                    final LinearLayout finalLayout = layout1;
                    final LinearLayout finalLayout1 = layout2;
                    swapButton1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (flag == 0) {
                                finalLayout.setVisibility(View.VISIBLE);
                                finalLayout1.setVisibility(View.GONE);
                                flag = 1;
                            } else {
                                finalLayout.setVisibility(View.GONE);
                                finalLayout1.setVisibility(View.VISIBLE);
                                flag = 0;
                            }
                        }
                    });
                    swapButton2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (flag == 0) {
                                finalLayout.setVisibility(View.VISIBLE);
                                finalLayout1.setVisibility(View.GONE);
                                flag = 1;
                            } else {
                                finalLayout.setVisibility(View.GONE);
                                finalLayout1.setVisibility(View.VISIBLE);
                                flag = 0;
                            }
                        }
                    });


                    unitSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent,
                                                   View view, int pos, long id) {
                            String unit = parent.getItemAtPosition(pos).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });
                    unitSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent,
                                                   View view, int pos, long id) {
                            String unit = parent.getItemAtPosition(pos).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });

                    saveParticipant.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            realm = Realm.getDefaultInstance();

                            realm.beginTransaction();
                            Quantity quantity = realm.where(Quantity.class)
                                    .equalTo("quantityName",unitName.getText().toString())
                                    .findFirst();
                            quantity.setQuantityName(editTextSpecifyUnit.getText().toString());
                            realm.commitTransaction();
                            dialog.cancel();
                            mContext.dialogueCancel();
                            mContext.editExistingUnit();
                        }
                    });
                    popupCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }


            });

        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.unit_list_item, parent, false);

        return new UnitViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((UnitViewHolder)holder).unitName.setText(unitList.get(position+1));
        poss=position;
    }

    @Override
    public int getItemCount() {
        return unitList.size()-1;
    }

}
