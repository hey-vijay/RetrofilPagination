package vijay.app.mystake.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionManager {
    public static boolean checkInternetConnection(Context context){
        ConnectivityManager manager =(ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info = (NetworkInfo) manager.getActiveNetworkInfo();
        if(info != null){
            return info.isConnected();
        }else{
            return false;
        }
    }
}
