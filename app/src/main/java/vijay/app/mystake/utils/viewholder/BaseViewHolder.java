package vijay.app.mystake.utils.viewholder;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    private int mCurrentPosition;
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public void onBind(int position, Context context) {
        mCurrentPosition = position;
        //clear();
    }
    public int getCurrentPosition() {
        return mCurrentPosition;
    }
}
