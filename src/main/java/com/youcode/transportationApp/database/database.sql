CREATE TYPE transportation_type as ENUM ('AIR' , 'TRAIN' , 'BUS');
CREATE TYPE partnership_status as ENUM ('ACTIVE' , 'INACTIVE' , 'SUSPENDED');

CREATE TABLE IF NOT EXISTS partners (
    partnerId VARCHAR(255) PRIMARY KEY,
    companyName VARCHAR(255),
    commercialContact VARCHAR(255),
    transportationType transportation_type,
    geographicZone VARCHAR(255),
    specialConditions TEXT,
    partnershipStatus partnership_status,
    creationDate TIMESTAMP
);

CREATE TYPE contract_status as ENUM ('ONGOING' , 'ENDED' , 'SUSPENDED');

CREATE TABLE IF NOT EXISTS contracts (
    contractId VARCHAR(255) PRIMARY KEY,
    staringDate TIMESTAMP,
    endDate  TIMESTAMP,
    specialRate DOUBLE PRECISION,
    agreementConditions VARCHAR(255),
    renewable BOOLEAN,
    contractStatus contract_status
);

CREATE TYPE offer_status as ENUM ('ACTIVE' , 'SUSPENDED' , 'EXPIRED');
CREATE TYPE discount_type as ENUM ('FIX_AMOUNT' , 'PERCENTAGE');

CREATE TABLE IF NOT EXISTS specialoffer (
    offerId VARCHAR(255) PRIMARY KEY,
    offerName VARCHAR(255),
    offerDescription TEXT,
    startingDate TIMESTAMP,
    endDate TIMESTAMP,
    discountType VARCHAR(255),
    discountValue DOUBLE PRECISION,
    conditions TEXT,
    offerStatus offer_status
);

CREATE TYPE ticket_status as ENUM ('SOLD' , 'CANCELLED' , 'PPENDING');

CREATE TABLE IF NOT EXISTS tickets (
    ticketId VARCHAR(255) PRIMARY KEY,
    transportaionType transportation_type,
    boughtFor DOUBLE PRECISION,
    sellingPrice DOUBLE PRECISION,
    soldAt TIMESTAMP,
    ticketStatus ticket_status
);  