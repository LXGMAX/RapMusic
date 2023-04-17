package xyz.gzjnas.rapmusic;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private enum ActionUrl {
        PAUSE("/play/pause"),
        NEXT("/play/next"),
        PREVIOUS("/play/previous"),
        VOLUMEUP("/play/volup"),
        VOLUMEDOWN("/play/voldown"),
        LIKE("/play/like"),
        LYRIC("/play/lyric"),
        MUTE("/play/mute"),
        GETVOL("/sys/getvol"),
        SETVOL("/sys/setvol");

        private final String path;

        ActionUrl(String path) {
            this.path = path;
        }

        public String getPath() {
            return this.path;
        }
    }

    private EditText ipEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipEdit = findViewById(R.id.ipEdit);
        loadRemoteIp();
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            saveRemoteIp(ipEdit.getText().toString());
            ipEdit.setEnabled(false);
            ipEdit.setEnabled(true);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.hideSoftInputFromWindow(ipEdit.getWindowToken(),0);
        });

        ImageButton playButton = findViewById(R.id.play);
        playButton.setOnClickListener(v -> sendHttpGet(ActionUrl.PAUSE.getPath()));

        ImageButton nextButton = findViewById(R.id.next);
        nextButton.setOnClickListener(v -> sendHttpGet(ActionUrl.NEXT.getPath()));

        ImageButton prevButton = findViewById(R.id.previous);
        prevButton.setOnClickListener(v -> sendHttpGet(ActionUrl.PREVIOUS.getPath()));

        ImageButton likeButton = findViewById(R.id.like);
        likeButton.setOnClickListener(v -> sendHttpGet(ActionUrl.LIKE.getPath()));

        ImageButton lyricButton = findViewById(R.id.lyric);
        lyricButton.setOnClickListener(v -> sendHttpGet(ActionUrl.LYRIC.getPath()));

        ImageButton volumeUpButton = findViewById(R.id.volumeFull);
        volumeUpButton.setOnClickListener(v -> sendHttpGet(ActionUrl.VOLUMEUP.getPath()));

        ImageButton volumeDownButton = findViewById(R.id.volumeDown);
        volumeDownButton.setOnClickListener(v-> sendHttpGet(ActionUrl.VOLUMEDOWN.getPath()));

        ImageButton muteButton = findViewById(R.id.mute);
        muteButton.setOnClickListener(v -> sendHttpGet(ActionUrl.MUTE.getPath()));

        SeekBar soundBar = findViewById(R.id.soundBar);
        soundBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void sendHttpGet(String path) {
        String remote = getRemoteIp();
        if (Objects.equals(remote, "")) {
            Toast.makeText(MainActivity.this, "请设置IP", Toast.LENGTH_LONG).show();
            return;
        }

        String URL = "http://" + remote + ":18890/" + path;
        new HttpUtils().execute(URL);
    }

    private void saveRemoteIp(String ip) {
        SharedPreferences sharedPreferences = getSharedPreferences("remote_ip", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // todo check ip address
        Log.d(TAG, "saveRemoteIp: " + ip);
        if (Patterns.IP_ADDRESS.matcher(ip).matches()) {
            editor.putString("ip", ip);
            editor.apply();
        } else {
            Toast.makeText(MainActivity.this, "IP不正确", Toast.LENGTH_LONG).show();
        }
    }

    private String getRemoteIp() {
        SharedPreferences sharedPref = getSharedPreferences("remote_ip", Context.MODE_PRIVATE);
        return sharedPref.getString("ip", "");
    }

    private void loadRemoteIp() {
        if (ipEdit != null) {
            String loadIp = getRemoteIp();
            ipEdit.setText(loadIp);
        }
    }
}