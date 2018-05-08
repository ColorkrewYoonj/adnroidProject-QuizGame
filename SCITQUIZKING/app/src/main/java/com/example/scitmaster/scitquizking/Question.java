package com.example.scitmaster.scitquizking;

public class Question {
    private String num;
    private String answer;
    private String level;
    private String imgPath;
    private String solv;

    public String getNum(){
        return num;
    }
    public void setNum(String num){
        this.num=num;
    }
    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public String getImgPath() {
        return imgPath;
    }
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
    public String getSolv(){
        return solv;
    }
    public void setSolv(String solv){
        this.solv=solv;
    }

    public Question(){
    }

    public Question(String num, String answer, String level, String imgPath, String solv) {
        this.num = num;
        this.answer = answer;
        this.level = level;
        this.imgPath = imgPath;
        this.solv = solv;
    }



}
