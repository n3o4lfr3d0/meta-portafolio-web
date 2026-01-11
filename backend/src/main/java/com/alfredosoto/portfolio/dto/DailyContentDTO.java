package com.alfredosoto.portfolio.dto;

public class DailyContentDTO {
    private String date;
    private String tip;
    private String quote;
    private String quoteAuthor;
    private String joke;

    public DailyContentDTO() {}

    public DailyContentDTO(String date, String tip, String quote, String quoteAuthor, String joke) {
        this.date = date;
        this.tip = tip;
        this.quote = quote;
        this.quoteAuthor = quoteAuthor;
        this.joke = joke;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTip() { return tip; }
    public void setTip(String tip) { this.tip = tip; }

    public String getQuote() { return quote; }
    public void setQuote(String quote) { this.quote = quote; }

    public String getQuoteAuthor() { return quoteAuthor; }
    public void setQuoteAuthor(String quoteAuthor) { this.quoteAuthor = quoteAuthor; }

    public String getJoke() { return joke; }
    public void setJoke(String joke) { this.joke = joke; }
}
