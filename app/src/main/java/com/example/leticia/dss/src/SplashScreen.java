package com.example.leticia.dss.src;



import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import com.example.leticia.dss.R;

            public class SplashScreen extends AppCompatActivity {

                /** The thread to process splash screen events */
                private Thread mSplashThread;

                /** Called when the activity is first created. */
                @Override
                public void onCreate(Bundle savedInstanceState) {

                    super.onCreate(savedInstanceState);
                    //final Bundle extras = getIntent().getExtras();
                    // Splash screen view
                    setContentView(R.layout.splashscreen);

                    int SPLASH_TIME = 3000;
                    Handler HANDLER = new Handler();

                    // thread for displaying the SplashScreen
                    HANDLER.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);

                        }
                    }, SPLASH_TIME);
                }



            }
