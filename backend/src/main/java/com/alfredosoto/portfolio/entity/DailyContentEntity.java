package com.alfredosoto.portfolio.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class DailyContentEntity {
    private String date; // Format: YYYY-MM-DD
    private String tip;
    private String quote;
    private String quoteAuthor;
    private String joke;

    public DailyContentEntity() {
        // Default constructor required by DynamoDB Enhanced Client for object instantiation during deserialization.
    }

    @DynamoDbPartitionKey
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
