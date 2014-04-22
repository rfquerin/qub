package com.rfquerin.simplebeamtool.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.RadioButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

import static java.lang.StrictMath.pow;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    public void radiobuttonClick(View v) {

        // handle radio button click

        // --test --TextView deflunits = (TextView)findViewById(R.id.textViewDeflnUnits);
        TextView spanunits = (TextView)findViewById(R.id.textViewSpanUnits);
        TextView tribunits = (TextView)findViewById(R.id.textViewTribUnits);
        TextView deadunits = (TextView)findViewById(R.id.textViewDeadUnits);
        TextView liveunits = (TextView)findViewById(R.id.textViewLiveUnits);
        TextView deflunits = (TextView)findViewById(R.id.textViewDeflnUnits);
        TextView momentunits = (TextView)findViewById(R.id.textViewMomentUnits);
        TextView reactionunits = (TextView)findViewById(R.id.textViewReactionUnits);
        TextView inertiaunits1 = (TextView)findViewById(R.id.textViewIxExponent);
        TextView inertiaunits2 = (TextView)findViewById(R.id.textViewIxUnits);


        // Is the button now checked?
        boolean checked = ((RadioButton) v).isChecked();

        // Check which radio button was clicked
        switch(v.getId()) {
            case R.id.radioButtonSI:
                if (checked)
                    // deflunits.setText("METRIC"); -- test case

                    // set all textviews with units to be metric ones

                    spanunits.setText(R.string.span_units_si);
                    tribunits.setText(R.string.tribwidth_units_si);
                    deadunits.setText(R.string.deadUDL_units_si);
                    liveunits.setText(R.string.liveUDL_units_si);
                    deflunits.setText(R.string.defln_units_si);
                    momentunits.setText(R.string.Mf_units_si);
                    reactionunits.setText(R.string.Rf_units_si);
                    inertiaunits1.setText(R.string.Ix_units1_si);
                    inertiaunits2.setText(R.string.Ix_units2_si);







                    break;
            case R.id.radioButtonIMP:
                if (checked)

                    // deflunits.setText("IMP"); -- test case

                    // set all textviews with units to be imperial ones

                    spanunits.setText(R.string.span_units_imp);
                    tribunits.setText(R.string.tribwidth_units_imp);
                    deadunits.setText(R.string.deadUDL_units_imp);
                    liveunits.setText(R.string.liveUDL_units_imp);
                    deflunits.setText(R.string.defln_units_imp);
                    momentunits.setText(R.string.Mf_units_imp);
                    reactionunits.setText(R.string.Rf_units_imp);
                    inertiaunits1.setText(R.string.Ix_units1_imp);
                    inertiaunits2.setText(R.string.Ix_units2_imp);


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



        // compute bending moment

        float load = (((float)1.25*deadload)+((float)1.5*liveload))*tribwidth;

        float moment = (load * span * span) * (float) 0.125;

        float liveload_linear = liveload * tribwidth;

        // compute end reaction

        float reaction = (load * span)*(float)0.50;


        // compute required moment of inertia

        float span_mm = (float)1000.0 * span;

        float defln_measurement = span_mm / deflnRatio;

        float numerator = (float)5.0 * liveload_linear*span_mm*span_mm*span_mm*span_mm;

        float denominator = (float)384 * (float)200000 * defln_measurement;

        float inertia = numerator/(denominator*(float)1e6);



        // format moment and reaction values into strings with only 1 decimal place

        DecimalFormat df = new DecimalFormat("0.#");

        String formattedmoment = df.format(moment);
        String formattedreaction = df.format(reaction);
        String formattedinertia = df.format(inertia);
        String formatteddeflection = df.format(defln_measurement);

        // put moment, reaction, and inertia values into appropriate textview widgets

        TextView momentField = (TextView)findViewById(R.id.textViewMoment);
        String momentString = formattedmoment;

        TextView reactionField = (TextView)findViewById(R.id.textViewReaction);
        String reactionString = formattedreaction;

        TextView inertiaField = (TextView)findViewById(R.id.textViewIx);
        String inertiaString = formattedinertia;

        TextView deflectionField = (TextView)findViewById(R.id.textViewDefln);
        String deflectionString = formatteddeflection;

        momentField.setText(momentString);
        reactionField.setText(reactionString);
        inertiaField.setText(inertiaString);
        deflectionField.setText(deflectionString);





    }
}
