package com.wambuacooperations.userinputs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText birthdayEditText,firstNameEditText,lastNameEditText,emailEditText,passwordEditText,confirmPasswordEditText;
    Button registerButton;
    CheckBox acceptTermsCheckbox;
    Calendar myCalendar=Calendar.getInstance();
    Spinner phoneNumberSpinner;
    String mSpinnerLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        birthdayEditText=findViewById(R.id.editTextDateOfBirth);
        phoneNumberSpinner=findViewById(R.id.phoneNumbersSpinner);
        firstNameEditText=findViewById(R.id.editTextFirstName);
        lastNameEditText=findViewById(R.id.editTextLastName);
        emailEditText=findViewById(R.id.editTextEmail);
        passwordEditText=findViewById(R.id.editTextPassword);
        confirmPasswordEditText=findViewById(R.id.editTextConfirmPassword);
        registerButton=findViewById(R.id.registerButton);
        acceptTermsCheckbox=findViewById(R.id.acceptTermsCheckBox);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_label, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(phoneNumberSpinner!=null){
            phoneNumberSpinner.setAdapter(adapter);
        }

    }


    DatePickerDialog.OnDateSetListener date=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR,year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updateLabel();
        }
    };

    public void selectBirthday(View view){
        new DatePickerDialog(this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void updateLabel(){
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        birthdayEditText.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSpinnerLabel= (String) parent.getItemAtPosition(position);
        Toast.makeText(this, "Set phone Number as: "+mSpinnerLabel, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Nothing selected!", Toast.LENGTH_SHORT).show();
    }

    public void registerClicked(View view){
        if(firstNameEditText.getText().toString().isEmpty()||lastNameEditText.getText().toString().isEmpty()||birthdayEditText.getText().toString().isEmpty()||passwordEditText.getText().toString().isEmpty()){
            new AlertDialog.Builder(this)
                    .setMessage("Please fill all the fields!")
                    .show();
        }else if(!(passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString()))){
            new AlertDialog.Builder(this)
                    .setMessage("Passwords do not match!")
                    .show();
        }else if(!(acceptTermsCheckbox.isChecked())){
            Toast.makeText(this, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Registered Successfully! :)", Toast.LENGTH_SHORT).show();
        }
    }
}
