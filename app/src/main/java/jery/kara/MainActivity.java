package jery.kara;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import jery.kara.karapersonal.KaraPersonalActivity;
import jery.kara.karaqueue.KaraQueueActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_UserRoom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, KaraPersonalActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.btn_QueueRom).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, KaraQueueActivity.class);
                startActivity(i);
            }
        });
    }
}
