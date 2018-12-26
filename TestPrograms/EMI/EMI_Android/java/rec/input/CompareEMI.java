package rec.input;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;
import android.view.Gravity;

public class CompareEMI extends Activity implements View.OnClickListener
{
    EditText loanAmountEditTxt_com1 = null;
    EditText interestEditTxt_com1 = null;
    EditText loanTimeEditTxt_com1 = null;
    Spinner monthOrYear_com1 = null;

    TextView emiTitle_com1 = null;
    TextView emiValue_com1 = null;

    TextView interestTitle_com1 = null;
    TextView interestValue_com1 = null;

    TextView totalPaymentTitle_com1 = null;
    TextView totalPaymentTitle2_com1 = null;
    TextView totalPaymentValue_com1 = null;


    EditText loanAmountEditTxt_com2 = null;
    EditText interestEditTxt_com2 = null;
    EditText loanTimeEditTxt_com2 = null;
    Spinner monthOrYear_com2 = null;

    TextView emiTitle_com2 = null;
    TextView emiValue_com2 = null;

    TextView interestTitle_com2 = null;
    TextView interestValue_com2 = null;

    TextView totalPaymentTitle_com2 = null;
    TextView totalPaymentTitle2_com2 = null;
    TextView totalPaymentValue_com2 = null;


    Button calEMIBtn_com = null;
    Button clearBtn_com = null;


    ScrollView scrollView = null;
    LinearLayout resultLayout = null;


    @Override protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compareemi);

        List<String> itemsList = new ArrayList<String>();
        itemsList.add("years");
        itemsList.add("months");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        loanAmountEditTxt_com1 = (EditText)findViewById(R.id.loanAmountEditText_com1);
        interestEditTxt_com1 = (EditText)findViewById(R.id.interestEditTxt_com1);
        loanTimeEditTxt_com1 = (EditText)findViewById(R.id.loanTenureEditText_com1);

        monthOrYear_com1 = (Spinner)findViewById(R.id.yearOrMonthSpinner_com1);
        monthOrYear_com1.setAdapter(dataAdapter);

        emiValue_com1 = (TextView)findViewById(R.id.emiValue_com1);
        emiValue_com1.setTextSize(35);

        emiTitle_com1 = (TextView)findViewById(R.id.emiTitle_com1);

        interestTitle_com1 = (TextView)findViewById(R.id.interestTitle_com1);
        interestValue_com1 = (TextView)findViewById(R.id.interestValue_com1);

        totalPaymentTitle_com1 = (TextView)findViewById(R.id.totalPaymentTitle_com1);
        totalPaymentTitle2_com1 = (TextView)findViewById(R.id.totalPaymentTitle2_com1);
        totalPaymentValue_com1 = (TextView)findViewById(R.id.totalPaymentValue_com1);


        //////////


        loanAmountEditTxt_com2 = (EditText)findViewById(R.id.loanAmountEditText_com2);
        interestEditTxt_com2 = (EditText)findViewById(R.id.interestEditTxt_com2);
        loanTimeEditTxt_com2 = (EditText)findViewById(R.id.loanTenureEditText_com2);

        monthOrYear_com2 = (Spinner)findViewById(R.id.yearOrMonthSpinner_com2);
        monthOrYear_com2.setAdapter(dataAdapter);

        emiValue_com2 = (TextView)findViewById(R.id.emiValue_com2);
        emiValue_com2.setTextSize(35);

        emiTitle_com2 = (TextView)findViewById(R.id.emiTitle_com2);

        interestTitle_com2 = (TextView)findViewById(R.id.interestTitle_com2);
        interestValue_com2 = (TextView)findViewById(R.id.interestValue_com2);

        totalPaymentTitle_com2 = (TextView)findViewById(R.id.totalPaymentTitle_com2);
        totalPaymentTitle2_com2 = (TextView)findViewById(R.id.totalPaymentTitle2_com2);
        totalPaymentValue_com2 = (TextView)findViewById(R.id.totalPaymentValue_com2);


        /////////

        calEMIBtn_com = (Button)findViewById(R.id.calBtn_com);
        calEMIBtn_com.setOnClickListener(this);
        clearBtn_com = (Button)findViewById(R.id.clearBtn_com);
        clearBtn_com.setOnClickListener(this);

        resultLayout = (LinearLayout)findViewById(R.id.resultLayout);
        resultLayout.setVisibility(View.GONE);

        scrollView = (ScrollView)findViewById(R.id.scrollView);
        scrollView.setSmoothScrollingEnabled(true);

        ClearAll();
    }

    @Override public void onClick(View v)
    {
        if(v == calEMIBtn_com)
        {
            if(CalcEMI1() == false)
                return;

            if(CalcEMI2() == false)
                return;
        }
        else if(v == clearBtn_com)
        {
            ClearAll();
        }
    }

    boolean CalcEMI1()
    {
        String loanAmountStr = loanAmountEditTxt_com1.getText().toString();
        String interestStr = interestEditTxt_com1.getText().toString();
        String loanTimeStr = loanTimeEditTxt_com1.getText().toString();

        if(loanAmountStr == null || loanAmountStr.trim().length() == 0)
        {
            ShowToastMsg("Loan Amount 1 is invalid", Toast.LENGTH_SHORT);
            ClearResult();
            return false;
        }
        else if(interestStr == null || interestStr.trim().length() == 0)
        {
            ShowToastMsg("Interest Rate 1 is invalid", Toast.LENGTH_SHORT);
            ClearResult();
            return false;
        }
        else if(loanTimeStr == null || loanTimeStr.trim().length() == 0)
        {
            ShowToastMsg("Loan Period 1 is invalid", Toast.LENGTH_SHORT);
            ClearResult();
            return false;
        }

        double loanAmount = Double.parseDouble(loanAmountStr);
        double interestPercent = Double.parseDouble(interestStr);
        int months = Integer.parseInt(loanTimeStr);

        if (monthOrYear_com1.getSelectedItemPosition() == 0)
            months = months * 12;

        double tempNum = 10000000;

        if(loanAmount > tempNum*tempNum)
        {
            ShowToastMsg("Loan Amount 1 exceeded the limit", Toast.LENGTH_LONG);
            ClearResult();
            return false;
        }

        if(interestPercent > 200)
        {
            ShowToastMsg("Interest Rate 1 should not be more than 200", Toast.LENGTH_LONG);
            ClearResult();
            return false;
        }

        if(months > 200*12)
        {
            ShowToastMsg("Loan Period 1 should not exceed 200 years", Toast.LENGTH_LONG);
            ClearResult();
            return false;
        }
        else if(months == 0)
        {
            ShowToastMsg("Loan Period 1 should not be 0", Toast.LENGTH_LONG);
            ClearResult();
            return false;
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
            resultLayout.setVisibility(View.VISIBLE);

            emiTitle_com1.setText("Loan EMI 1");
            emiValue_com1.setText("₹ " + numEmi);

            interestTitle_com1.setText("Total Interest Payable 1");
            interestValue_com1.setText("₹ " + numInterest);

            totalPaymentTitle_com1.setText("Total Payment 1");
            totalPaymentTitle2_com1.setText("(Principal + Interest)");
            totalPaymentValue_com1.setText("₹ " + (long)(loanAmount+numInterest));

            ScorllToEnd();
        }

        return true;
    }

    boolean CalcEMI2()
    {
        String loanAmountStr = loanAmountEditTxt_com2.getText().toString();
        String interestStr = interestEditTxt_com2.getText().toString();
        String loanTimeStr = loanTimeEditTxt_com2.getText().toString();

        if(loanAmountStr == null || loanAmountStr.trim().length() == 0)
        {
            ShowToastMsg("Loan Amount 2 is invalid", Toast.LENGTH_SHORT);
            ClearResult();
            return false;
        }
        else if(interestStr == null || interestStr.trim().length() == 0)
        {
            ShowToastMsg("Interest Rate 2 is invalid", Toast.LENGTH_SHORT);
            ClearResult();
            return false;
        }
        else if(loanTimeStr == null || loanTimeStr.trim().length() == 0)
        {
            ShowToastMsg("Loan Period 2 is invalid", Toast.LENGTH_SHORT);
            ClearResult();
            return false;
        }

        double loanAmount = Double.parseDouble(loanAmountStr);
        double interestPercent = Double.parseDouble(interestStr);
        int months = Integer.parseInt(loanTimeStr);

        if (monthOrYear_com2.getSelectedItemPosition() == 0)
            months = months * 12;

        double tempNum = 10000000;

        if(loanAmount > tempNum*tempNum)
        {
            ShowToastMsg("Loan Amount 2 exceeded the limit", Toast.LENGTH_LONG);
            ClearResult();
            return false;
        }

        if(interestPercent > 200)
        {
            ShowToastMsg("Interest Rate 2 should not be more than 200", Toast.LENGTH_LONG);
            ClearResult();
            return false;
        }

        if(months > 200*12)
        {
            ShowToastMsg("Loan Period 2 should not exceed 200 years", Toast.LENGTH_LONG);
            ClearResult();
            return false;
        }
        else if(months == 0)
        {
            ShowToastMsg("Loan Period 2 should not be 0", Toast.LENGTH_LONG);
            ClearResult();
            return false;
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
            resultLayout.setVisibility(View.VISIBLE);

            emiTitle_com2.setText("Loan EMI 2");
            emiValue_com2.setText("₹ " + numEmi);

            interestTitle_com2.setText("Total Interest Payable 2");
            interestValue_com2.setText("₹ " + numInterest);

            totalPaymentTitle_com2.setText("Total Payment 2");
            totalPaymentTitle2_com2.setText("(Principal + Interest)");
            totalPaymentValue_com2.setText("₹ " + (long) (loanAmount + numInterest));

            ScorllToEnd();
        }

        return true;
    }

    void ScorllToEnd()
    {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    void ClearAll()
    {
        loanAmountEditTxt_com1.setText("");
        interestEditTxt_com1.setText("");
        loanTimeEditTxt_com1.setText("");

        loanAmountEditTxt_com2.setText("");
        interestEditTxt_com2.setText("");
        loanTimeEditTxt_com2.setText("");

        ClearResult();
    }

    void ClearResult()
    {
        resultLayout.setVisibility(View.GONE);
    }

    void ShowToastMsg(String msg, int length)
    {
        Toast toast = Toast.makeText(this, msg, length);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
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
