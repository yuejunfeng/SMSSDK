package yuejunfeng.bawei.com.mysmssdk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class MainActivity extends AppCompatActivity {

    private EventHandler eventHandler;
    private TextView get_sms;
    private boolean boolShowInDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get_sms = (TextView) findViewById(R.id.get_sms);
        
        SMSSDK.setAskPermisionOnReadContact(boolShowInDialog);

        // 创建EventHandler对象
        // 处理你自己的逻辑
        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (data instanceof Throwable) {
                    Throwable throwable = (Throwable) data;
                    String msg = throwable.getMessage();

                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                } else {
                    // 处理你自己的逻辑
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        HashMap<String,Object> PhoneMap=(HashMap<String, Object>) data;
                        String country = (String) PhoneMap.get("country");
                        System.out.println("国家"+country);

                    }
                }
            }
        };
        get_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开注册界面
                RegisterPage registerPage = new RegisterPage();
                registerPage.setRegisterCallback(eventHandler);
                //显示注册的面板
                registerPage.show(MainActivity.this);
            }
        });

        // 注册监听器
        SMSSDK.registerEventHandler(eventHandler);
    }
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}
