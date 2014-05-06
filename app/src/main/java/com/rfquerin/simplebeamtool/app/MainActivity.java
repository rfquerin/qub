package com.rfquerin.simplebeamtool.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.text.DecimalFormat;

import static java.lang.StrictMath.pow;





public class MainActivity extends Activity {

    public boolean isMetric = true;
    public float deadfactor = (float)1.25;
    public float livefactor = (float)1.50;
    public float YoungsMetric = (float)200000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // read preferences file and extract load factors and youngs modulus value

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String dfactorstring = sharedPref.getString("dead_factor","1.25");
        String lfactorstring = sharedPref.getString("live_factor","1.50");
        String Ystring = sharedPref.getString("youngs_modulus","200000");

        deadfactor = Float.valueOf(dfactorstring);
        livefactor = Float.valueOf(lfactorstring);
        YoungsMetric = Float.valueOf(Ystring);




        // arbitrarily hit refresh to update values on creation

        Button refreshbutton = (Button)findViewById(R.id.buttonRefresh);
        refreshbutton.performClick();

		// refresh units to handle device
		// orientation change
		
		// put  some code here to do it

        // determine whether metric or imperial

        RadioButton unitsSI = (RadioButton)findViewById(R.id.radioButtonSI);

        if (unitsSI.isChecked()) {

            setUnitsMetric();

        }
        else
        {

            setUnitsImperial();
        }

		
		
		
    }

    @Override
    protected void onResume() {
        super.onResume();

        // refresh units to handle device
        // orientation change

        // put  some code here to do it

        // determine whether metric or imperial

        RadioButton unitsSI = (RadioButton)findViewById(R.id.radioButtonSI);

        if (unitsSI.isChecked()) {

            setUnitsMetric();

        }
        else
        {

            setUnitsImperial();
        }

        // arbitrarily hit refresh to update values on creation

        Button refreshbutton = (Button)findViewById(R.id.buttonRefresh);
        refreshbutton.performClick();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                //call settings menu up;
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void radiobuttonClick(View v) {

        // handle radio button click


        TextView spanunits = (TextView)findViewById(R.id.textViewSpanUnits);
        TextView tribunits = (TextView)findViewById(R.id.textViewTribUnits);
        TextView deadunits = (TextView)findViewById(R.id.textViewDeadUnits);
        TextView liveunits = (TextView)findViewById(R.id.textViewLiveUnits);
        TextView deflunits = (TextView)findViewById(R.id.textViewDeflnUnits);
        TextView momentunits = (TextView)findViewById(R.id.textViewMomentUnits);
        TextView reactionunits = (TextView)findViewById(R.id.textViewReactionUnits);
        TextView inertiaunits1 = (TextView)findViewById(R.id.textViewIxUnits);

        EditText spantext = (EditText)findViewById(R.id.editTextSpan);
        EditText tribtext = (EditText)findViewById(R.id.editTextTrib);
        EditText deadtext = (EditText)findViewById(R.id.editTextDead);
        EditText livetext = (EditText)findViewById(R.id.editTextLive);
        TextView deflntext = (TextView)findViewById(R.id.textViewDefln);
        EditText deflnratiotext = (EditText)findViewById(R.id.editTextDeflnRatio);

        TextView momenttext = (TextView)findViewById(R.id.textViewMoment);
        TextView reactiontext = (TextView)findViewById(R.id.textViewReaction);
        TextView inertiatext = (TextView)findViewById(R.id.textViewIx);





        // set formatting for values

        DecimalFormat df3 = new DecimalFormat("0.000");
        DecimalFormat df2 = new DecimalFormat("0.00");
        DecimalFormat df1 = new DecimalFormat("0.0");
        DecimalFormat df_IxSI = new DecimalFormat("##0.###E0");


        // Is the button now checked?
        boolean checked = ((RadioButton) v).isChecked();



        // Check which radio button was clicked
        switch(v.getId()) {
            case R.id.radioButtonSI:
                if (checked)

                    if (!isMetric) // if current state was imperial
                    {
                        // change EditText values to metric

                        float spanimp = Float.valueOf(spantext.getText().toString());
                        float spanmetric = spanimp * (float)0.3048;

                        float tribimp = Float.valueOf(tribtext.getText().toString());
                        float tribmetric = tribimp * (float)0.3048;

                        float deadimp = Float.valueOf(deadtext.getText().toString());
                        float deadmetric = deadimp / (float)20.88;

                        float liveimp = Float.valueOf(livetext.getText().toString());
                        float livemetric = liveimp / (float)20.88;

                        float deflnratio = Float.valueOf(deflnratiotext.getText().toString());

                        float deflnmetric = spanmetric * (float)1000.0 / deflnratio;



                        float momentmetric = calcMomentMetric(spanmetric, tribmetric, deadmetric, livemetric);
                        float reactionmetric = calcReactionMetric(spanmetric, tribmetric, deadmetric, livemetric);
                        float inertiametric = calcInertiaMetric(spanmetric, tribmetric, livemetric, deflnratio);







                        // format values into strings



                        String formattedspan = df3.format(spanmetric);
                        String formattedtrib = df3.format(tribmetric);
                        String formatteddead = df2.format(deadmetric);
                        String formattedlive = df2.format(livemetric);
                        String formatteddefln = df1.format(deflnmetric);
                        String formattedmoment = df1.format(momentmetric);
                        String formattedreaction = df1.format(reactionmetric);
                        String formattedinertia = df_IxSI.format(inertiametric);







                        // put formatted values into appropriate edittext widgets


                        spantext.setText(formattedspan);
                        tribtext.setText(formattedtrib);
                        deadtext.setText(formatteddead);
                        livetext.setText(formattedlive);
                        deflntext.setText(formatteddefln);
                        momenttext.setText(formattedmoment);
                        reactiontext.setText(formattedreaction);
                        inertiatext.setText(formattedinertia);



                        // set all textviews with units to be metric ones

                        setUnitsMetric();




                        isMetric = true;

                    }






                    break;
            case R.id.radioButtonIMP:
                if (checked)

                    // deflunits.setText("IMP"); -- test case

                    // set all textviews with units to be imperial ones

                   // spanunits.setText(R.string.span_units_imp);

                    spanunits.setText(R.string.span_units_imp);
                    tribunits.setText(R.string.tribwidth_units_imp);
                    deadunits.setText(R.string.deadUDL_units_imp);
                    liveunits.setText(R.string.liveUDL_units_imp);
                    deflunits.setText(R.string.defln_units_imp);
                    momentunits.setText(R.string.Mf_units_imp);
                    reactionunits.setText(R.string.Rf_units_imp);
                    inertiaunits1.setText(R.string.Ix_units1_imp);



                if (isMetric) // if current state was metric
                {
                    // change EditText values to imperial

                    float spanmetric = Float.valueOf(spantext.getText().toString());
                    float spanimp = spanmetric / (float)0.3048;

                    float tribmetric = Float.valueOf(tribtext.getText().toString());
                    float tribimp = tribmetric / (float)0.3048;

                    float deadmetric = Float.valueOf(deadtext.getText().toString());
                    float deadimp = deadmetric * (float)20.88;

                    float livemetric = Float.valueOf(livetext.getText().toString());
                    float liveimp = livemetric * (float)20.88;

                    float deflnratio = Float.valueOf(deflnratiotext.getText().toString());

                    float deflnimp = spanimp * (float)12.0 / deflnratio;



                    float momentimp = calcMomentImperial(spanimp, tribimp, deadimp, liveimp);
                    float reactionimp = calcReactionImperial(spanimp, tribimp, deadimp, liveimp);
                    float inertiaimp = calcInertiaImperial(spanimp, tribimp, liveimp, deflnratio);


                    // format values into strings



                    String formattedspan = df3.format(spanimp);
                    String formattedtrib = df3.format(tribimp);
                    String formatteddead = df1.format(deadimp);
                    String formattedlive = df1.format(liveimp);
                    String formatteddefln = df1.format(deflnimp);
                    String formattedmoment = df1.format(momentimp);
                    String formattedreaction = df1.format(reactionimp);
                    String formattedinertia = df1.format(inertiaimp);






                    // put formatted values into appropriate edittext widgets


                    spantext.setText(formattedspan);
                    tribtext.setText(formattedtrib);
                    deadtext.setText(formatteddead);
                    livetext.setText(formattedlive);
                    deflntext.setText(formatteddefln);

                    momenttext.setText(formattedmoment);
                    reactiontext.setText(formattedreaction);
                    inertiatext.setText(formattedinertia);

                    isMetric = false;


                }



                break;
        }
    }


    public void refreshbuttonOnClick(View v) {
    // take the input values and compute the output.

        // get the value from the span input field
        TextView spanInput = (TextView)findViewById(R.id.editTextSpan);
        float span = Float.valueOf(spanInput.getText().toString());

        // get the value from the dead load input field
        TextView deadInput = (TextView)findViewById(R.id.editTextDead);
        float deadload = Float.valueOf(deadInput.getText().toString());

        // get the value from the live load input field
        TextView liveInput = (TextView)findViewById(R.id.editTextLive);
        float liveload = Float.valueOf(liveInput.getText().toString());

        // get the value from the tributary width input field
        TextView tribInput = (TextView)findViewById(R.id.editTextTrib);
        float tribwidth = Float.valueOf(tribInput.getText().toString());

        // get the value from the deflection limit input field
        TextView deflnInput = (TextView)findViewById(R.id.editTextDeflnRatio);
        float deflnRatio = Float.valueOf(deflnInput.getText().toString());


        // handle moment, reaction and inertia calculations


        if (isMetric) {


            // compute bending moment

            float moment = calcMomentMetric(span,tribwidth,deadload,liveload);


            // compute end reaction

            float reaction = calcReactionMetric(span,tribwidth,deadload,liveload);


            // compute required moment of inertia

            float inertia = calcInertiaMetric(span,tribwidth,liveload,deflnRatio);

            float defln_measurement = span * (float)1000.0 / deflnRatio;



            // format moment and reaction values into strings with only 1 decimal place

            DecimalFormat df = new DecimalFormat("0.0");
            DecimalFormat df_Ix = new DecimalFormat("##0.###E0");

            String formattedmoment = df.format(moment);
            String formattedreaction = df.format(reaction);
            String formattedinertia = df_Ix.format(inertia);
            String formatteddeflection = df.format(defln_measurement);

            // put moment, reaction, and inertia values into appropriate textview widgets

            TextView momentField = (TextView) findViewById(R.id.textViewMoment);


            TextView reactionField = (TextView) findViewById(R.id.textViewReaction);


            TextView inertiaField = (TextView) findViewById(R.id.textViewIx);


            TextView deflectionField = (TextView) findViewById(R.id.textViewDefln);


            momentField.setText(formattedmoment);
            reactionField.setText(formattedreaction);
            inertiaField.setText(formattedinertia);
            deflectionField.setText(formatteddeflection);
        }
        else
        {

            // carry out calculations for imperial

            // compute bending moment

            float moment = calcMomentImperial(span,tribwidth,deadload,liveload);



            // compute end reaction

            float reaction = calcReactionImperial(span,tribwidth,deadload,liveload);


            // compute required moment of inertia

            float inertia = calcInertiaImperial(span, tribwidth,liveload,deflnRatio);

            float defln_measurement = span * (float) 12.0 / deflnRatio;


            // format moment and reaction values into strings with only 1 decimal place

            DecimalFormat df = new DecimalFormat("0.0");

            String formattedmoment = df.format(moment);
            String formattedreaction = df.format(reaction);
            String formattedinertia = df.format(inertia);
            String formatteddeflection = df.format(defln_measurement);

            // put moment, reaction, and inertia values into appropriate textview widgets

            TextView momentField = (TextView) findViewById(R.id.textViewMoment);
            String momentString = formattedmoment;

            TextView reactionField = (TextView) findViewById(R.id.textViewReaction);
            String reactionString = formattedreaction;

            TextView inertiaField = (TextView) findViewById(R.id.textViewIx);
            String inertiaString = formattedinertia;

            TextView deflectionField = (TextView) findViewById(R.id.textViewDefln);
            String deflectionString = formatteddeflection;

            momentField.setText(momentString);
            reactionField.setText(reactionString);
            inertiaField.setText(inertiaString);
            deflectionField.setText(deflectionString);



        }
    }



    public void setUnitsMetric(){
        // set all units to metric

        TextView spanunits = (TextView)findViewById(R.id.textViewSpanUnits);
        TextView tribunits = (TextView)findViewById(R.id.textViewTribUnits);
        TextView deadunits = (TextView)findViewById(R.id.textViewDeadUnits);
        TextView liveunits = (TextView)findViewById(R.id.textViewLiveUnits);
        TextView deflunits = (TextView)findViewById(R.id.textViewDeflnUnits);
        TextView momentunits = (TextView)findViewById(R.id.textViewMomentUnits);
        TextView reactionunits = (TextView)findViewById(R.id.textViewReactionUnits);
        TextView inertiaunits1 = (TextView)findViewById(R.id.textViewIxUnits);



//        spanunits.setText(R.string.span_units_si);
        spanunits.setText("m");
        tribunits.setText(R.string.tribwidth_units_si);
        deadunits.setText(R.string.deadUDL_units_si);
        liveunits.setText(R.string.liveUDL_units_si);
        deflunits.setText(R.string.defln_units_si);
        momentunits.setText(R.string.Mf_units_si);
        reactionunits.setText(R.string.Rf_units_si);
        inertiaunits1.setText(R.string.Ix_units1_si);

        isMetric = true;



    }


    public void setUnitsImperial(){
        // set all units to imperial


        TextView spanunits = (TextView)findViewById(R.id.textViewSpanUnits);
        TextView tribunits = (TextView)findViewById(R.id.textViewTribUnits);
        TextView deadunits = (TextView)findViewById(R.id.textViewDeadUnits);
        TextView liveunits = (TextView)findViewById(R.id.textViewLiveUnits);
        TextView deflunits = (TextView)findViewById(R.id.textViewDeflnUnits);
        TextView momentunits = (TextView)findViewById(R.id.textViewMomentUnits);
        TextView reactionunits = (TextView)findViewById(R.id.textViewReactionUnits);
        TextView inertiaunits1 = (TextView)findViewById(R.id.textViewIxUnits);

        spanunits.setText(R.string.span_units_imp);
        tribunits.setText(R.string.tribwidth_units_imp);
        deadunits.setText(R.string.deadUDL_units_imp);
        liveunits.setText(R.string.liveUDL_units_imp);
        deflunits.setText(R.string.defln_units_imp);
        momentunits.setText(R.string.Mf_units_imp);
        reactionunits.setText(R.string.Rf_units_imp);
        inertiaunits1.setText(R.string.Ix_units1_imp);

        isMetric = false;



    }


    public float calcMomentMetric(float span, float trib, float dead, float live){

        // calculation of bending moment in metric (span in m, trib in m, dead in kPa, live in kPa)

        float wf = ((deadfactor * dead)+(livefactor * live)) * trib * span * span / (float)8.0;
        return wf;
    }


    public float calcReactionMetric(float span, float trib, float dead, float live){

        // calculation of beam reaction in metric (span in m, trib in m, dead in kPa, live in kPa)

        float Rf = ((deadfactor * dead)+(livefactor * live)) * trib * span / (float)2.0;
        return Rf;
    }

    public float calcInertiaMetric(float span, float trib, float live, float deflnratio){

        // calculation of moment of inertia in metric (span in m, trib in m, live in kPa, defln is a ratio of the span
        float span_mm = span * (float)1000.0; // mm
        
        float lineload = (live*trib); // N/mm

        float numerator = (float)5.0 * lineload * span_mm * span_mm * span_mm *span_mm;
        
        float denominator = (float)384 * YoungsMetric * (span_mm/deflnratio);
        
        float Ix = numerator/denominator;
        
        return Ix;
        
                
    }

    public float calcMomentImperial(float span, float trib, float dead, float live){

        // calculation of bending moment in imperial (span in ft, trib in ft, dead in psf, live in psf)

        float wf = ((deadfactor * dead)+(livefactor * live))* (float)0.001 * trib * span * span / (float)8.0;
        return wf;
    }

    public float calcReactionImperial(float span, float trib, float dead, float live){

        // calculation of beam reaction in imperial (span in ft, trib in ft, dead in psf, live in psf)

        float Rf = ((deadfactor * dead)+(livefactor * live))*(float)0.001 * trib * span / (float)2.0;
        return Rf;
    }

    public float calcInertiaImperial(float span, float trib, float live, float deflnratio){

        // calculation of moment of inertia in imperial (span in ft, trib in ft, live in psf, defln is a ratio of the span

        float YoungsImperial = YoungsMetric * (float) 0.145161;

        float span_in = span * (float)12; // in

        float live_ksf = live * (float)0.001 * live; // ksft

        float lineload = (live_ksf * trib)/(float)12.0; // kips/in

        float numerator = (float)5.0 * lineload * span_in * span_in * span_in * span_in;

        float denominator = (float)384 * YoungsImperial * (span_in/deflnratio);

        float Ix = numerator/denominator;

        return Ix;


    }


}
