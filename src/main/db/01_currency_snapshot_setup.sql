create database if not exists crypto;

use crypto;

drop table if exists currency_snapshot cascade;

create table currency_snapshot (
	Id bigint not null auto_increment,
    Name varchar(50) not null,
    Symbol varchar(10) not null,
    Rank int not null,
    MarketCap bigint not null,
    Price decimal(10,2) not null,
    CirculatingSupply bigint,
    Volume_24h bigint,
    PercentChange_1h decimal(10,2),
    PercentChange_24h decimal(10,2),
    PercentChange_7d decimal(10,2),
    Timestamp datetime,
    BatchNum int not null,
    primary key (Id)
);

create index ix_currency_name on currency_snapshot (name);
create index ix_currency_symbol on currency_snapshot (symbol);