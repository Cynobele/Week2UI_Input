package uk.ac.abertay.week2ui_input;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener{

    float[] hsv = new float[3];
    View root_layout;
    Button touch_button;
    boolean background_changed = false;
    String BACKGROUND_CHANGED = "bg_key"; //assign non-null values to keys
    String H = "hue_key";
    String S = "saturation_key";
    String V = "value_key";
    String BUTTON_COLOUR = "btncolour_key";
    String BUTTON_TEXT = "btntext_key";
    int btn_colour; //colour flag


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) { //runs when activity is created

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        touch_button = findViewById(R.id.touch_button);
        Button left = findViewById(R.id.button_left);
        Button right = findViewById(R.id.button_right);

        root_layout = findViewById(R.id.root_layout);
        root_layout.setOnTouchListener(this);

        left.setOnClickListener(this);
        right.setOnClickListener(this);

        if(savedInstanceState!=null)
        {
            //unpack bundle

            hsv[0] = savedInstanceState.getFloat(H);
            hsv[1] = savedInstanceState.getFloat(S);
            hsv[2] = savedInstanceState.getFloat(V);
            background_changed = savedInstanceState.getBoolean(BACKGROUND_CHANGED);
            root_layout.setBackgroundColor(Color.HSVToColor(hsv));

            int x = 4;
            switch (savedInstanceState.getInt(BUTTON_COLOUR)){
                case 0:
                    x = Color.YELLOW;
                     break;
                case 1:
                    x=Color.RED;
                    break;
                case 2:
                    x = Color.GREEN;
                    break;
                case 4:
                    x = Color.GRAY;
                    break;
            }
            touch_button.setBackgroundColor(x);
            touch_button.setText(savedInstanceState.getCharSequence(BUTTON_TEXT));

        }else{

            //set required defaults

            hsv[0] = 0.0f;//hue
            hsv[1] = 0.0f;//saturation
            hsv[2] = 1.0f;//value
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){ //saves the state before it is destroyed

        //pack items into bundle for recreation
        outState.putFloat(H , hsv[0]); //hue, saturation, value : for layout background
        outState.putFloat(S, hsv[1]);
        outState.putFloat(V, hsv[2]);

        outState.putBoolean(BACKGROUND_CHANGED, background_changed);//flag signalling if background is default

        int colour = getBtn_colour();//background of btn
        outState.putInt(BUTTON_COLOUR, colour);

        outState.putCharSequence(BUTTON_TEXT, touch_button.getText());

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        switch(keyCode){
            case KeyEvent.KEYCODE_VOLUME_UP:

                if(hsv[2] < 1.0f)
                {hsv[2] += 0.1f;}
                root_layout.setBackgroundColor(Color.HSVToColor(hsv));
                return false;

            case KeyEvent.KEYCODE_VOLUME_DOWN:

                if(hsv[2] > 0.1f)
                {hsv[2] -= 0.1f;}
                root_layout.setBackgroundColor(Color.HSVToColor(hsv));
                return false;

            default:
                return false;
        }
    }

    @Override
    public void onClick(View view) { //buttons being tapped will trigger this
        switch (view.getId()){
            case R.id.button_left:
                Toast.makeText(getApplicationContext(),"Left button clicked!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_right:
                Toast.makeText(getApplicationContext(),"Right button clicked!",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event){ //the background being tapped will trigger this

        touch_button = findViewById(R.id.touch_button);

        if(view.getId() == R.id.root_layout) {
            switch (event.getAction()) {
                //region
                case (MotionEvent.ACTION_DOWN)://on tap
                    Toast.makeText(getApplicationContext(), "Touch detected!", Toast.LENGTH_SHORT).show();
                    touch_button.setText("DOWN");
                    touch_button.setBackgroundColor(Color.RED);
                    setBtn_colour(Color.RED);
                    return true;

                case (MotionEvent.ACTION_UP)://on tap release
                    Toast.makeText(getApplicationContext(), "Touch released!", Toast.LENGTH_SHORT).show();
                    touch_button.setText("UP");
                    touch_button.setBackgroundColor(Color.GREEN);
                    setBtn_colour(Color.GREEN);
                    return true;
                //endregion
                case (MotionEvent.ACTION_MOVE)://moving finger across screen
                    changeBackgroundColour(event);
                    touch_button.setText("MOVING");
                    touch_button.setBackgroundColor(Color.YELLOW);
                    setBtn_colour(Color.YELLOW);
                    return true;
                default:
                    return super.onTouchEvent(event);
            }
        }else { return false; }
    }

    public int getBtn_colour() {
        return btn_colour;
    }
    public void setBtn_colour(int x){
        switch(x){
            case Color.YELLOW:
                this.btn_colour = 0;
            case Color.RED:
                this.btn_colour = 1;
            case Color.GREEN:
                this.btn_colour = 2;
            default:
                break;
        }
    }

    public void changeBackgroundColour(MotionEvent event) {
        if(event!=null) {
            float eventX = event.getX();
            float eventY = event.getY();
            float height = root_layout.getHeight(); // make sure the ref is declared and initialised (this is a reference to your root layout)
            float width = root_layout.getWidth();
            hsv[0] = eventY / height * 360; // (0 to 360)
            hsv[1] = eventX / width + 0.1f; // (0.1 to 1)
        }
        background_changed = true;
        root_layout.setBackgroundColor(Color.HSVToColor(hsv));

        //last_colour[0] = hsv[0]; //save last colour used
        //last_colour[1] = hsv[1];
        //last_colour[2] = hsv[2];
    }
}
