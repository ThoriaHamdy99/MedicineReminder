package com.example.medicinereminder.AddEditMedicine.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicinereminder.R;
import com.example.medicinereminder.databinding.ActivityAddEditMedBinding;
import com.example.medicinereminder.services.model.MedicationPOJO;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditMedActivity extends AppCompatActivity implements onClickAddMedication, AddAndEditMedicationInterface {
    //select Add or Edit Screen
    private boolean isAdd = true;

    private ActivityAddEditMedBinding binding;
    private MedicationPOJO medication;


   // private Repository repository;
   // private ConcreteLocalClass localClass;
   // private AddMedicationPresenterInterface presenterInterface;

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
    private String weight = "";
    private String start_date;
    private String end_date;
    private String strength = "";
    private String leftNumber = "";
    private String leftNumberReminder = "";
    private String reason = "";

    private TimeSelectedAdapter timeSelectedAdapter;

    private String email;

    //dialog
    MaterialAlertDialogBuilder dialogBuilder;

    public AddEditMedActivity() {
        // Required empty public constructor
    }


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddEditMedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Todo : add repo ,local , remote and select screen type (add,edit)
        if (false) {
            //get Med pojo
            isAdd = false;
            handleEditScreen();
        } else {
            medication = new MedicationPOJO();
            isAdd = true;
            handleAddScreen();
         }
        initRecycleView();
        handleSpinners();

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
        binding.tvTitle.setText("Edit");
        setSpinnerResult();
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
                /*
                setWorkTimer();
                Bundle bundle = new Bundle();
                bundle.putParcelable(DisplayMedicationDrug.displayTag, medication);
                Navigation.findNavController(v).navigate(R.id.action_fragment_add_Medication_to_displayMedicationDrug, bundle);
                */
                Toast.makeText(this, "Successfully Updated!", Toast.LENGTH_SHORT).show();

            }
        });
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
                /*
                setWorkTimer();
                Navigation.findNavController(v).navigate(R.id.action_fragment_add_Medication_to_fragment_home);
                */
                Toast.makeText(this, "Successfully Added!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void lunchExitDialog() {
        dialogBuilder = new MaterialAlertDialogBuilder(this);
        dialogBuilder.setTitle("Do you want to cancel ?")
                .setMessage("Your changes will not be save!")
                .setPositiveButton("Yes", (dialog, i) -> {
                    dialog.dismiss();
                    if(isAdd){
                        //navigate to home fragment
                    }else{
                        //navigate to display activity
                    }
                })
                .setNegativeButton("No", (dialog, i) -> {
                    dialog.dismiss();
                }).show();
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

    }

    @Override
    public void addMedication(MedicationPOJO medication) {

    }
    private void getEditableText() {
        medicationName = binding.etEditMedName.getEditableText().toString().trim();
        start_date = binding.tvSelectedStartDate.getText().toString().trim();
        end_date = binding.tvSelectedEndDate.getText().toString().trim();
        strength = binding.etEditStrengthDose.getEditableText().toString();
        leftNumber = binding.etEditLeft.getEditableText().toString();
        leftNumberReminder = binding.etRefillReminder.getEditableText().toString();
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
        if (!weight.isEmpty())
            medication.setWeight(Integer.parseInt(weight));
        medication.setMedicationReason(reason);
    }

    private void setSpinnerResultToPOJO() {
        medication.setMedicationType(medicationType);
        medication.setStrengthType(strengthType);
        medication.setTakeTimePerDay(dosePerDay);
        medication.setInstruction(instruction);
        medication.setTakeTimePerWeek(takeTimePerWeek);
    }


    private void setArraysAndMapsResultToPOJO() {

        //medication.setActive(!timeAndDoseAdapter.getTimeAndDose().isEmpty());
    }


    private void showDatePicker(TextView v, String s) {
        final Calendar myCalender = Calendar.getInstance();
        int year = myCalender.get(Calendar.YEAR);
        int month = myCalender.get(Calendar.MONTH);
        int day = myCalender.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener myDateListener = (view, year1, month1, day1) -> {
            if (view.isShown()) {
                month1 = month1 + 1;
                String date = day1 + "/" + month1 + "/" + year1;
                if (s.equals("start")) {
                    startDate = getDateMillis(date);
                } else {
                    endDate = getDateMillis(date);
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

    private Long getDateMillis(String date) {
        long milliseconds = -1;
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        try {
            Date d = f.parse(date);
            milliseconds = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds;
    }

    private String getDateString(Long date) {
        Date d = new Date(date);
        DateFormat f = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        return f.format(d);
    }

    private void initRecycleView() {
        int n = 0;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (medication.getTimeSimpleTaken() != null && medication.getTimeSimpleTaken().size() != 0) {
            n = medication.getDateTimeAbs().size();
        }
        timeSelectedAdapter = new TimeSelectedAdapter(n, this, medication.getTimeSimpleTaken());
        binding.recycleChooseTime.setLayoutManager(layoutManager);
        binding.recycleChooseTime.setAdapter(timeSelectedAdapter);
    }

    private void setDosePerTime(int n) {
        timeSelectedAdapter.setDosePerDay(n);
        timeSelectedAdapter.notifyDataSetChanged();
    }

    private void setSpinnerResult() {
        if (!medication.getMedicationType().isEmpty()) {
            binding.spEditMedType.setSelection(((ArrayAdapter) binding.spEditMedType.getAdapter())
                    .getPosition(medication.getMedicationType()));
        }

        if (!medication.getStrengthType().isEmpty()) {
            binding.spEditStrengthType.setSelection(((ArrayAdapter) binding.spEditStrengthType.getAdapter())
                    .getPosition(medication.getStrengthType()));
        }

        if (!medication.getTakeTimePerDay().isEmpty()) {
            binding.spOccurrDay.setSelection(((ArrayAdapter) binding.spOccurrDay.getAdapter())
                    .getPosition(medication.getTakeTimePerDay()));
        }

        if (!medication.getInstruction().isEmpty()) {
            binding.spEditMedEating.setSelection(((ArrayAdapter) binding.spEditMedEating.getAdapter())
                    .getPosition(medication.getInstruction()));
        }

        if (!medication.getTakeTimePerWeek().isEmpty()) {
            binding.spOccurrWeek.setSelection(((ArrayAdapter) binding.spOccurrWeek.getAdapter())
                    .getPosition(medication.getTakeTimePerWeek()));
        }
    }

    private void setEditText() {
        binding.etEditMedName.setText(medication.getMedicationName());
        binding.tvSelectedStartDate.setText(getDateString(medication.getStartDate()));
        startDate = medication.getStartDate();
        binding.tvSelectedEndDate.setText(getDateString(medication.getEndDate()));
        endDate = medication.getEndDate();
        binding.etEditStrengthDose.setText(medication.getStrength());
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
//        WorkManager.getInstance(this.getContext()).enqueue(periodicWorkRequest);
    }

*/


}