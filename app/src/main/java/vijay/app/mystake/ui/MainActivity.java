package vijay.app.mystake.ui;

import android.os.Bundle;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import vijay.app.mystake.R;
import vijay.app.mystake.model.Answer;
import vijay.app.mystake.model.Owner;
import vijay.app.mystake.restService.response.AnswerResponse;
import vijay.app.mystake.utils.CallbackListener;
import vijay.app.mystake.utils.ClickListener;
import vijay.app.mystake.utils.ConnectionManager;
import vijay.app.mystake.utils.PagingScrollListener;
import vijay.app.mystake.utils.adapter.MyAdapter;

import static vijay.app.mystake.utils.PagingScrollListener.PAGE_START;

public class MainActivity extends AppCompatActivity implements CallbackListener {
    private static String TAG = MainActivity.class.getName();

    private MainViewModel viewModel;

    private SwipeRefreshLayout refreshLayout;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ViewStub emptyStateLayout;
    private FloatingActionButton fab;
    private LinearLayoutManager layoutManager;
    private MyAdapter adapter;
    private ArrayList<Answer> answers;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        initView();
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.setContext(this);

        emptyStateLayout.setVisibility(View.VISIBLE);
        getData();
        refreshLayout.setOnRefreshListener(this::reset);
    }


    private void initView() {
        //initialise views and recyclerView here
        toolbar = findViewById(R.id.toolbar);
        refreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        fab = findViewById(R.id.fab);

        setSupportActionBar(toolbar);
        refreshLayout.setRefreshing(true);

        answers = new ArrayList<>();
        //answers = generateTempData();
        adapter = new MyAdapter(answers, this, clickListener);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener((view -> recyclerView.smoothScrollToPosition(0)));

        recyclerView.addOnScrollListener(new PagingScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                Log.d(TAG, "loadMoreItems: Load more items");
                isLoading = true;
                getData();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public void showScrollUp(Boolean value) {
                if(value) {
                    fab.setVisibility(View.VISIBLE);
                } else {
                    fab.setVisibility(View.GONE);
                }
            }
        });
    }

    private final ClickListener clickListener = answer -> Toast.makeText(MainActivity.this, answer.getOwner().display_name, Toast.LENGTH_SHORT).show();

    private void getData() {
        //check for the internet connection
        //if available then fetch the data
        Log.d(TAG, "getData: ");
        if (ConnectionManager.checkInternetConnection(this)) {
            viewModel.fetchData(currentPage, this);
            if (currentPage > 1) {
                isLoading = true;
            }
        } else {
            isLoading = false;
            refreshLayout.setRefreshing(false);
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
    }

    private void reset() {
        currentPage = PAGE_START;
        isLastPage = false;
        adapter.clear();
        getData();
    }

    @Override
    public void onSuccess(AnswerResponse answers) {
        Log.d(TAG, "onSuccess: size " + answers.items.size());
        //Log.d(TAG, "onSuccess: " + answers.items.toString());
        if (currentPage > 1 || currentPage != PAGE_START) {
            adapter.removeLoading();
        }
        isLastPage = !answers.has_more;
        currentPage++;
        isLoading = false;
        if (answers.quota_remaining == 0) isLastPage = true;
        emptyStateLayout.setVisibility(View.GONE);
        refreshLayout.setRefreshing(false);
        adapter.addItems(answers.items);
        // check weather we reached the last page or not
        if (!isLastPage) {
            adapter.addLoading();
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        if(adapter.getItemCount() > 0) {
            Toast.makeText(this, R.string.too_many_request, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.exhaust_limit, Toast.LENGTH_SHORT).show();
        }

        if (currentPage > 1) {
            adapter.removeLoading();
        }
        isLoading = true;
        refreshLayout.setRefreshing(false);
    }

    /*temporary data for testing */
    private ArrayList<Answer> generateTempData() {
        ArrayList<Answer> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            String profilePic = "https://lh3.googleusercontent.com/-TKUnmqj2V1U/AAAAAAAAAAI/AAAAAAAAAzI/uu8lNMGfCjI/photo.jpg?sz=128";
            Owner owner = new Owner(0, 0, "", profilePic, "newUser", "");
            Answer answer = new Answer(owner, i / 2 == 0, 0, 0, 0, 0, 0, 0, "");
            list.add(answer);
        }
        return list;
    }

}