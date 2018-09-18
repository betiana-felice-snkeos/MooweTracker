package tracker.moowe.de.moowe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeText extends AppCompatActivity {
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_text);

        button = (Button) findViewById(R.id.buttonIntro);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTrackerButtonOff();

            }
        });
    }
    public void openTrackerButtonOff() {
        Intent intent = new Intent(this, TrackerButtonOff.class);
        startActivity(intent);
    }
}
