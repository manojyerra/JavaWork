package rec.input;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;
import android.view.Gravity;

public class CalcEMI extends Activity implements View.OnClickListener
{
    EditText loanAmountEditTxt = null;
    EditText interestEditTxt = null;
    EditText loanTimeEditTxt = null;
    Spinner monthOrYear = null;

    Button calEMIBtn = null;
    Button clearBtn = null;

    TextView emiValueTitle = null;
    TextView emiValue = null;

    TextView interestTitle = null;
    TextView interestValue = null;

    TextView totalPaymentTitle = null;
    TextView totalPaymentTitle2 = null;
    TextView totalPaymentValue = null;


    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calcemi);

        loanAmountEditTxt = (EditText)findViewById(R.id.loanAmountEditText);
        interestEditTxt = (EditText)findViewById(R.id.interestEditTxt);
        loanTimeEditTxt = (EditText)findViewById(R.id.loanTenureEditText);

        calEMIBtn = (Button)findViewById(R.id.calEMIBtn);
        calEMIBtn.setOnClickListener(this);
        clearBtn = (Button)findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(this);

        emiValue = (TextView)findViewById(R.id.emiValue);
        emiValueTitle = (TextView)findViewById(R.id.loanEMITitle);

        emiValue.setTextSize(35);

        interestTitle = (TextView)findViewById(R.id.interestTitle);
        interestValue = (TextView)findViewById(R.id.interestValue);

        totalPaymentTitle = (TextView)findViewById(R.id.totalPaymentTitle);
        totalPaymentTitle2 = (TextView)findViewById(R.id.totalPaymentTitle2);
        totalPaymentValue = (TextView)findViewById(R.id.totalPaymentValue);


        List<String> itemsList = new ArrayList<String>();
        itemsList.add("years");
        itemsList.add("months");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        monthOrYear = (Spinner)findViewById(R.id.yearOrMonthSpinner);
        monthOrYear.setAdapter(dataAdapter);

        ClearAllText();
    }

    @Override public void onClick(View v)
    {
        if(v == calEMIBtn)
        {
            String loanAmountStr = loanAmountEditTxt.getText().toString();
            String interestStr = interestEditTxt.getText().toString();
            String loanTimeStr = loanTimeEditTxt.getText().toString();

            if(loanAmountStr == null || loanAmountStr.trim().length() == 0)
            {
                ShowToastMsg("Loan Amount is invalid", Toast.LENGTH_SHORT);
                ClearAnswers();
                return;
            }
            else if(interestStr == null || interestStr.trim().length() == 0)
            {
                ShowToastMsg("Interest Rate is invalid", Toast.LENGTH_SHORT);
                ClearAnswers();
                return;
            }
            else if(loanTimeStr == null || loanTimeStr.trim().length() == 0)
            {
                ShowToastMsg("Loan Period is invalid", Toast.LENGTH_SHORT);
                ClearAnswers();
                return;
            }

            double loanAmount = Double.parseDouble(loanAmountStr);
            double interestPercent = Double.parseDouble(interestStr);
            int months = Integer.parseInt(loanTimeStr);

            if (monthOrYear.getSelectedItemPosition() == 0)
                months = months * 12;

            double tempNum = 10000000;

            if(loanAmount > tempNum*tempNum)
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

            double emi = EMIUtil.CalcEMI(loanAmount, interestPercent, months);
            double totInterest = EMIUtil.CalcTotInterest(loanAmount, interestPercent, months);

            long numEmi = (long) (emi + 0.5);
            long numInterest = (long) (totInterest + 0.5);

            long emi_months = (long)(emi*months + 0.5);
            long loan_interest = (long)((loanAmount+totInterest) + 0.5);

            System.out.println("\n EMI : "+emi+", totInterest : "+totInterest);

            System.out.println("\n EMI_MONTHS : "+emi_months+", LOAN_INTEREST : "+loan_interest);

            if( emi_months == loan_interest)
            {
                emiValueTitle.setText("Loan EMI");
                emiValue.setText("₹ " + numEmi);

                interestTitle.setText("Total Interest Payable");
                interestValue.setText("₹ " + numInterest);

                totalPaymentTitle.setText("Total Payment");
                totalPaymentTitle2.setText("(Principal + Interest)");
                totalPaymentValue.setText("₹ " + (long)(loanAmount+numInterest));
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
        loanAmountEditTxt.setText("");
        interestEditTxt.setText("");
        loanTimeEditTxt.setText("");

        ClearAnswers();
    }

    void ClearAnswers()
    {
        emiValueTitle.setText("");
        emiValue.setText("");

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



/*
    public static double CalcLoanPeriod(double loanAmount, double emi, double interestRate)
    {
        double firstMonthInterest = loanAmount * interestRate / 1200;

        if(emi <= firstMonthInterest)
        {
            return -1.0;
        }

        double deduct = emi - firstMonthInterest;

        double maxMonths = loanAmount / deduct;
        double minMonths = 0;
        double currMonths = (minMonths + maxMonths) / 2;

        while(true)
        {
            double emiChk = loanAmount;

            if((int)currMonths > 0)
                emiChk = CalcEMI(loanAmount, interestRate, (int)currMonths);

            double emiChkNextMonth = CalcEMI(loanAmount, interestRate, (int)currMonths + 1);

            if(emi >= emiChkNextMonth && emi <= emiChk)
            {
                break;
            }

            if(emiChk > emi)
            {
                System.out.println("emiChk > emi "+emiChk+" > "+emi+" currMonths"+currMonths);

                minMonths = currMonths;
                currMonths = (minMonths + maxMonths) / 2;
            }
            else if(emiChk < emi)
            {
                System.out.println("emiChk < emi "+emiChk+" < "+emi+" currMonths"+currMonths);

                maxMonths = currMonths;
                currMonths = (minMonths + maxMonths) / 2;
            }
        }

        return currMonths;
    }
 */