package antony.flipcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    Button mNormalButton;
    Button mMediumButton;
    Button mHardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mNormalButton = (Button) findViewById(R.id.normal_difficulty);
        mMediumButton = (Button) findViewById(R.id.medium_difficulty);
        mHardButton = (Button) findViewById(R.id.hard_difficulty);

        setListeners();
    }

    private void handleDifficultyButton(String difficulty) {
        System.out.println("Normal");
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra("difficulty", difficulty);

        startActivity(i);
    }

    private void setListeners() {
        mNormalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDifficultyButton("normal");
            }
        });

        mMediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDifficultyButton("medium");
            }
        });

        mHardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDifficultyButton("hard");
            }
        });
    }

    //        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
}
