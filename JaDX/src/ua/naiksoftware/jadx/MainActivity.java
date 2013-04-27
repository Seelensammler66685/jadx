package ua.naiksoftware.jadx;

import android.app.*;
import android.os.*;
import android.view.View;
import jadx.Main;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import filedialog.FileDialog;
import android.util.Log;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.content.DialogInterface;

public class MainActivity extends Activity {

    private final String tag = getClass().getName();

	private Button decompile;
	private TextView inputTw, outputTw;
	private boolean inputOk = false, outputOk = false;
	private String strInput, strOutput;
	private static final int SELECT_INPUT = 0x1;
	private static final int SELECT_OUTPUT = 0x2;
	private SharedPreferences prefs;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		inputTw = (TextView) findViewById(R.id.text_input);
		outputTw = (TextView) findViewById(R.id.text_output);
		decompile = (Button) findViewById(R.id.btn_decompile);
		decompile.setEnabled(false);
		prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
		strInput = prefs.getString("input_file", "");
		strOutput = prefs.getString("output_dir", "");
		if (!strInput.equals("")) {
			inputTw.setText(strInput);
			inputOk = true;
		}
		if (!strOutput.equals("")) {
			outputTw.setText(strOutput);
			outputOk = true;
		}
		if (inputOk && outputOk) {
			decompile.setEnabled(true);
		}
    }

    /**
     * Declared in xml layout
     *
     * @param v Button launch
     */
    public void decompile(View v) {
        new Decompiler(this).execute(strInput, strOutput);
    }

	public void selectInput(View v) {
		Intent intent = new Intent(this, FileDialog.class);
        intent.putExtra(FileDialog.START_PATH, Environment.getExternalStorageDirectory().getAbsolutePath());
        //can user select directories or not
        //intent.putExtra(FileDialog.CAN_SELECT_DIR, false);
        //alternatively you can set file filter
        intent.putExtra(FileDialog.FORMAT_FILTER, new String[]{"apk", "dex", "jar", "class"});
        startActivityForResult(intent, SELECT_INPUT);

	}

	public void selectOutput(View v) {
		Intent intent = new Intent(this, FileDialog.class);
        intent.putExtra(FileDialog.START_PATH, Environment.getExternalStorageDirectory().getAbsolutePath());
        //can user select directories or not
        intent.putExtra(FileDialog.CAN_SELECT_DIR, true);
        //alternatively you can set file filter
        //intent.putExtra(FileDialog.FORMAT_FILTER, new String[]{"ext1", "ext2"});
        startActivityForResult(intent, SELECT_OUTPUT);
	}

	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_CANCELED) {
			return;
		}
        String strSelected = data.getStringExtra(FileDialog.RESULT_PATH);
        Log.d(tag, "selected=" + strSelected);
		SharedPreferences.Editor prefsEditor = prefs.edit();
        switch (requestCode) {
			case SELECT_INPUT:
				strInput = strSelected;
				inputTw.setText(strInput);
				prefsEditor.putString("input_file", strInput);
				inputOk = true;
				break;
			case SELECT_OUTPUT:
				strOutput = strSelected;
				outputTw.setText(strOutput);
				prefsEditor.putString("output_dir", strOutput);
				outputOk = true;
				break;
		}
		prefsEditor.commit();
		if (inputOk && outputOk) {
			decompile.setEnabled(true);
		}
        super.onActivityResult(requestCode, resultCode, data);
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1, Menu.NONE, R.string.info);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		TextView info = new TextView(this);
        info.setText(R.string.description);
        info.setTextSize(15);
        info.setPadding(20, 20, 20, 20);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.info);
        builder.setCancelable(true);
        builder.setView(info);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface p1, int p2) {
					// auto close
				}
			});
        builder.show();

		return super.onOptionsItemSelected(item);
	}
}
