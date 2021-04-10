package vijay.app.mystake.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vijay.app.mystake.restService.StackAPI;
import vijay.app.mystake.restService.response.AnswerResponse;
import vijay.app.mystake.utils.CallbackListener;
import vijay.app.mystake.utils.Const;
import vijay.app.mystake.utils.RetrofitBuilder;

public class MainViewModel extends ViewModel {
    private static String TAG = MainViewModel.class.getName();

    private Context context;
    private static Boolean isBackoffTimeEnable = true;

    public MainViewModel() {}

    public void setContext(Context context) {
        this.context = context;
    }

    public void fetchData(int fromPage, CallbackListener callbackListener) {
        //wait for 10 second before making another request
        if(!isBackedOff()) {
            Log.d(TAG, "fetchData: backed off time limit");
            callbackListener.onFailure("Too many request");
            return ;
        }
        int pageSize = 100;
        StackAPI stackAPI = RetrofitBuilder.getInstance(Const.BASE_URL).create(StackAPI.class);

        Call<AnswerResponse> call =  stackAPI.getAnswers(fromPage, pageSize, Const.SITE);
        Log.d(TAG, "fetchData: request " + call.request().toString());

        if(call != null) {
            call.enqueue(new Callback<AnswerResponse>() {
                @Override
                public void onResponse(Call<AnswerResponse> call, Response<AnswerResponse> response) {
                    if(!response.isSuccessful()) {
                        Log.d(TAG, "onResponseUnsuccessful: raw response " + response.raw());
                        Log.d(TAG, "onResponseUnsuccessful: responseBody " + response.body() +"\n"+ response.message());
                        if (response.errorBody() != null) {
                            Log.d(TAG, "onResponseUnsuccessful: error body" + response.errorBody().toString());
                        }
                        callbackListener.onFailure("No data found");
                        return ;
                    }

                    if (response.body() != null) {
                        AnswerResponse answers = response.body();
                        Log.d(TAG, "onResponse: " + answers.toString());
                        isBackoffTimeEnable = answers.backoff != 0;
                        callbackListener.onSuccess(answers);
                    }
                }

                @Override
                public void onFailure(Call<AnswerResponse> call, Throwable t) {
                    callbackListener.onFailure("onFailure: something IS wrong " + t.getMessage());
                }
            });
        }
    }

    /*calculate the time difference between two API request must be more than 10 seconds*/
    private boolean isBackedOff() {
        Log.d(TAG, "isBackedOff: " + isBackoffTimeEnable);
        if(isBackoffTimeEnable) {
            //check for previous request time
            SharedPreferences sharedpreferences = context.getSharedPreferences(Const.SHARED_PREF, Context.MODE_PRIVATE);
            long lastReqTime = sharedpreferences.getLong(Const.LAST_REQUEST_TIME, 0L);
            long currentTime = System.currentTimeMillis()/1000L;    //current time in second

            if(lastReqTime == 0) {
                sharedpreferences.edit().putLong(Const.LAST_REQUEST_TIME, currentTime).apply();
                return true;
            }

            //check TimeDifference
            Log.d(TAG, "isBackedOff: time difference= " + (currentTime - lastReqTime));
            if(currentTime - lastReqTime > 10) {
                sharedpreferences.edit().putLong(Const.LAST_REQUEST_TIME, currentTime).apply();
                return true;
            }
            return false;
        } else {
            return true;
        }
    }

}
