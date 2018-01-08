use crypto;

drop table if exists CoinSentiment cascade;

create table CoinSentiment (
	Id bigint not null auto_increment,
    Symbol varchar(50) not null,
    TwitterChange_24h decimal(10,2),
    TwitterVolume_24h bigint,
    RedditChange_24h decimal(10,2),
    RedditVolume_24h bigint,
    SocialVolumeChange_24h decimal(10,2),
    SocialVolume_24h bigint,
    Timestamp datetime,
    BatchNum int not null,
    primary key (Id)
);

create index ix_CoinSentiment_Symbol on CoinSentiment (Symbol);
create index ix_CoinSentiment_BatchNum on CoinSentiment (BatchNum);