package vijay.app.mystake.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PagingScrollListener extends RecyclerView.OnScrollListener {
    public static final int PAGE_START = 1;
    private static final String TAG = "PagingScrollListener";
    @NonNull
    private final LinearLayoutManager layoutManager;

    private static final int PAGE_SIZE = 10;

    public PagingScrollListener(@NonNull LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        Log.d(TAG, "onScrolled: isLastPage" + isLastPage());
        Log.d(TAG, "onScrolled: isLoading" + isLoading());
        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE) {
                loadMoreItems();
            }
        }

        showScrollUp(firstVisibleItemPosition != 0);
    }
    protected abstract void loadMoreItems();
    public abstract boolean isLastPage();
    public abstract void showScrollUp(Boolean value);
    public abstract boolean isLoading();
}
