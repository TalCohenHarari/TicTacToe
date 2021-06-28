package com.example.ticactoe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ImageButton[][] matrix = new ImageButton[3][3];
    int[][] check=new int[3][3];
    ImageView[] marks=new ImageView[8];
    Button reset;
    TextView player;
    ImageView pause;
    String x_o = "x"; //Define x as 1 and o as 2
    int counter =0;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();
        //Background music:
        View view= null;
        PlayBackgroundSound(view);
        //Icon play and pause music:
        i=0;
        pause = findViewById(R.id.pause);
        pause.setBackgroundResource(R.drawable.pause_icon);
        pause.setOnClickListener(v->{
            BackgroundSoundService.mediaPlayer.pause();
            if(i==0){
                pause.setBackgroundResource(R.drawable.play_icon);
                i=1;
            }
            else{
                BackgroundSoundService.mediaPlayer.start();
                pause.setBackgroundResource(R.drawable.pause_icon);
                i=0;
            }
        });
        //The buttons for the X or O:
        matrix[0][0]=findViewById(R.id.matrix_0_0);
        matrix[0][1]=findViewById(R.id.matrix_0_1);
        matrix[0][2]=findViewById(R.id.matrix_0_2);
        matrix[1][0]=findViewById(R.id.matrix_1_0);
        matrix[1][1]= findViewById(R.id.matrix_1_1);
        matrix[1][2]=findViewById(R.id.matrix_1_2);
        matrix[2][0]=findViewById(R.id.matrix_2_0);
        matrix[2][1]=findViewById(R.id.matrix_2_1);
        matrix[2][2]=findViewById(R.id.matrix_2_2);
        //The Player's turn and if someone won or lost:
        player = findViewById(R.id.player);
        //Reset the game:
        reset = findViewById(R.id.reset);

        // Win mark (top row): -
        marks[0]=findViewById(R.id.top_row);
        marks[0].setVisibility(View.INVISIBLE);
        // Win mark (mid row): -
        marks[1]=findViewById(R.id.mid_row);
        marks[1].setVisibility(View.INVISIBLE);
        // Win mark (bottom row): -
        marks[2]=findViewById(R.id.bottom_row);
        marks[2].setVisibility(View.INVISIBLE);
        // Win mark (left col): |
        marks[3]=findViewById(R.id.left_col);
        marks[3].setVisibility(View.INVISIBLE);
        // Win mark (mid col): |
        marks[4]=findViewById(R.id.mid_col);
        marks[4].setVisibility(View.INVISIBLE);
        // Win mark (right col): |
        marks[5]=findViewById(R.id.right_col);
        marks[5].setVisibility(View.INVISIBLE);
        // Win mark: \
        marks[6]=findViewById(R.id.top_left);
        marks[6].setVisibility(View.INVISIBLE);
        // Win mark: /
        marks[7]=findViewById(R.id.top_right);
        marks[7].setVisibility(View.INVISIBLE);
        //A matrix for determining victory:
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                check[i][j]=0;

        // Get instance of Vibrator from current Context
        Vibrator vibration = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // The buttons listeners:
        matrix[0][0].setOnClickListener(v->{if(matrix[0][0].isEnabled()) play(matrix[0][0],0,0); vibration.vibrate(150);});
        matrix[0][1].setOnClickListener(v->{if(matrix[0][1].isEnabled()) play(matrix[0][1],0,1); vibration.vibrate(150);});
        matrix[0][2].setOnClickListener(v->{if(matrix[0][2].isEnabled()) play(matrix[0][2],0,2); vibration.vibrate(150);});
        matrix[1][0].setOnClickListener(v->{if(matrix[1][0].isEnabled()) play(matrix[1][0],1,0); vibration.vibrate(150);});
        matrix[1][1].setOnClickListener(v->{if(matrix[1][1].isEnabled()) play(matrix[1][1],1,1); vibration.vibrate(150);});
        matrix[1][2].setOnClickListener(v->{if(matrix[1][2].isEnabled()) play(matrix[1][2],1,2); vibration.vibrate(150);});
        matrix[2][0].setOnClickListener(v->{if(matrix[2][0].isEnabled()) play(matrix[2][0],2,0); vibration.vibrate(150);});
        matrix[2][1].setOnClickListener(v->{if(matrix[2][1].isEnabled()) play(matrix[2][1],2,1); vibration.vibrate(150);});
        matrix[2][2].setOnClickListener(v->{if(matrix[2][2].isEnabled()) play(matrix[2][2],2,2); vibration.vibrate(150);});
        // he reset listener button:
        reset.setOnClickListener(v->reset());
    }

    public void play(ImageButton imB,int i,int j)
    {
        if(x_o.equals("x")) {
            x_o="o";
            check[i][j]=1;
            imB.setImageResource(R.drawable.x3);
            player.setText("O  Play");
        }
        else {
            check[i][j]=2;
            x_o="x";
            imB.setImageResource(R.drawable.o3);
            player.setText("X  Play");
        }
        imB.setEnabled(false);
        counter++;

        boolean res=false;
        if(counter>=5)
             res = isWin();
        if(counter==9 && !res)
            player.setText("No  Winner");
    }

    public void reset()
    {
        for(int i=0;i<3;i++)
            for(int j=0; j<3; j++) {
                matrix[i][j].setImageResource(R.drawable.empty);
                matrix[i][j].setEnabled(true);
            }

        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                check[i][j]=0;
        for(int i=0;i<8;i++)
             marks[i].setVisibility(View.INVISIBLE);

        player.setText("X  Play");
        x_o="x";
        counter=0;
    }

    public boolean isWin()
    {
        Boolean[] a=new Boolean[8];
        if( (a[0]=(mark(0,0,1) && mark(0,1,1) && mark(0,2,1))==true) ||
            (a[1]=(mark(1,0,1) && mark(1,1,1) && mark(1,2,1))==true) ||
            (a[2]=(mark(2,0,1) && mark(2,1,1) && mark(2,2,1))==true) ||
            (a[3]=(mark(0,0,1) && mark(1,0,1) && mark(2,0,1))==true) ||
            (a[4]=(mark(0,1,1) && mark(1,1,1) && mark(2,1,1))==true) ||
            (a[5]=(mark(0,2,1) && mark(1,2,1) && mark(2,2,1))==true) ||
            (a[6]=(mark(0,0,1) && mark(1,1,1) && mark(2,2,1))==true) ||
            (a[7]=(mark(0,2,1) && mark(1,1,1) && mark(2,0,1))==true))
        {
            player.setText("X  won");
            for(int i=0;i<3;i++)
                for(int j=0; j<3; j++)
                    matrix[i][j].setEnabled(false);
             for(int i=0;i<8;i++)
            {
              if(a[i]==true)
              {
                  marks[i].setVisibility(View.VISIBLE);
                  return true;
              }
            }
        }
        if( (a[0]=(mark(0,0,2) && mark(0,1,2) && mark(0,2,2))==true) ||
                (a[1]=(mark(1,0,2) && mark(1,1,2) && mark(1,2,2))==true) ||
                (a[2]=(mark(2,0,2) && mark(2,1,2) && mark(2,2,2))==true) ||
                (a[3]=(mark(0,0,2) && mark(1,0,2) && mark(2,0,2))==true) ||
                (a[4]=(mark(0,1,2) && mark(1,1,2) && mark(2,1,2))==true) ||
                (a[5]=(mark(0,2,2) && mark(1,2,2) && mark(2,2,2))==true) ||
                (a[6]=(mark(0,0,2) && mark(1,1,2) && mark(2,2,2))==true) ||
                (a[7]=(mark(0,2,2) && mark(1,1,2) && mark(2,0,2))==true))
        {
            player.setText("O  Won");

            for(int i=0;i<3;i++)
                for(int j=0; j<3; j++)
                    matrix[i][j].setEnabled(false);

            for(int i=0;i<8;i++)
            {
                if(a[i]==true)
                {
                    marks[i].setVisibility(View.VISIBLE);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean mark(int i, int j, int number)
    {
        return check[i][j]==number;
    }

    public void PlayBackgroundSound(View view) {
        Intent intent = new Intent(this, BackgroundSoundService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if( BackgroundSoundService.mediaPlayer!=null) {
            BackgroundSoundService.mediaPlayer.start();
            if(i==1) {
                pause.setBackgroundResource(R.drawable.pause_icon);
                i = 0;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if( BackgroundSoundService.mediaPlayer!=null) {
            if(i==0) {
                BackgroundSoundService.mediaPlayer.pause();
                pause.setBackgroundResource(R.drawable.play_icon);
                i = 1;
            }
        }
    }
}