package com.example.medicinereminder.AddEditMedicine.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicinereminder.AddEditMedicine.presenter.AddMedicationPresenter;
import com.example.medicinereminder.AddEditMedicine.presenter.AddMedicationPresenterInterface;
import com.example.medicinereminder.DisplayMedicine.DisplayMedActivity;
import com.example.medicinereminder.HomeScreen.HomeFragment;
import com.example.medicinereminder.HomeScreen.view.Home_Screen;
import com.example.medicinereminder.databinding.ActivityAddEditMedBinding;
import com.example.medicinereminder.localdatabase.LocalSource;
import com.example.medicinereminder.model.MedicationPOJO;
import com.example.medicinereminder.model.TimeUtility;
import com.example.medicinereminder.repository.Repository;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddEditMedActivity extends AppCompatActivity implements onClickAddMedication, AddAndEditMedicationInterface {
    //select Add or Edit Screen
    private boolean isAdd = true;

    private ActivityAddEditMedBinding binding;
    private MedicationPOJO medication = new MedicationPOJO();

    private boolean isFillReminder = false;
    private boolean chooseTimesFlag = false;
    private long startDate;
    private long endDate;

    //spinner
    private String medicationType = "";
    private String strengthType = "";
    private String instruction = "";
    private String takeTimePerWeek = "";
    private String takeTimePerDay;


    //editable
    private String dosePerDay = "";
    private String medicationName;
    private String start_date;
    private String end_date;
    private String strength = "";
    private String leftNumber = "";
    private String leftNumberReminder = "";
    private String medicineSize = "";
    private String reason = "";

    private Map<String, Boolean> timeSimpleTaken;
    private Map<String, Boolean> dateTimeSimpleTaken=new HashMap<>();
    private Map<String, Boolean> dateTimeAbsTaken=new HashMap<>();
    private List<String> timeAbs;

    private TimeSelectedAdapter timeSelectedAdapter;

    private String email;

    //dialog
    MaterialAlertDialogBuilder dialogBuilder;

    ArrayAdapter<CharSequence> adapterMedType;
    ArrayAdapter<CharSequence> adapterEating;
    ArrayAdapter<CharSequence> adapterStrType;
    ArrayAdapter<CharSequence> adapterPerWeek;
    ArrayAdapter<CharSequence> adapterPerDay;

    String[] medType = {"pills", "drops", "injection", "powder", "syrup"};
    String[] eatingArr = {"Before eating", "While eating","After eating"};
    String[] strTypeArr = {"mg", "mEq", "mL", "mg/g", "mg/cm","mcg","IU","g","%"};
    String[] perWeekArr = {"Everyday", "every two days", "every three days", "every four days", "every five days" ,"once a week"};
    String[] perDayArr = {"once Daily", "twice Daily", "3 times a Day", "4 times a Day"};
    SharedPreferences preferences;
    public static final String SHARED_PER = "SHAREDfILE";
    public static final String USER_EMAIL = "USER_EMAIL";
    AddMedicationPresenterInterface presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddEditMedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter=new AddMedicationPresenter(this,this);
        Intent intent=getIntent();
        isAdd=intent.getBooleanExtra("isAdd",true);

         preferences = getSharedPreferences(SHARED_PER, MODE_PRIVATE);
         email=preferences.getString(USER_EMAIL,"null");
         Log.i("AddScreen",email);
        handleSpinners();
        if (!isAdd) {
            handleEditScreen();
        } else {
            medication = new MedicationPOJO();
            handleAddScreen();
         }


    }



    private void handleSpinners(){

        binding.ivAddBack.setOnClickListener(v -> lunchExitDialog());
        binding.btnStartDate.setOnClickListener(v -> showDatePicker(binding.tvSelectedStartDate, "start"));

        binding.btnEndDate.setOnClickListener(v -> showDatePicker(binding.tvSelectedEndDate, "end"));

        adapterMedType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, medType);
        adapterMedType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spEditMedType.setAdapter(adapterMedType);
        binding.spEditMedType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    medicationType=adapterView.getItemAtPosition(i).toString();


                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapterEating = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, eatingArr);
        adapterEating.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spEditMedEating.setAdapter(adapterEating);
        binding.spEditMedEating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    instruction=adapterView.getItemAtPosition(i).toString();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        adapterPerWeek = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, perWeekArr);
        adapterPerWeek.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spOccurrWeek.setAdapter(adapterPerWeek);
        binding.spOccurrWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    takeTimePerWeek=adapterView.getItemAtPosition(i).toString();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapterStrType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, strTypeArr);
        adapterStrType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spEditStrengthType.setAdapter(adapterStrType);
        binding.spEditStrengthType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    strengthType=adapterView.getItemAtPosition(i).toString();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapterPerDay = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, perDayArr);
        adapterPerDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spOccurrDay.setAdapter(adapterPerDay);
        binding.spOccurrDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    int n;
                    takeTimePerDay=adapterView.getItemAtPosition(i).toString();
                    switch (takeTimePerDay){
                        case "once Daily":
                            n=1;
                            break;
                        case "twice Daily":
                            n=2;
                            break;
                        case "3 times a Day":
                            n=3;
                            break;
                        case "4 times a Day":
                            n=4;
                            break;
                        default:
                            n=1;
                    }
                    setDosePerTime(n);
                    Log.i("Toooba",n+"");
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void handleEditScreen() {
        medication = (MedicationPOJO) getIntent().getSerializableExtra("med");
        binding.tvTitle.setText("Edit");
        setSpinnerResult(medication);
        setEditText();
        binding.btnDoneAddEdit.setOnClickListener(v -> {
            getEditableText();
            if (medicationName.isEmpty()) {
                binding.etEditMedName.setError("Medication name required");
                binding.etEditMedName.requestFocus();
            } else if (start_date.equals("Selected Start Date")) {
                binding.tvSelectedStartDate.setError("Start date required");
                binding.tvSelectedStartDate.requestFocus();
            } else if (end_date.equals("Selected End Date")) {
                binding.tvSelectedEndDate.setError("End date required");
                binding.tvSelectedEndDate.requestFocus();
            } else {
                getEditableText();
                setEditTextResultToPOJO();
                setSpinnerResultToPOJO();
                setArraysAndMapsResultToPOJO();
                onClick(medication);

                //Todo handle sending the object to display activity and work manager and navigate
                //setWorkTimer();

                Intent intent = new Intent(AddEditMedActivity.this,DisplayMedActivity.class);
                intent.putExtra("med", (Serializable) medication);
                startActivity(intent);
            }
        });
        initRecycleView(medication);
    }

    private void handleAddScreen() {
        binding.tvTitle.setText("ADD");

        binding.btnDoneAddEdit.setOnClickListener(v -> {
            getEditableText();
            if (medicationName.isEmpty()) {
                binding.etEditMedName.setError("Medication name required");
                binding.etEditMedName.requestFocus();
            } else if (start_date.equals("Selected Start Date")) {
                binding.tvSelectedStartDate.setError("Start date required");
                binding.tvSelectedStartDate.requestFocus();
            } else if (end_date.equals("Selected End Date")) {
                binding.tvSelectedEndDate.setError("End date required");
                binding.tvSelectedEndDate.requestFocus();
            } else {
                setEditTextResultToPOJO();
                setSpinnerResultToPOJO();
                setArraysAndMapsResultToPOJO();
                medication.setId(Calendar.getInstance().getTimeInMillis()+medicationName+endDate);
                onClick(medication);
                startActivity(new Intent(AddEditMedActivity.this, Home_Screen.class));
                //Todo handle  work manager
                //setWorkTimer();


            }
        });
        initRecycleView(medication);
        handleSpinners();

    }



    private void lunchExitDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Are you sure you want to go back no changes will be saved");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                (dialog, which) -> {
                    if(isAdd){
                        startActivity(new Intent(AddEditMedActivity.this, Home_Screen.class));
                    }else{
                        startActivity(new Intent(AddEditMedActivity.this,DisplayMedActivity.class));
                    }
                    dialog.dismiss();

                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();

    }

    @Override
    public void onClick(MedicationPOJO medication) {
        if (isAdd)
            addMedication(medication);
        else
            updateMedication(medication);
    }

    @Override
    public void updateMedication(MedicationPOJO medication) {
        presenter.updateToDatabase(medication,email);

    }

    @Override
    public void addMedication(MedicationPOJO medication) {
        presenter.addToDatabase(medication,email);
    }

    @Override
    public void onSuccess() {
        if(isAdd){
            Toast.makeText(this, "Successfully Added!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure() {
        Toast.makeText(this, "Failed to Add", Toast.LENGTH_SHORT).show();
    }


    private void getEditableText() {
        medicationName = binding.etEditMedName.getEditableText().toString().trim();
        dosePerDay = binding.etEditNumDose.getEditableText().toString().trim();
        start_date = binding.tvSelectedStartDate.getText().toString().trim();
        end_date = binding.tvSelectedEndDate.getText().toString().trim();
        strength = binding.etEditStrengthDose.getEditableText().toString();
        leftNumber = binding.etEditLeft.getEditableText().toString();
        leftNumberReminder = binding.etRefillReminder.getEditableText().toString();
        medicineSize = binding.etEditMedSize.getEditableText().toString();
        reason = binding.etEditReason.getEditableText().toString();
    }
    private void setEditTextResultToPOJO() {
        medication.setMedicationName(medicationName);
        if (!strength.isEmpty())
            medication.setStrength(Integer.parseInt(strength));
        if (!leftNumber.isEmpty())
            medication.setLeftNumber(Integer.parseInt(leftNumber));
        if (!leftNumberReminder.isEmpty())
            medication.setLeftNumberReminder(Integer.parseInt(leftNumberReminder));
        if (!start_date.equals("Selected Start Date"))
            medication.setStartDate(startDate);
        if (!end_date.equals("Selected End Date"))
            medication.setEndDate(endDate);
        if (!medicineSize.isEmpty())
            medication.setMedicineSize(Integer.parseInt(medicineSize));
        if (!dosePerDay.isEmpty())
            medication.setDoseNum(dosePerDay);

        medication.setMedicationReason(reason);
        medication.setEmail(email);
        medication.setActive(true);
    }

    private void setSpinnerResultToPOJO() {
        medication.setMedicationType(medicationType);
        medication.setStrengthType(strengthType);
        medication.setTakeTimePerDay(takeTimePerDay);
        medication.setInstruction(instruction);
        medication.setTakeTimePerWeek(takeTimePerWeek);
    }


    private void setArraysAndMapsResultToPOJO() {
        timeSimpleTaken=timeSelectedAdapter.getTimeMap();
        timeAbs=timeSelectedAdapter.getTimeList();
        int day;
        switch(takeTimePerWeek){
            case "Everyday":
                day=1;
                break;
            case "every two days":
                day=2;
                break;
            case "every three days":
                day=3;
                break;
            case "every four days":
                day=4;
                break;
            case "every five days":
                day=5;
                break;
            case "once a week":
                day=7;
                break;
            default:
                day=1;
        }
        for (long i= startDate;i<=endDate;i+=(day*24*60*60000L)){
            dateTimeSimpleTaken.put(TimeUtility.getDateString(i),false);
            for (int j=0;j<timeAbs.size();j++)
            dateTimeAbsTaken.put((i+timeAbs.get(j))+"",false);
        }

        medication.setDateTimeAbsTaken(dateTimeAbsTaken);
        medication.setDateTimeSimpleTaken(dateTimeSimpleTaken);
        medication.setTimeSimpleTaken(timeSimpleTaken);
        medication.setDateTimeAbs(timeAbs);

    }


    private void showDatePicker(TextView v, String s) {
        final Calendar myCalender = Calendar.getInstance();
        int year = myCalender.get(Calendar.YEAR);
        int month = myCalender.get(Calendar.MONTH);
        int day = myCalender.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener myDateListener = (view, year1, month1, day1) -> {
            if (view.isShown()) {
                month1 = month1 + 1;
                String date = day1 + "-" + month1 + "-" + year1;
                if (s.equals("start")) {
                    startDate = TimeUtility.getDateMillis(date);
                } else {
                    endDate = TimeUtility.getDateMillis(date);
                }
                v.setText(date);
            }

        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                myDateListener, year, month, day);
        String title = "Choose " + s + " date";
        datePickerDialog.setTitle(title);

        datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        datePickerDialog.show();
    }


    private void initRecycleView(MedicationPOJO medication) {
        int n = 0;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (medication.getTimeSimpleTaken() != null && medication.getTimeSimpleTaken().size() != 0) {
            n = medication.getDateTimeAbs().size();
            Log.i("toooba",medication.getTimeSimpleTaken().toString());
            timeSelectedAdapter = new TimeSelectedAdapter(n, this, medication.getTimeSimpleTaken());
            timeSelectedAdapter.setTimeAndDose(medication.getTimeSimpleTaken());
        }else{
            timeSelectedAdapter = new TimeSelectedAdapter(n, this, medication.getTimeSimpleTaken());

        }
        binding.recycleChooseTime.setLayoutManager(layoutManager);
        binding.recycleChooseTime.setAdapter(timeSelectedAdapter);

    }

    private void setDosePerTime(int n) {
        timeSelectedAdapter.setDosePerDay(n);
        timeSelectedAdapter.notifyDataSetChanged();
    }

    private void setSpinnerResult(MedicationPOJO medication) {
        if (!medication.getMedicationType().isEmpty()) {
            binding.spEditMedType.setSelection(adapterMedType.getPosition(medication.getMedicationType()));
        }

        if (!medication.getStrengthType().isEmpty()) {
            binding.spEditStrengthType.setSelection(adapterStrType.getPosition(medication.getStrengthType()));
        }

        if (!medication.getTakeTimePerDay().isEmpty()) {
            binding.spOccurrDay.setSelection(adapterPerDay.getPosition(medication.getTakeTimePerDay()));
        }

        if (!medication.getInstruction().isEmpty()) {
            binding.spEditMedEating.setSelection(adapterEating.getPosition(medication.getInstruction()));
        }

        if (!medication.getTakeTimePerWeek().isEmpty()) {
            binding.spOccurrWeek.setSelection(adapterPerWeek.getPosition(medication.getTakeTimePerWeek()));
        }
    }

    @SuppressLint("SetTextI18n")
    private void setEditText() {
        binding.etEditMedName.setText(medication.getMedicationName());
        binding.etEditNumDose.setText(medication.getDoseNum());
        binding.tvSelectedStartDate.setText(TimeUtility.getDateString(medication.getStartDate()));
        startDate = medication.getStartDate();
        binding.tvSelectedEndDate.setText(TimeUtility.getDateString(medication.getEndDate()));
        endDate = medication.getEndDate();
        binding.etEditStrengthDose.setText(String.valueOf(medication.getStrength()));
        binding.etEditReason.setText(medication.getMedicationReason().isEmpty() ? "" : medication.getMedicationReason());
        binding.etEditLeft.setText(medication.getLeftNumber()+"");
        binding.etEditMedSize.setText(medication.getMedicineSize()+"");
        binding.etRefillReminder.setText(medication.getLeftNumberReminder()+"");
    }
    //Todo set worker notification
/*
    private void setWorkTimer() {
        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(MyPeriodicWorkManger.class,
                3, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this.getContext()).enqueueUniquePeriodicWork("Counter", ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);

    }

*/


}
