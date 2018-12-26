package rec.input;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class EntryPoint extends Activity  implements View.OnClickListener
{
	Button calEMIBtn = null;
	Button calcLoanAmount = null;
	Button calcLoanTime = null;
	Button compareEMI = null;


	@Override protected void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		calEMIBtn = (Button)findViewById(R.id.emiMenuBtn);
		calcLoanAmount = (Button)findViewById(R.id.calcLoanAmount);
		calcLoanTime = (Button)findViewById(R.id.calcLoanTime);
		compareEMI = (Button)findViewById(R.id.compareEMI);

		calEMIBtn.setOnClickListener(this);
		calcLoanAmount.setOnClickListener(this);
		calcLoanTime.setOnClickListener(this);
		compareEMI.setOnClickListener(this);
 	}

	@Override public void onClick(View v)
	{
		if (v == calEMIBtn)
		{
			Intent i = new Intent(this, rec.input.CalcEMI.class);
			startActivity(i);
		}
		else if (v == calcLoanAmount)
		{
			Intent i = new Intent(this, rec.input.CalcLoanAmount.class);
			startActivity(i);
		}
		else if (v == calcLoanTime)
		{
			Intent i = new Intent(this, rec.input.CalcLoanTime.class);
			startActivity(i);
		}
		else if (v == compareEMI)
		{
			Intent i = new Intent(this, rec.input.CompareEMI.class);
			startActivity(i);
		}
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
    }
    
    @Override public void onDestroy()
    {
    	super.onDestroy();
    }
}


/*

            <LinearLayout
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="0.5"
                android:orientation="vertical"
                android:layout_marginLeft="5dp"
                android:layout_marginRight="5dp"
                android:background="#eeeeee">

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:gravity="center"
                    android:textAppearance="?android:attr/textAppearanceLarge"
                    android:text="Loan Amount"
                    android:id="@+id/loanTitle_com2"
                    android:layout_marginBottom="10dp"
                    android:textColor="#009688" />

                <EditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:inputType="number"
                    android:ems="10"
                    android:id="@+id/loanAmountEditText_com2" />

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:gravity="center"
                    android:textAppearance="?android:attr/textAppearanceLarge"
                    android:text="Interest"
                    android:id="@+id/interestTextView_com2"
                    android:layout_marginTop="20dp"
                    android:layout_marginBottom="10dp"
                    android:textColor="#009688" />

                <EditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:inputType="number|numberDecimal"
                    android:ems="10"
                    android:id="@+id/interestEditTxt_com2"
                    android:layout_below="@+id/interestTextView_com1"/>

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:textAppearance="?android:attr/textAppearanceLarge"
                    android:text="Loan Tenure"
                    android:gravity="center"
                    android:id="@+id/la_loanTenureTextView_com2"
                    android:textColor="#009688"
                    android:layout_marginTop="20dp"
                    android:layout_marginBottom="10dp"/>

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="50dp"
                    android:layout_marginTop="5dp"
                    android:layout_marginBottom="5dp"
                    android:orientation="horizontal">

                    <EditText
                        android:layout_width="0dp"
                        android:layout_height="match_parent"
                        android:layout_weight=".45"
                        android:id="@+id/loanTenureEditText_com2"
                        android:inputType="number"/>

                    <Spinner
                        android:layout_width="0dp"
                        android:layout_height="match_parent"
                        android:layout_weight=".55"
                        android:id="@+id/yearOrMonthSpinner_com2"
                        android:layout_marginBottom="2dp"/>

                </LinearLayout>

                <Button
                    android:layout_width="match_parent"
                    android:layout_height="75dp"
                    android:text="Calculate EMI"
                    android:id="@+id/calBtn_com2"
                    android:textColor="#ffffff"
                    android:backgroundTint="#009688"
                    android:textSize="20dp"
                    android:layout_marginTop="10dp"
                    />

                <Button
                    android:layout_width="match_parent"
                    android:layout_height="70dp"
                    android:text="Clear"
                    android:id="@+id/clearBtn_com2"
                    android:textColor="#ffffff"
                    android:backgroundTint="#009688"
                    android:textSize="20dp"
                    android:layout_marginTop="10dp"/>

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:gravity="center"
                    android:textAppearance="?android:attr/textAppearanceLarge"
                    android:text="Loan EMI"
                    android:id="@+id/emiTitle_com2"
                    android:textColor="#000000"
                    android:layout_marginTop="10dp"
                    android:layout_marginBottom="10dp"/>

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginBottom="20dp"
                    android:gravity="center"
                    android:textAppearance="?android:attr/textAppearanceLarge"
                    android:id="@+id/emiValue_com2"
                    android:textColor="#000000"/>

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="25dp"
                    android:layout_marginBottom="10dp"
                    android:gravity="center"
                    android:textAppearance="?android:attr/textAppearanceLarge"
                    android:text="Total Interest Payable"
                    android:id="@+id/interestTitle_com2"
                    android:textColor="#000000"/>

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginBottom="20dp"
                    android:gravity="center"
                    android:textAppearance="?android:attr/textAppearanceLarge"
                    android:id="@+id/interestValue_com2"
                    android:textColor="#000000"/>

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="25dp"
                    android:layout_marginBottom="5dp"
                    android:gravity="center"
                    android:textAppearance="?android:attr/textAppearanceLarge"
                    android:text="Total Payment"
                    android:id="@+id/totalPaymentTitle_com2"
                    android:textColor="#000000"/>

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:gravity="center"
                    android:textAppearance="?android:attr/textAppearanceLarge"
                    android:text="(Principal + Interest)"
                    android:id="@+id/totalPaymentTitle2_com2"
                    android:textColor="#000000"/>

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="15dp"
                    android:layout_marginBottom="30dp"
                    android:gravity="center"
                    android:textAppearance="?android:attr/textAppearanceLarge"
                    android:id="@+id/totalPaymentValue_com2"
                    android:textColor="#000000"/>

            </LinearLayout>

        </LinearLayout>

    </ScrollView>

</LinearLayout>
 */