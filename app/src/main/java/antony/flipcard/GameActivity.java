package antony.flipcard;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private static final int NORMAL_SIZE = 4;
    private static final int MEDIUM_SIZE = 6;
    private static final int HARD_SIZE = 8;
    //private Button[][] buttons;
    private ImageView[][] cards;
    private int[][] backgrounds;
    private TextView player1_turn;
    private TextView player2_turn;
    private TextView player1_points;
    private TextView player2_points;

    // might need locks on the below resources (specifically canFlip)
    private int cardFlipped = 0;
    private int i_card0;
    private int j_card0;
    private int i_card1;
    private int j_card1;

    boolean canFlip = true;

    private int player = 1; // 1 = player 1, 2 = player 2

    private int player1_score = 0;
    private int player2_score = 0;

    private int cards_found = 0;
    private int total_cards;

    private String difficulty;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_game_normal);

        Intent intent = getIntent();
        String difficulty = intent.getStringExtra("difficulty");

        System.out.println("Difficulty: " + difficulty);

        setLayout(difficulty);
        initializeText();

        this.difficulty = difficulty;

        setButtonBackgrounds(difficulty);
        initializeBoard(difficulty);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setLayout(String difficulty) {
        if (difficulty.equals("normal")) {
            setContentView(R.layout.activity_game_normal);

        } else if (difficulty.equals("medium")) {
            setContentView(R.layout.activity_game_medium);
        } else {
            setContentView(R.layout.activity_game_hard);
        }
    }

    private void initializeText() {
        player1_turn = (TextView) findViewById(R.id.current_player1);
        player2_turn = (TextView) findViewById(R.id.current_player2);
        player1_turn.setText("Your Turn!");
        player2_turn.setText("");

        player1_points = (TextView) findViewById(R.id.player1_points);
        player2_points = (TextView) findViewById(R.id.player2_points);
    }

    private void setButtonBackgrounds(String difficulty) {
        if (difficulty.equals("normal")) {
            cards = new ImageView[NORMAL_SIZE][NORMAL_SIZE];

            for (int i = 1; i < NORMAL_SIZE + 1; i++) {
                for (int j = 1; j < NORMAL_SIZE + 1; j++) {
                    final int x = i - 1; // for passing into inner class
                    final int y = j - 1;

                    String buttonId = "normal_row" + i + "_" + j;
                    int resId = getResources().getIdentifier(buttonId, "id", getPackageName());
                    cards[i - 1][j - 1] = (ImageView) findViewById(resId);

                    initializeCard(cards[i - 1][j - 1], x, y, difficulty);
                }
            }
        } else if (difficulty.equals("medium")) {
            cards = new ImageView[MEDIUM_SIZE][MEDIUM_SIZE];

            for (int i = 1; i < MEDIUM_SIZE + 1; i++) {
                for (int j = 1; j < MEDIUM_SIZE + 1; j++) {
                    final int x = i - 1; // for passing into inner class
                    final int y = j - 1;

                    String buttonId = "normal_row" + i + "_" + j;
                    int resId = getResources().getIdentifier(buttonId, "id", getPackageName());
                    cards[i - 1][j - 1] = (ImageView) findViewById(resId);

                    initializeCard(cards[i - 1][j - 1], x, y, difficulty);
                }
            }
        } else {
            cards = new ImageView[HARD_SIZE][HARD_SIZE];

            for (int i = 1; i < HARD_SIZE + 1; i++) {
                for (int j = 1; j < HARD_SIZE + 1; j++) {
                    final int x = i - 1; // for passing into inner class
                    final int y = j - 1;

                    String buttonId = "normal_row" + i + "_" + j;
                    int resId = getResources().getIdentifier(buttonId, "id", getPackageName());
                    cards[i - 1][j - 1] = (ImageView) findViewById(resId);

                    initializeCard(cards[i - 1][j - 1], x, y, difficulty);
                }
            }
        }
    }

    private void initializeCard(ImageView image, final int x, final int y, String difficulty) {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardClicked(x, y);
            }
        });

        // set initial background image
        //b.setText("?");
        //image.setBackgroundResource(R.drawable.question_mark_small);
        int width;
        int height;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;


        //image.setImageAlpha(127);
        Bitmap b;
        if (difficulty.equals("normal")) {
            b = BitmapFactory.decodeResource(getResources(), R.drawable.question_mark);
            ViewGroup.LayoutParams params = image.getLayoutParams();
            params.width = width / NORMAL_SIZE;
            params.height = width / NORMAL_SIZE; // same as width
            image.setLayoutParams(params);
        } else if (difficulty.equals("medium")) {
            b = BitmapFactory.decodeResource(getResources(), R.drawable.question_mark_small);
            ViewGroup.LayoutParams params = image.getLayoutParams();
            params.width = width / MEDIUM_SIZE;
            params.height = width / MEDIUM_SIZE; // same as width
            image.setLayoutParams(params);

        } else {
            ViewGroup.LayoutParams params = image.getLayoutParams();
            params.width = width / HARD_SIZE;
            params.height = width / HARD_SIZE; // same as width
            image.setLayoutParams(params);
            b = BitmapFactory.decodeResource(getResources(), R.drawable.question_mark);
        }

        //b.setHasAlpha(true);

        image.setImageBitmap(b);


        // set initial width and such
        //b.setLayoutParams(new LinearLayout.LayoutParams(10, 100));
    }

    private void cardClicked(int i, int j) {
        final ImageView card = cards[i][j];

        if (!canFlip || backgrounds[i][j] == -1) {
            return; // should not flip any cards
        }
        if (cardFlipped == 1 && i == i_card0 && j == j_card0) { // same card
            return;
        }

        if (cardFlipped == 0) {
            cardFlipped++;

            // flip the card over
            //card.setText("Clicked");
            //card.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.elephant_small));
            card.setImageBitmap(BitmapFactory.decodeResource(getResources(), backgrounds[i][j]));

            // set location
            i_card0 = i;
            j_card0 = j;
        } else if (cardFlipped == 1) {
            cardFlipped = 2;

            System.out.println("flipped second card");

            // flip the card over
            //card.setText("Clicked");
            //card.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.elephant_small));
            card.setImageBitmap(BitmapFactory.decodeResource(getResources(), backgrounds[i][j]));

            // disable clicking for a while
            canFlip = false;

            // set location
            i_card1 = i;
            j_card1 = j;

            // check if the flip was correct
            if (backgrounds[i_card0][j_card0] == backgrounds[i_card1][j_card1]) {
                animateCardRemove();
            } else {
                // animate card flip
                // can be done via : https://developer.android.com/training/animation/cardflip.html

                animateCardFlip();
            }

            // set cardFlipped to 0 again
            cardFlipped = 0;
        } else {
            // Shouldn't get here
            System.out.println("Shouldn't get here yet.");
        }

    }

    private void animateCardFlip() {
        final String difficulty = new String(this.difficulty);

        // wait a few seconds, and then flip over the cards again
        final Runnable resetCards = new Runnable() {
            @Override
            public void run() {
                if (difficulty.equals("normal")) {
                    cards[i_card0][j_card0].setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.question_mark));
                    cards[i_card1][j_card1].setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.question_mark));
                } else if (difficulty.equals("medium")) {
                    cards[i_card0][j_card0].setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.question_mark));
                    cards[i_card1][j_card1].setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.question_mark));
                } else {
                    cards[i_card0][j_card0].setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.question_mark));
                    cards[i_card1][j_card1].setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.question_mark));
                }

                // change player turn
                changePlayer(difficulty);

                canFlip = true;
            }
        };


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(resetCards);
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 2500);
    }

    private void animateCardRemove() {
        final Runnable resetCards = new Runnable() {
            @Override
            public void run() {
                // remove both cards
                cards[i_card0][j_card0].setImageAlpha(0);
                cards[i_card1][j_card1].setImageAlpha(0);
                backgrounds[i_card0][j_card0] = -1;
                backgrounds[i_card1][j_card1] = -1;

                // increment player score
                if (player == 1) {
                    player1_score++;
                    player1_points.setText("Player 1 Points: " + player1_score);
                } else {
                    player2_score++;
                    player2_points.setText("Player 2 Points: " + player2_score);
                }

                cards_found += 2;

                if (cards_found == total_cards) { // end of game
                    System.out.println("End of Game!");

                    openGameOverScreen();

                }

                // allow flipping again
                canFlip = true;
            }
        };

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(resetCards);
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 1500);
    }

    private void openGameOverScreen() {
        Intent i = new Intent(this, GameOverActivity.class);
        i.putExtra("score", player1_score > player2_score ? player1_score : player2_score);
        i.putExtra("winner", player1_score > player2_score ? 1 : 2);

        startActivity(i);
    }

    private void changePlayer(String difficulty) {
        if (player == 1) {
            player = 2;
            player2_turn.setText("Your Turn!");
            player1_turn.setText("");
        } else {
            player = 1;
            player1_turn.setText("Your Turn!");
            player2_turn.setText("");
        }
        System.out.println("Current rotation: " + cards[0][0].getRotation());
        int length;
        if (difficulty.equals("normal")) {
            length = NORMAL_SIZE;
        } else if (difficulty.equals("medium")) {
            length = MEDIUM_SIZE;
        } else {
            length = HARD_SIZE;
        }

        // rotate all of the cards 180 degrees for the other player
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                cards[i][j].setPivotX(cards[i][j].getWidth() / 2);
                cards[i][j].setPivotY(cards[i][j].getHeight() / 2);
                int angle = 180;

                cards[i][j].setRotation(cards[i][j].getRotation() + angle);
            }
        }

    }

    private static class Point {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private void initializeBoard(String difficulty) {
        LinkedList<Point> points = new LinkedList<Point>();
        // R.drawable.giraffe2_transparent
        int[] images = {R.drawable.elephant, R.drawable.monkey, R.drawable.cow, R.drawable.giraffe};

        if (difficulty.equals("normal")) {

            int pointer = 0;

            backgrounds = new int[NORMAL_SIZE][NORMAL_SIZE];
            total_cards = NORMAL_SIZE * NORMAL_SIZE;

            for (int i = 0; i < NORMAL_SIZE; i++) {
                for (int j = 0; j < NORMAL_SIZE; j++) {
                    points.add(new Point(i, j));
                }
            }

            Random rand = new Random();
            for (int i = 0; i < (NORMAL_SIZE * NORMAL_SIZE) / 2; i++) {
                Point a = points.remove(rand.nextInt(points.size() - 1));
                Point b;
                int temp = points.size() - 1;
                if (temp == 0) {
                    b = points.remove();
                } else {
                    b = points.remove(rand.nextInt(points.size() - 1));
                }

                // these two will have the same animal
                backgrounds[a.x][a.y] = images[pointer];
                backgrounds[b.x][b.y] = images[pointer];

                // increment pointer
                if (pointer < images.length - 1) {
                    pointer++;
                } else if (pointer == images.length - 1) {
                    pointer = 0;
                }
            }
        } else if (difficulty.equals("medium")) {
            //int[] images = {R.drawable.elephant_small_medium, R.drawable.monkey_small_medium, R.drawable.cow_small_medium, R.drawable.giraffe2_small_medium};
            int pointer = 0;

            backgrounds = new int[MEDIUM_SIZE][MEDIUM_SIZE];
            total_cards = MEDIUM_SIZE * MEDIUM_SIZE;

            for (int i = 0; i < MEDIUM_SIZE; i++) {
                for (int j = 0; j < MEDIUM_SIZE; j++) {
                    points.add(new Point(i, j));
                }
            }

            Random rand = new Random();
            for (int i = 0; i < (MEDIUM_SIZE * MEDIUM_SIZE) / 2; i++) {
                Point a = points.remove(rand.nextInt(points.size() - 1));
                Point b;
                int temp = points.size() - 1;
                if (temp == 0) {
                    b = points.remove();
                } else {
                    b = points.remove(rand.nextInt(points.size() - 1));
                }

                // these two will have the same animal
                backgrounds[a.x][a.y] = images[pointer];
                backgrounds[b.x][b.y] = images[pointer];

                // increment pointer
                if (pointer < images.length - 1) {
                    pointer++;
                } else if (pointer == images.length - 1) {
                    pointer = 0;
                }
            }
        } else {
            //int[] images = {R.drawable.elephant_small, R.drawable.monkey_small, R.drawable.cow_small, R.drawable.giraffe2_transparent_small};
            int pointer = 0;

            backgrounds = new int[HARD_SIZE][HARD_SIZE];
            total_cards = HARD_SIZE * HARD_SIZE;

            for (int i = 0; i < HARD_SIZE; i++) {
                for (int j = 0; j < HARD_SIZE; j++) {
                    points.add(new Point(i, j));
                }
            }

            Random rand = new Random();
            for (int i = 0; i < (HARD_SIZE * HARD_SIZE) / 2; i++) {
                Point a = points.remove(rand.nextInt(points.size() - 1));
                Point b;
                int temp = points.size() - 1;
                if (temp == 0) {
                    b = points.remove();
                } else {
                    b = points.remove(rand.nextInt(points.size() - 1));
                }

                // these two will have the same animal
                backgrounds[a.x][a.y] = images[pointer];
                backgrounds[b.x][b.y] = images[pointer];

                // increment pointer
                if (pointer < images.length - 1) {
                    pointer++;
                } else if (pointer == images.length - 1) {
                    pointer = 0;
                }
            }
        }
    }
}
