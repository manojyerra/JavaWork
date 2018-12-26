package rec.input;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class CalcLoanTime extends Activity implements View.OnClickListener
{
    EditText loanAmountEditTxt = null;
    EditText emiEditTxt = null;
    EditText interestEditTxt = null;

    Button calLoanAmountBtn = null;
    Button clearBtn = null;

    TextView loanTimeTitle = null;
    TextView loanTimeValue = null;


    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calcloantime);

        loanAmountEditTxt = (EditText)findViewById(R.id.lt_loanAmountEditText);
        emiEditTxt = (EditText)findViewById(R.id.lt_emiEditTxt);
        interestEditTxt = (EditText)findViewById(R.id.lt_interestEditTxt);

        calLoanAmountBtn = (Button)findViewById(R.id.lt_calLoanAmountBtn);
        calLoanAmountBtn.setOnClickListener(this);

        clearBtn = (Button)findViewById(R.id.lt_clearBtn);
        clearBtn.setOnClickListener(this);

        loanTimeTitle = (TextView)findViewById(R.id.lt_loanTimeTitle);
        loanTimeValue = (TextView)findViewById(R.id.lt_loanTimeValue);

        ClearAllText();
    }

    @Override public void onClick(View v)
    {
        if(v == calLoanAmountBtn) {
            String loanAmountStr = loanAmountEditTxt.getText().toString();
            String emiStr = emiEditTxt.getText().toString();
            String interestStr = interestEditTxt.getText().toString();

            if (emiStr == null || emiStr.trim().length() == 0) {
                Toast toast = Toast.makeText(this, "EMI is invalid", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                return;
            } else if (interestStr == null || interestStr.trim().length() == 0) {
                Toast toast = Toast.makeText(this, "Interest Rate is invalid", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                return;
            } else if (loanAmountStr == null || loanAmountStr.trim().length() == 0) {
                Toast toast = Toast.makeText(this, "Loan Amount is invalid", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                return;
            }

            double loanAmount = Integer.parseInt(loanAmountStr);
            double emi = Double.parseDouble(emiStr);
            double interestPercent = Double.parseDouble(interestStr);

            double tenPow7 = 10000000;

            if(loanAmount > tenPow7*tenPow7)
            {
                ShowToastMsg("Loan amount exceeded the limit", Toast.LENGTH_LONG);
                ClearAnswers();
                return;
            }

            if(interestPercent > 200)
            {
                ShowToastMsg("Interest percent should not be more than 200", Toast.LENGTH_LONG);
                ClearAnswers();
                return;
            }

            if(emi >= loanAmount)
            {
                ShowToastMsg("EMI should be less than Loan Amount", Toast.LENGTH_LONG);
                ClearAnswers();
                return;
            }

            long emiFor200Yrs = (long)EMIUtil.CalcEMI(loanAmount, interestPercent, 200*12) + 1;

            if(emi < emiFor200Yrs)
            {
                loanTimeTitle.setText("To calculate, EMI should be more than or equal to ₹ " + (emiFor200Yrs + 1));
                loanTimeValue.setText("");
                return;
            }

            double months = EMIUtil.CalcLoanPeriod(loanAmount, emi, interestPercent);

            if((long)months >= 1)
            {
                loanTimeTitle.setText("Time Period");
                loanTimeValue.setText(MonthsToYears((int)months)+" to "+MonthsToYears((int)months+1));
            }
            else
            {
                loanTimeTitle.setText("Error...");
                loanTimeValue.setText("");
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

    String MonthsToYears(int months)
    {
        if(months > 12)
        {
            int years = months/(int)12;
            int mon = (int)months - years*12;

            if(mon >= 1)
            {
                return (years+" years "+mon+" months");
            }
            else
            {
                return (years+" years");
            }
        }

        return months+" months";
    }

    void ClearAllText()
    {
        emiEditTxt.setText("");
        interestEditTxt.setText("");
        loanAmountEditTxt.setText("");

        ClearAnswers();
    }

    void ClearAnswers()
    {
        loanTimeTitle.setText("");
        loanTimeValue.setText("");
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

