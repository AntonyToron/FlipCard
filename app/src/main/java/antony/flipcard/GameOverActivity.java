package antony.flipcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    TextView scoreView;
    TextView winnerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        scoreView = (TextView) findViewById(R.id.final_winner_score);
        winnerView = (TextView) findViewById(R.id.final_winner);

        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);
        int winner = intent.getIntExtra("winner", 1);

        scoreView.setText("Winner: Player " + winner + "!");
        winnerView.setText("Final score: " + score);
    }

//    @Override
//    protected void onDestroy() {
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // remove game activity too (all above menu activity)
        startActivity(intent);
    }

}
