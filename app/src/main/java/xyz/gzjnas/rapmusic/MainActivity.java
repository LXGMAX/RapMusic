package xyz.gzjnas.rapmusic;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.UnknownHostException;

import xyz.gzjnas.rapmusic.HttpUtils;

public class MainActivity extends AppCompatActivity {
    private final String[] path = {
            "/play/pause",
            "/play/next",
            "/play/previous",
            "/play/volup",
            "/play/voldown",
            "/play/like",
            "/play/lyric",
            "/play/mute",
            "/sys/getvol"
    };

    private enum PathIndex {
        PAUSE,
        NEXT,
        PREVIOUS,
        VOLUMEUP,
        VOLUMEDOWN,
        LIKE,
        LYRIC,
        MUTE,
        GETVOL
    };

    private EditText ipEdit;

    /*ImageButton nextButton = (ImageButton) findViewById(R.id.next);
    ImageButton previousButton = (ImageButton) findViewById(R.id.previous);
    ImageButton likeButton = (ImageButton) findViewById(R.id.like);
    ImageButton lyricButton = (ImageButton) findViewById(R.id.lyric);
    ImageButton volFullButton = (ImageButton) findViewById(R.id.volumeFull);
    ImageButton muteButton = (ImageButton) findViewById(R.id.mute);*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipEdit = (EditText) findViewById(R.id.ipEdit);
        loadRemoteIp();
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            saveRemoteIp(ipEdit.getText().toString());
            ipEdit.setEnabled(false);
            ipEdit.setEnabled(true);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.hideSoftInputFromWindow(ipEdit.getWindowToken(),0);
        });

        ImageButton playButton = (ImageButton) findViewById(R.id.play);
        playButton.setOnClickListener(v -> {
            sendHttpGet(PathIndex.PAUSE.ordinal());
        });

        ImageButton nextButton = (ImageButton) findViewById(R.id.next);
        nextButton.setOnClickListener(v -> {
            sendHttpGet(PathIndex.NEXT.ordinal());
        });

        ImageButton prevButton = (ImageButton) findViewById(R.id.previous);
        prevButton.setOnClickListener(v -> {
            sendHttpGet(PathIndex.PREVIOUS.ordinal());
        });

        ImageButton likeButton = (ImageButton) findViewById(R.id.like);
        likeButton.setOnClickListener(v -> {
            sendHttpGet(PathIndex.LIKE.ordinal());
        });

        ImageButton lyricButton = (ImageButton) findViewById(R.id.lyric);
        lyricButton.setOnClickListener(v -> {
            sendHttpGet(PathIndex.LYRIC.ordinal());
        });

        ImageButton volumeUpButton = (ImageButton) findViewById(R.id.volumeFull);
        volumeUpButton.setOnClickListener(v -> {
            sendHttpGet(PathIndex.VOLUMEUP.ordinal());
        });

        ImageButton muteButton = (ImageButton) findViewById(R.id.mute);
        muteButton.setOnClickListener(v -> {
            sendHttpGet(PathIndex.MUTE.ordinal());
        });
    }

    private void sendHttpGet(int index) {
        String remote = getRemoteIp();
        String URL = "http://" + remote + ":18890/" + path[index];
        new HttpUtils().execute(URL);
    }

    private void saveRemoteIp(String ip) {
        SharedPreferences sharedPreferences = getSharedPreferences("remote_ip", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // todo check ip address
        Log.d(TAG, "saveRemoteIp: " + ip);
        if (Patterns.IP_ADDRESS.matcher(ip).matches() == true) {
            editor.putString("ip", ip);
            editor.apply();
        } else {
            Toast.makeText(MainActivity.this, "IP不正确", Toast.LENGTH_LONG).show();
            return;
        }
    }

    private String getRemoteIp() {
        SharedPreferences sharedPref = getSharedPreferences("remote_ip", Context.MODE_PRIVATE);
        // todo check ip
        return sharedPref.getString("ip", "");
    }

    private void loadRemoteIp() {
        if (ipEdit != null) {
            String loadIp = getRemoteIp();
            ipEdit.setText(loadIp);
        }
    }
}