package com.sayone.omidyar.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.sayone.omidyar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.CollationElementIterator;

public class JsonParserActivity extends AppCompatActivity {

    public static final String JSON_STRING="{\"frequency\": [\n" +
            "    {\n" +
            "      \"harvestFrequency\": \"one-time\",\n" +
            "      \"harvestFrequencyHindi\": \"एक बार\",\n" +
            "      \"value\": 1\n" +
            "    },\n" +
            "    {\n" +
            "      \"harvestFrequency\": \"per day\",\n" +
            "      \"harvestFrequencyHindi\": \"हर दिन\",\n" +
            "      \"value\": 365\n" +
            "    },\n" +
            "    {\n" +
            "      \"harvestFrequency\": \"per week\",\n" +
            "      \"harvestFrequencyHindi\": \"प्रति सप्ताह\",\n" +
            "      \"value\": 52\n" +
            "    },\n" +
            "    {\n" +
            "      \"harvestFrequency\": \"per month\",\n" +
            "      \"harvestFrequencyHindi\": \"प्रति माह\",\n" +
            "      \"value\": 12\n" +
            "    },\n" +
            "    {\n" +
            "      \"harvestFrequency\": \"per year\",\n" +
            "      \"harvestFrequencyHindi\": \"प्रति वर्ष\",\n" +
            "      \"value\": 1\n" +
            "    }\n" +
            "  ]}";
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_parser);

        text=(TextView)findViewById(R.id.text);

        String data = "";
        try {
            // Create the root JSONObject from the JSON string.
            JSONObject  jsonRootObject = new JSONObject(JSON_STRING);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("frequency");

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int value = Integer.parseInt(jsonObject.optString("value").toString());
                String harvestFrequency = jsonObject.optString("harvestFrequency").toString();
                String harvestFrequencyHindi = jsonObject.optString("harvestFrequencyHindi").toString();


                data += "value= "+ value +" \n harvestFrequency= "+ harvestFrequency +" \n harvestFrequencyHindi= "+ harvestFrequencyHindi +" \n ";
            }
            text.setText(data);
        } catch (JSONException e) {e.printStackTrace();}

    }



}
