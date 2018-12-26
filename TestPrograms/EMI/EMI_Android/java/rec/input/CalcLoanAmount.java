package rec.input;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;


public class CalcLoanAmount extends Activity implements View.OnClickListener
{
    EditText emiEditTxt = null;
    EditText interestEditTxt = null;
    EditText loanTimeEditTxt = null;
    Spinner monthOrYear = null;

    Button calLoanAmountBtn = null;
    Button clearBtn = null;

    TextView loanAmountValueTitle = null;
    TextView loanAmountValue = null;

    TextView interestTitle = null;
    TextView interestValue = null;

    TextView totalPaymentTitle = null;
    TextView totalPaymentTitle2 = null;
    TextView totalPaymentValue = null;


    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calcloanamount);

        emiEditTxt = (EditText)findViewById(R.id.la_emiAmountEditText);
        interestEditTxt = (EditText)findViewById(R.id.la_interestEditTxt);
        loanTimeEditTxt = (EditText)findViewById(R.id.la_loanTenureEditText);

        calLoanAmountBtn = (Button)findViewById(R.id.la_calLoanAmountBtn);
        calLoanAmountBtn.setOnClickListener(this);
        clearBtn = (Button)findViewById(R.id.la_clearBtn);
        clearBtn.setOnClickListener(this);

        loanAmountValue = (TextView)findViewById(R.id.la_loanAmountValue);
        loanAmountValue.setTextSize(35);
        loanAmountValueTitle = (TextView)findViewById(R.id.la_loanAmountTitle);

        interestTitle = (TextView)findViewById(R.id.la_interestTitle);
        interestValue = (TextView)findViewById(R.id.la_interestValue);

        totalPaymentTitle = (TextView)findViewById(R.id.la_totalPaymentTitle);
        totalPaymentTitle2 = (TextView)findViewById(R.id.la_totalPaymentTitle2);
        totalPaymentValue = (TextView)findViewById(R.id.la_totalPaymentValue);


        List<String> itemsList = new ArrayList<String>();
        itemsList.add("years");
        itemsList.add("months");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        monthOrYear = (Spinner)findViewById(R.id.la_yearOrMonthSpinner);
        monthOrYear.setAdapter(dataAdapter);

        ClearAllText();
    }

    @Override public void onClick(View v)
    {
        if(v == calLoanAmountBtn)
        {
            String emiStr = emiEditTxt.getText().toString();
            String interestStr = interestEditTxt.getText().toString();
            String loanTimeStr = loanTimeEditTxt.getText().toString();

            if(emiStr == null || emiStr.trim().length() == 0)
            {
                Toast toast = Toast.makeText(this, "EMI is invalid", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                ClearAnswers();
                return;
            }
            else if(interestStr == null || interestStr.trim().length() == 0)
            {
                Toast toast = Toast.makeText(this, "Interest Rate is invalid", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                ClearAnswers();
                return;
            }
            else if(loanTimeStr == null || loanTimeStr.trim().length() == 0)
            {
                Toast toast = Toast.makeText(this, "Loan Period is invalid", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                ClearAnswers();
                return;
            }

            double emi = Double.parseDouble(emiStr);
            double interestPercent = Double.parseDouble(interestStr);
            int months = Integer.parseInt(loanTimeStr);

            if (monthOrYear.getSelectedItemPosition() == 0)
                months = months * 12;

            if(emi > 16666666666667.0)
            {
                ShowToastMsg("EMI exceeded the limit", Toast.LENGTH_LONG);
                ClearAnswers();
                return;
            }

            if(interestPercent > 200)
            {
                ShowToastMsg("Interest percent should not be more than 200", Toast.LENGTH_LONG);
                ClearAnswers();
                return;
            }

            if(months > 200*12)
            {
                ShowToastMsg("Loan period should not exceed 200 years", Toast.LENGTH_LONG);
                ClearAnswers();
                return;
            }
            else if(months == 0)
            {
                ShowToastMsg("Loan period should not be 0", Toast.LENGTH_LONG);
                ClearAnswers();
                return;
            }


            double loanAmount = EMIUtil.CalcLoanAmount(emi, interestPercent, months);
            double totInterest = EMIUtil.CalcTotInterest(loanAmount, interestPercent, months);

            long numLoanAmount = (long) (loanAmount + 0.5);
            long numInterest = (long) (totInterest + 0.5);

            long emi_months = (long)(emi*months + 0.5);
            long loan_interest = (long)((loanAmount+totInterest) + 0.5);

            if(emi_months  == loan_interest)
            {
                loanAmountValueTitle.setText("Loan Amount");
                loanAmountValue.setText("₹ " + numLoanAmount);

                interestTitle.setText("Total Interest Payable");
                interestValue.setText("₹ " + numInterest);

                totalPaymentTitle.setText("Total Payment");
                totalPaymentTitle2.setText("(Principal + Interest)");
                totalPaymentValue.setText("₹ " + (long) (loanAmount + numInterest));
            }
            else
            {
                System.out.println("EMI_MONTHS : "+(emi*months)+" , LOAN_INTEREST "+(loanAmount+totInterest));
                ClearAnswers();
            }

        }
        else if(v == clearBtn)
        {
            ClearAllText();
        }
    }

    void ShowToastMsg(String msg, int length)
    {
        Toast toast = Toast.makeText(this, msg, length);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    void ClearAllText()
    {
        emiEditTxt.setText("");
        interestEditTxt.setText("");
        loanTimeEditTxt.setText("");

        ClearAnswers();
    }

    void ClearAnswers()
    {
        loanAmountValueTitle.setText("");
        loanAmountValue.setText("");

        interestTitle.setText("");
        interestValue.setText("");

        totalPaymentTitle.setText("");
        totalPaymentTitle2.setText("");
        totalPaymentValue.setText("");
    }

    @Override protected void onStart()
    {
        super.onStart();
    }

    @Override protected void onStop()
    {
        super.onStop();
    }

    @Override protected void onActivityResult(int request, int response, Intent data)
    {
        super.onActivityResult(request, response, data);
    }

    @Override protected void onPause()
    {
        super.onPause();
    }

    @Override protected void onResume()
    {
        super.onResume();
    }

    @Override public void onBackPressed()
    {
        finish();
    }

    @Override public void onDestroy()
    {
        super.onDestroy();
    }
}

