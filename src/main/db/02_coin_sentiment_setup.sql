use crypto;

drop table if exists CoinSentiment cascade;

create table CoinSentiment (
	Id int not null auto_increment,
    Symbol varchar(50) not null,
    TwitterChange_24h decimal(10,2),
    TwitterVolume_24h long,
    RedditChange_24h decimal(10,2),
    RedditVolume_24h long,
    SocialVolumeChange_24h decimal(10,2),
    SocialVolume_24h long,
    Timestamp long,
    BatchNum int not null,
    primary key (Id)
);

create index ix_CoinSentiment_Symbol on CoinSentiment (Symbol);
create index ix_CoinSentiment_BatchNum on CoinSentiment (BatchNum);