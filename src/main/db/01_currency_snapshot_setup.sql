create database if not exists crypto;

use crypto;

drop table if exists currency_snapshot cascade;

create table currency_snapshot (
	Id int not null auto_increment,
    Name varchar(50) not null,
    Symbol varchar(10) not null,
    MarketCap long not null,
    Price decimal,
    CirculatingSupply long,
    Volume_24h long,
    PercentChange_1h decimal,
    PercentChange_24h decimal,
    PercentChange_7d decimal,
    Timestamp datetime,
    primary key (Id)
);