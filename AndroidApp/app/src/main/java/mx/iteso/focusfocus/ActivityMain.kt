package mx.iteso.focusfocus

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ActivityMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn = findViewById<Button>(R.id.button2) as Button;
        btn.setOnClickListener{
            val intent = Intent(this, ActivityButton::class.java);
            startActivity(intent);
            finish();

        }


    }


}
