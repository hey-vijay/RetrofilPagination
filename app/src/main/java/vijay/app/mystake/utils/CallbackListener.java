package vijay.app.mystake.utils;

import vijay.app.mystake.restService.response.AnswerResponse;

public interface CallbackListener {
    void onSuccess(AnswerResponse answers);
    void onFailure(String errorMessage);
}
