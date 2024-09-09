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
    startingDate TIMESTAMP,
    endDate TIMESTAMP,
    specialRate DOUBLE PRECISION,
    agreementConditions VARCHAR(255),
    renewable BOOLEAN,
    contractStatus contract_status,
    partnerId VARCHAR(255),
    FOREIGN KEY (partnerId) REFERENCES partners(partnerId)
);


CREATE TYPE offer_status as ENUM ('ACTIVE' , 'SUSPENDED' , 'EXPIRED');
CREATE TYPE discount_type as ENUM ('FIX_AMOUNT' , 'PERCENTAGE');

CREATE TABLE IF NOT EXISTS specialoffers (
    offerId VARCHAR(255) PRIMARY KEY,
    offerName VARCHAR(255),
    offerDescription TEXT,
    startingDate TIMESTAMP,
    endDate TIMESTAMP,
    discountType VARCHAR(255),
    discountValue DOUBLE PRECISION,
    conditions TEXT,
    offerStatus offer_status,
    contractId VARCHAR(255),
    FOREIGN KEY (contractId) REFERENCES contracts(contractId)
);

CREATE TYPE ticket_status as ENUM ('SOLD' , 'CANCELLED' , 'PENDING');

CREATE TABLE IF NOT EXISTS tickets (
    ticketId VARCHAR(255) PRIMARY KEY,
    transportaionType transportation_type,
    boughtFor DOUBLE PRECISION,
    sellingPrice DOUBLE PRECISION,
    soldAt TIMESTAMP,
    ticketStatus ticket_status,
    contractId VARCHAR(255),
    routeid VARCHAR(255),
    FOREIGN KEY (contractId) REFERENCES contracts(contractId),
    FOREIGN KEY (routeid) REFERENCES routes(routeid)
);


CREATE TABLE IF NOT EXISTS customers (
    email VARCHAR(100) PRIMARY KEY NOT NULL,
    firstName VARCHAR(100),
    familyName VARCHAR(100),
    phoneNumber VARCHAR(15)
)


CREATE TABLE IF NOT EXISTS reservations(
    reservationId VARCHAR(255) NOT NULL PRIMARY KEY,
    cancelled_at TIMESTAMP,
    customerEmail VARCHAR(100),
    FOREIGN KEY(customerEmail) REFERENCES customers(email)
)

CREATE TABLE IF NOT EXISTS ticketsOfReservation(
    ticketId VARCHAR(255),
    reservationId VARCHAR(255),
    FOREIGN KEY (ticketId) REFERENCES tickets (ticketId),
    FOREIGN KEY (reservationId) REFERENCES reservations (reservationId),
    PRIMARY KEY (ticketId , reservationId)
)

CREATE TABLE IF NOT EXISTS routes(
    routeId VARCHAR(255) PRIMARY KEY NOT NULL,
    departure VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL
)