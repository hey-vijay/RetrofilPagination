package vijay.app.mystake.restService.response;

import java.util.List;

import vijay.app.mystake.model.Answer;

public class AnswerResponse {
    public List<Answer> items;
    public Boolean has_more;
    public int backoff;
    public int quota_max;
    public int quota_remaining;
    public int error_id;

    @Override
    public String toString() {
        return "AnswerResponse{" +
                "items size=" + items.size() +
                ", has_more=" + has_more +
                ", backoff=" + backoff +
                ", quota_max=" + quota_max +
                ", quota_remaining=" + quota_remaining +
                ", error_id=" + error_id +
                '}';
    }
}
