package ua.sergeylevchenko.lesson27;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private final static String FILENAME = "sample.txt"; // имя файла
    private EditText mEditText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mEditText = findViewById(R.id.editText);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open:
                openFile();
                return true;
            case R.id.action_save:
                saveFile();
                return true;
            default:
                return true;
            case R.id.action_settings:
                Intent intent = new Intent();
                intent.setClass(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
    }

    // Метод для открытия файла
    private void openFile() {
        try {
            InputStream inputStream = openFileInput(MainActivity.FILENAME);

            if (inputStream != null) {
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isr);
                String line;
                StringBuilder builder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }

                inputStream.close();
                mEditText.setText(builder.toString());
            }
        } catch (Throwable t) {
            Toast.makeText(getApplicationContext(),
                    "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    // Метод для сохранения файла
    private void saveFile() {
        try {
            OutputStream outputStream = openFileOutput(MainActivity.FILENAME, 0);
            OutputStreamWriter osw = new OutputStreamWriter(outputStream);
            osw.write(mEditText.getText().toString());
            osw.close();
        } catch (Throwable t) {
            Toast.makeText(getApplicationContext(),
                    "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        // читаем установленное значение из CheckBoxPreference
        if (prefs.getBoolean(getString(R.string.pref_openmode), false)) {
            openFile();
            // читаем размер шрифта из EditTextPreference
            float fSize = Float.parseFloat(prefs.getString(
                    getString(R.string.pref_size), "20"));
// применяем настройки в текстовом поле
            mEditText.setTextSize(fSize);
            // читаем стили текста из ListPreference
            String regular = prefs.getString(getString(R.string.pref_style), "");
            int typeface = Typeface.NORMAL;

            if (regular.contains("Полужирный"))
                typeface += Typeface.BOLD;

            if (regular.contains("Курсив"))
                typeface += Typeface.ITALIC;

// меняем настройки в EditText
            mEditText.setTypeface(null, typeface);
        }
    }

}