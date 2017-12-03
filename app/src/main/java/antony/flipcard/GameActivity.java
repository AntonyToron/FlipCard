package antony.flipcard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private static final int NORMAL_SIZE = 4;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_game_normal);

        Intent intent = getIntent();
        String difficulty = intent.getStringExtra("difficulty");

        System.out.println("Difficulty: " + difficulty);

        setLayout(difficulty);
        initializeText();

        setButtonBackgrounds(difficulty);
        initializeBoard(difficulty);
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

            for (int i = 1; i < 5; i++) {
                for (int j = 1; j < 5; j++) {
                    final int x = i - 1; // for passing into inner class
                    final int y = j - 1;

                    String buttonId = "normal_row" + i + "_" + j;
                    int resId = getResources().getIdentifier(buttonId, "id", getPackageName());
                    cards[i - 1][j - 1] = (ImageView) findViewById(resId);

                    initializeCard(cards[i - 1][j - 1], x, y);
                }
            }
        } else if (difficulty.equals("medium")) {
            cards = new ImageView[NORMAL_SIZE][NORMAL_SIZE];

            for (int i = 1; i < 5; i++) {
                for (int j = 1; j < 5; j++) {
                    final int x = i - 1; // for passing into inner class
                    final int y = j - 1;

                    String buttonId = "normal_row" + i + "_" + j;
                    int resId = getResources().getIdentifier(buttonId, "id", getPackageName());
                    cards[i - 1][j - 1] = (ImageView) findViewById(resId);

                    initializeCard(cards[i - 1][j - 1], x, y);
                }
            }
        } else {
            cards = new ImageView[NORMAL_SIZE][NORMAL_SIZE];

            for (int i = 1; i < 5; i++) {
                for (int j = 1; j < 5; j++) {
                    final int x = i - 1; // for passing into inner class
                    final int y = j - 1;

                    String buttonId = "normal_row" + i + "_" + j;
                    int resId = getResources().getIdentifier(buttonId, "id", getPackageName());
                    cards[i - 1][j - 1] = (ImageView) findViewById(resId);

                    initializeCard(cards[i - 1][j - 1], x, y);
                }
            }
        }
    }

    private void initializeCard(ImageView image, final int x, final int y) {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardClicked(x, y);
            }
        });

        // set initial background image
        //b.setText("?");
        //image.setBackgroundResource(R.drawable.question_mark_small);

        //image.setImageAlpha(127);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.question_mark_small);
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
        }
        else if (cardFlipped == 1) {
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
            }
            else {
                // animate card flip
                // can be done via : https://developer.android.com/training/animation/cardflip.html

                animateCardFlip();
            }

            // set cardFlipped to 0 again
            cardFlipped = 0;
        }
        else {
            // Shouldn't get here
            System.out.println("Shouldn't get here yet.");
        }

    }

    private void animateCardFlip() {
        // wait a few seconds, and then flip over the cards again
        final Runnable resetCards = new Runnable() {
            @Override
            public void run() {
                cards[i_card0][j_card0].setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.question_mark_small));
                cards[i_card1][j_card1].setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.question_mark_small));

                // change player turn
                changePlayer();

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
                }
                else {
                    player2_score++;
                    player2_points.setText("Player 2 Points: " + player1_score);
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
        i.putExtra("score", player1_score > player2_score ? player1_score : player2_score );
        i.putExtra("winner", player1_score > player2_score ? 1 : 2);

        startActivity(i);
    }

    private void changePlayer() {
        if (player == 1) {
            player = 2;
            player2_turn.setText("Your Turn!");
            player1_turn.setText("");
        }
        else {
            player = 1;
            player1_turn.setText("Your Turn!");
            player2_turn.setText("");
        }
        System.out.println("Current rotation: " + cards[0][0].getRotation());
        // rotate all of the cards 180 degrees for the other player
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cards[i][j].setPivotX(cards[i][j].getWidth() / 2);
                cards[i][j].setPivotY(cards[i][j].getHeight() /2);
                int angle = 180;

                cards[i][j].setRotation(cards[i][j].getRotation() + angle);
            }
        }

    }

    private static class Point {
        public int x;
        public int y;

        public Point (int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private void initializeBoard(String difficulty) {
        LinkedList<Point> points = new LinkedList<Point>();
        int[] images = {R.drawable.elephant_small, R.drawable.monkey_small, R.drawable.cow_small, R.drawable.giraffe2_transparent_small};
        int pointer = 0;

        if (difficulty.equals("normal")) {
            backgrounds = new int[NORMAL_SIZE][NORMAL_SIZE];
            total_cards = NORMAL_SIZE * NORMAL_SIZE;

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    points.add(new Point(i, j));
                }
            }

            Random rand = new Random();
            for (int i = 0; i < NORMAL_SIZE * NORMAL_SIZE / 2; i++) {
                Point a = points.remove(rand.nextInt(points.size() - 1));
                Point b;
                int temp = points.size() - 1;
                if (temp == 0) {
                    b = points.remove();
                }
                else {
                    b = points.remove(rand.nextInt(points.size() - 1));
                }

                // these two will have the same animal
                backgrounds[a.x][a.y] = images[pointer];
                backgrounds[b.x][b.y] = images[pointer];

                // increment pointer
                if (pointer < images.length - 1) {
                    pointer++;
                }
                else if (pointer == images.length - 1) {
                    pointer = 0;
                }
            }
        }
        else {
            backgrounds = new int[NORMAL_SIZE][NORMAL_SIZE];
            total_cards = NORMAL_SIZE * NORMAL_SIZE;

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    points.add(new Point(i, j));
                }
            }

            Random rand = new Random();
            for (int i = 0; i < NORMAL_SIZE * NORMAL_SIZE / 2; i++) {
                Point a = points.remove(rand.nextInt(points.size() - 1));
                Point b;
                int temp = points.size() - 1;
                if (temp == 0) {
                    b = points.remove();
                }
                else {
                    b = points.remove(rand.nextInt(points.size() - 1));
                }

                // these two will have the same animal
                backgrounds[a.x][a.y] = images[pointer];
                backgrounds[b.x][b.y] = images[pointer];

                // increment pointer
                if (pointer < images.length - 1) {
                    pointer++;
                }
                else if (pointer == images.length - 1) {
                    pointer = 0;
                }
            }
        }
    }
}
