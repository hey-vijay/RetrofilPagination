package vijay.app.mystake.restService;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import vijay.app.mystake.restService.response.AnswerResponse;

public interface StackAPI {

    @GET("2.2/answers")
    @Headers("Accept-type: application/json")
    public Call<AnswerResponse> getAnswers(
            @Query("page") int page,
            @Query("pagesize") int pageSize,
            @Query("site") String Site
    );
}
