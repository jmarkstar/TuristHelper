package com.jmarkstar.turisthelper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.jmarkstar.turisthelper.R;
import com.jmarkstar.turisthelper.extensions.FoursquareDialog;
import com.jmarkstar.turisthelper.helpers.RetrofitHelper;
import com.jmarkstar.turisthelper.models.Session;
import com.jmarkstar.turisthelper.services.UserService;
import com.jmarkstar.turisthelper.utils.LogUtils;
import butterknife.OnClick;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author jmarkstar
 * */
public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    private FoursquareDialog.FoursquareDialogListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        listener = new FoursquareDialog.FoursquareDialogListener() {
            @Override
            public void onComplete(String accessToken) {
                LogUtils.info(TAG, "accessToken: "+accessToken);

                UserService userService = RetrofitHelper.getRetrofitInstance().create(UserService.class);

                final Call<Session> requestGetInfo = userService.getUserInformation();

                LogUtils.info(TAG, "Url="+requestGetInfo.request().url().toString());
                requestGetInfo.enqueue(new Callback<Session>() {
                    @Override
                    public void onResponse(Call<Session> call, Response<Session> response) {
                        LogUtils.info(TAG, "code="+response.code());
                        LogUtils.info(TAG, "user"+response.body().toString());
                        final Session userInfo = response.body();
                        realm = Realm.getDefaultInstance();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Session session = realm.where(Session.class).findFirst();
                                session.setFourSquareId(userInfo.getFourSquareId());
                                session.setFullName(userInfo.getFullName());
                                session.setEmail(userInfo.getEmail());
                                session.setPhoto(userInfo.getPhoto());
                                LogUtils.info(TAG, "The user information was saved on the realm db");
                            }
                        });
                        realm.close();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.startActivity(intent);
                        LoginActivity.this.finish();
                    }

                    @Override
                    public void onFailure(Call<Session> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, R.string.login_error_user_information, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                Toast.makeText(LoginActivity.this, R.string.login_error_authorization_failed, Toast.LENGTH_LONG).show();
            }
        };
    }

    @OnClick(R.id.btn_connect_foursquare)
    public void connect(){
        FoursquareDialog mFoursquareDialog = new FoursquareDialog(LoginActivity.this, listener);
        mFoursquareDialog.show();
    }
}
