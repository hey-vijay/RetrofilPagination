package vijay.app.mystake.utils.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vijay.app.mystake.R;
import vijay.app.mystake.model.Answer;
import vijay.app.mystake.model.Owner;
import vijay.app.mystake.utils.ClickListener;
import vijay.app.mystake.utils.viewholder.BaseViewHolder;

public class MyAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final String TAG = MyAdapter.class.getName();
    private ArrayList<Answer> answers;
    private Context context;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    private ClickListener mListener;

    public MyAdapter(List<Answer> list, Context context, ClickListener clickListener) {
        answers = new ArrayList<>(list);
        this.context = context;
        mListener = clickListener;
    }

    public void addItems(List<Answer> list) {
        answers.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new MyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_bar, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position, context);
    }

    public class MyViewHolder extends BaseViewHolder {

        CircleImageView profilePic;
        TextView tvUserName, tvUserId, tvQuestionId, tvReputation, tvAsked;
        ImageView imgIsAccepted;
        CardView rootLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rootLayout = itemView.findViewById(R.id.cv_parent);
            profilePic = itemView.findViewById(R.id.civProfilePic);
            tvUserName = itemView.findViewById(R.id.userName);
            tvUserId = itemView.findViewById(R.id.tvUserID);
            tvQuestionId = itemView.findViewById(R.id.tvQuestionID);
            tvReputation = itemView.findViewById(R.id.tvReputation);
            tvAsked = itemView.findViewById(R.id.tvDateCreated);
            imgIsAccepted = itemView.findViewById(R.id.imgIsAccepted);
        }


        public void onBind(int position, Context context) {
            super.onBind(position, context);
            Answer answer = answers.get(position);
            int reputation = answer.getOwner().reputation;
            if(reputation > 999) {
                reputation = reputation/1000;
                tvReputation.setText(reputation +"k");
            } else {
                tvReputation.setText(String.valueOf(reputation));
            }
            if(answer.getIs_accepted()) {
                imgIsAccepted.setVisibility(View.VISIBLE);
            }

            tvUserName.setText(answer.getOwner().display_name);
            tvAsked.setText(getTime(answer.getCreation_date()));

            String userId = "UserId: " + answer.getOwner().user_id;
            String questionId = "QuestionId: " + answer.getOwner().user_id;
            tvUserId.setText(userId);
            tvQuestionId.setText(questionId);

            rootLayout.setOnClickListener((v) -> mListener.onCardClick(answer));

            //set the profile picture
            Owner owner = answer.getOwner();
            Glide.with(context)
                    .load(owner.profile_image)
                    .placeholder(R.drawable.ic_stack_overflow)
                    .error(R.drawable.ic_stack_overflow)
                    .into(profilePic);
        }

        private String getTime(int creation_date) {
            long date = creation_date*1000L;
            DateFormat formatter = new SimpleDateFormat("E dd MMM, yy");

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date);
            return formatter.format(calendar.getTime()).toString();
        }
    }

    public static class ProgressHolder extends BaseViewHolder {
        ProgressBar progressBar;
        ProgressHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == answers.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    public Answer getItem(int position) {
        return answers.get(position);
    }

    public void clear() {
        answers.clear();
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
        Answer temp = new Answer();
        answers.add(temp);
        notifyItemInserted(answers.size() - 1);
    }
    public void removeLoading() {
        isLoaderVisible = false;
        int position = answers.size() - 1;
        Answer item = getItem(position);
        if (item != null) {
            answers.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public int getItemCount() {
        return answers == null ? 0 : answers.size();
    }

}
