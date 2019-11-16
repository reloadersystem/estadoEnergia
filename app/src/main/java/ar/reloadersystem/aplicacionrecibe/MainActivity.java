package ar.reloadersystem.aplicacionrecibe;

/**
 * Created by Reloader
 **/
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvBroadCast;
    private IntentFilter chargingIntentFilter;
    private ChargedBroadCastReceiver chargingBroadCastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Estado de Carga..");
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {

        tvBroadCast = findViewById(R.id.tvBroadCast);

        chargingIntentFilter = new IntentFilter();
        chargingIntentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        chargingIntentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        chargingBroadCastReceiver = new ChargedBroadCastReceiver();
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        showChargind(isCharging);
    }

    private class ChargedBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            boolean isCharging = action.equals(Intent.ACTION_POWER_CONNECTED);
            showChargind(isCharging);
        }
    }

    private void showChargind(boolean isCharging) {

        if (isCharging) {
            tvBroadCast.setText("Conectado: Cargando..");
            tvBroadCast.setTextColor(Color.parseColor("#0000FF"));
        } else {
            tvBroadCast.setText("Cable Desconectado..");
            tvBroadCast.setTextColor(Color.parseColor("#FF0000"));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(chargingBroadCastReceiver, chargingIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(chargingBroadCastReceiver);
    }
}
