use crypto;

alter table CoinSentiment add column Sentiment_24h decimal(10,2);
alter table CoinSentiment add column SentimentChange_24h decimal(10,2);