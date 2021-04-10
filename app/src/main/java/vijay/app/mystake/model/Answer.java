package vijay.app.mystake.model;

public class Answer {

    private Owner owner;
    private boolean is_accepted;
    private int score;
    private int last_activity_date;
    private int last_edit_date;
    private int creation_date;
    private int answer_id;
    private int question_id;
    private String content_license;

    public Answer() {}

    public Answer(Owner owner, boolean is_accepted, int score, int last_activity_date,
                  int last_edit_date, int creation_date, int answer_id,
                  int question_id, String content_license) {
        this.owner = owner;
        this.is_accepted = is_accepted;
        this.score = score;
        this.last_activity_date = last_activity_date;
        this.last_edit_date = last_edit_date;
        this.creation_date = creation_date;
        this.answer_id = answer_id;
        this.question_id = question_id;
        this.content_license = content_license;
    }

    public Owner getOwner() {
        return owner;
    }

    public boolean getIs_accepted() {
        return is_accepted;
    }

    public int getScore() {
        return score;
    }

    public int getLast_activity_date() {
        return last_activity_date;
    }

    public int getLast_edit_date() {
        return last_edit_date;
    }

    public int getCreation_date() {
        return creation_date;
    }

    public int getAnswer_id() {
        return answer_id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public String getContent_license() {
        return content_license;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "owner=" + owner +
                ", is_accepted=" + is_accepted +
                ", score=" + score +
                ", last_activity_date=" + last_activity_date +
                ", last_edit_date=" + last_edit_date +
                ", creation_date=" + creation_date +
                ", answer_id=" + answer_id +
                ", question_id=" + question_id +
                ", content_license='" + content_license + '\'' +
                '}';
    }
}
