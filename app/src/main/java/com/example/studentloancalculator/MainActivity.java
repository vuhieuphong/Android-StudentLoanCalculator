package com.example.studentloancalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] years = { "1 year", "2 years", "3 years", "4 years", "5 years" };

    EditText editTextLoanAmount;
    Spinner spinnerLoanDuration;
    RadioButton radioInterest1;
    RadioButton radioInterest2;
    RadioButton radioInterest3;
    Button buttonCalculate;
    TextView textViewResult;

    Button buttonShowDatePicker;
    TextView textViewStartDate;

    int loanDuration;
    Calendar calendar = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextLoanAmount=(EditText)findViewById(R.id.editTextLoanAmount);
        spinnerLoanDuration=(Spinner)findViewById(R.id.spinnerLoanDuration);
        radioInterest1=(RadioButton)findViewById(R.id.radioInterest1);
        radioInterest2=(RadioButton)findViewById(R.id.radioInterest2);
        radioInterest3=(RadioButton)findViewById(R.id.radioInterest3);
        buttonCalculate=(Button)findViewById(R.id.buttonCalculate);
        textViewResult=(TextView)findViewById(R.id.textViewResult);

        buttonShowDatePicker=(Button)findViewById(R.id.buttonShowDatePicker);
        textViewStartDate=(TextView)findViewById(R.id.textViewStartDate);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoanDuration.setAdapter(adapter);
        spinnerLoanDuration.setOnItemSelectedListener(this);

        updateTextViewStartDate();
    }

    public void buttonCalculateClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Calculate Loan?")
            .setMessage("Do you want to calculate your loan amount?")
            .setCancelable(false)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                            double loanAmount=Double.parseDouble(String.valueOf(editTextLoanAmount.getText()));
                            double interest=(radioInterest1.isChecked())? 0.02
                                            :(radioInterest2.isChecked())? 0.025
                                            :(radioInterest3.isChecked())? 0.03
                                            :null;
                            double result=loanAmount*Math.pow(1.0+interest/1200,loanDuration*12);
                            calendar.add(Calendar.YEAR, loanDuration);
                            Date endDate=calendar.getTime();
                            textViewResult.setText(new DecimalFormat("$#.##").format(result)+" by "+dateFormat.format(endDate));
                            calendar.add(Calendar.YEAR, -loanDuration);

                            Toast.makeText(getApplicationContext(), "Loan Amount: "+loanAmount+"\nInterest: "+interest, Toast.LENGTH_SHORT).show();
                        }
                        catch(Exception e){
                            textViewResult.setText("");
                            AlertDialog.Builder errorDialog=new AlertDialog.Builder(MainActivity.this);
                            errorDialog.setTitle("Exeption Caught")
                                    .setMessage(e.getMessage())
                                    .setPositiveButton("OK", null)
                                    .setCancelable(true);
                            errorDialog.show();
                        }
                    }
                })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textViewResult.setText("");
                    }
                });
        builder.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        loanDuration=Integer.parseInt(String.valueOf(years[position].substring(0,1)));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateTextViewStartDate();
        }
    };

    public void buttonShowDatePickerClicked(View view) {
        new DatePickerDialog(this, date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateTextViewStartDate() {
        textViewStartDate.setText(dateFormat.format(calendar.getTime())+"");

    }
}
