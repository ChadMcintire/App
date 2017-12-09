package com.example.chad.smstrialapp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class EditTemplate extends AppCompatActivity {
    List<String> listOfTemplateNames = new ArrayList<>();

    boolean needUpdate = false;

    private static final String tag = "Captains Log: ";

    Button saveTextButton;
    Button deleteTextButton;
    final Context context = this;
    EditText templateText;
    String currentTemplate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_template);
        final Context contex = this;
        final SavePopUp spp = new SavePopUp();
        final SavePopUp templateName = new SavePopUp();

        //deleteTextButton = (Button) findViewById(R.id.deleteTemp);
        saveTextButton = (Button) findViewById(R.id.saveText);
        templateText = (EditText) findViewById(R.id.templateText);
        //if(TemplateSave.loadSet(this, "listOfTemplateNames").size() == 0);{}

        saveTextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final EditText input = new EditText(context); // This could also come from an xml resource, in which case you would use findViewById() to access the input

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        spp.setEntry(value); // mItem is a member variable in your Activity
                        dialog.dismiss();

                        templateName.setEntry(spp.getEntry());
                        TemplateSave ts = new TemplateSave();
                        Set<String> temporaryNames = ts.loadSet(context, "listOfTemplateNames");
                        temporaryNames.add(templateName.getEntry());
                        //listOfTemplateNames.add(templateName.getEntry());
                        ts.save(context, templateName.getEntry(), templateText.getText().toString());
                        ts.saveSet(context, "listOfTemplateNames", new HashSet(temporaryNames));
                        Toast.makeText(getBaseContext(),
                                spp.getEntry(),
                                Toast.LENGTH_SHORT).show();
                        reload();
                    }
                });
                builder.show();
            }
        });

       /*deleteTextButton.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {

                TemplateSave ts = new TemplateSave();
                Set<String> temporaryNames = ts.loadSet(context, "listOfTemplateNames");
                Iterator<String> it = temporaryNames.iterator();
                while (it.hasNext()) {
                    String s = it.next();
                    if (s.contentEquals(currentTemplate)) {
                        it.remove();
                    }
                }
                ts.delete(context, currentTemplate);
                //listOfTemplateNames.add(templateName.getEntry());
                //ts.save(context, templateName.getEntry(), templateText.getText().toString());
                ts.saveSet(context, "listOfTemplateNames", new HashSet(temporaryNames));
            }
        });*/

        Spinner betterSpinner = (Spinner) findViewById(R.id.spinner2);
       betterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TemplateSave tempSave = new TemplateSave();
                templateText.setText(tempSave.load(context, parent.getItemAtPosition(position).toString()));
                currentTemplate = parent.getItemAtPosition(position).toString();
                setDefault();
                Toast.makeText(getBaseContext(), currentTemplate,
                        Toast.LENGTH_SHORT).show();
            }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }

       });

        TemplateSave ts = new TemplateSave();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                new ArrayList(ts.loadSet(context, "listOfTemplateNames")));
        betterSpinner.setAdapter(arrayAdapter);
        //addListenerOnSpinnerItemSelection();
        //new ArrayList(ts.loadSet(context, "listOfTemplateNames"))


    }

    public void setDefault(){
        TemplateSave ts = new TemplateSave();
        ts.save(this, "pianoAppointment", ts.load(this, currentTemplate));
    }

    public void reload(){
        this.recreate();
    }


}
