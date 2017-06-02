package one.marshangeriksen.loloaldrin.myapplication.objectModels;

public class HighScore {
    private int _id_highscore;
    private String name_highscore;
    private int passnumber_highscore;

    public HighScore(int _id_highscore, String name_highscore, int passnumber_highscore) {
        this._id_highscore = _id_highscore;
        this.name_highscore = name_highscore;
        this.passnumber_highscore = passnumber_highscore;
    }

    public HighScore() {
    }

    public HighScore(String name_highscore) {
        this.name_highscore = name_highscore;
    }

    public int get_id_highscore() {
        return _id_highscore;
    }

    public void set_id_highscore(int _id_highscore) {
        this._id_highscore = _id_highscore;
    }

    public String getName_highscore() {
        return name_highscore;
    }

    public void setName_highscore(String name_highscore) {
        this.name_highscore = name_highscore;
    }

    public int getPassnumber_highscore() {
        return passnumber_highscore;
    }

    public void setPassnumber_highscore(int passnumber_highscore) {
        this.passnumber_highscore = passnumber_highscore;
    }
}
