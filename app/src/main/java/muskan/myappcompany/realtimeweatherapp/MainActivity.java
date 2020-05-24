package muskan.myappcompany.realtimeweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView resultTextView;

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try{

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data!=-1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                //String tempInfo = jsonObject.getString("main");
                //Log.i("Weather Info", weatherInfo);

                JSONArray arr = new JSONArray(weatherInfo);
                //JSONArray arr2 = new JSONArray(tempInfo);
                String message = "";
                for(int i = 0 ; i<arr.length(); i++){
                    JSONObject jsonPart = arr.getJSONObject(i);

                    String main = jsonPart.getString("main");
                    String description = jsonPart.getString("description");

                    if(!main.equals("") && !description.equals("")){
                        message += main + ": " + description + "\r\n";
                    }else{
                        Toast.makeText(getApplicationContext(), "Could not find weather!", Toast.LENGTH_SHORT).show();
                    }
                }
                /*for(int i = 0 ; i<arr2.length(); i++){
                    JSONObject jsonPart2 = arr2.getJSONObject(i);

                    String temp = jsonPart2.getString("temp");
                    String pressure = jsonPart2.getString("pressure");
                    String humidity = jsonPart2.getString("pressure");
                    if(!temp.equals("") && !pressure.equals("") && !humidity.equals("")){
                        message += "Temperature: " + temp + "F \r\nPressure: " + pressure + "\r\nHumidity" + humidity;
                    }else{
                        Toast.makeText(getApplicationContext(), "Could not find weather!", Toast.LENGTH_SHORT).show();
                    }
                }*/

                if(!message.equals("")){
                    resultTextView.setText(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Could not find weather!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void getWeather(View view){


        DownloadTask task = new DownloadTask();
        task.execute("https://openweathermap.org/data/2.5/weather?q=" + editText.getText().toString() + "&appid=439d4b804bc8187953eb36d2a8c26a02");
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        resultTextView = (TextView) findViewById(R.id.resultTextView);



    }
}
