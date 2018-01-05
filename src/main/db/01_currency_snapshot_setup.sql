create database if not exists crypto;

use crypto;

drop table if exists currency_snapshot cascade;

create table currency_snapshot (
	Id int not null auto_increment,
    Name varchar(50) not null,
    Symbol varchar(10) not null,
    Rank int not null,
    MarketCap long not null,
    Price decimal(10,2) not null,
    CirculatingSupply long,
    Volume_24h long,
    PercentChange_1h decimal(10,2),
    PercentChange_24h decimal(10,2),
    PercentChange_7d decimal(10,2),
    Timestamp datetime,
    primary key (Id)
);