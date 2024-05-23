
CREATE TABLE [AcceptedOffer]
( 
	[idPackage]          integer  NOT NULL ,
	[userName]           varchar(100)  NOT NULL ,
	[pricePercentage]    decimal(10,3)  NOT NULL 
)
go

CREATE TABLE [Admin]
( 
	[userName]           varchar(100)  NOT NULL 
)
go

CREATE TABLE [City]
( 
	[idCity]             integer  IDENTITY(1,1) NOT NULL ,
	[name]               varchar(100)  NOT NULL ,
	[postalCode]         varchar(100)  NOT NULL 
)
go

CREATE TABLE [Courier]
( 
	[userName]           varchar(100)  NOT NULL ,
	[numberOfDeliveries] integer  NOT NULL ,
	[profit]             decimal(10,3)  NOT NULL ,
	[status]             integer  NOT NULL ,
	[licencePlateNumber] varchar(100)  NOT NULL ,
	[currentProfit]      decimal(10,3) NOT NULL
)
go

CREATE TABLE [CourierRequest]
( 
	[userName]           varchar(100)  NOT NULL ,
	[licencePlateNumber] varchar(100)  NOT NULL 
)
go

CREATE TABLE [District]
( 
	[idDistrict]         integer  IDENTITY(1,1) NOT NULL ,
	[xCord]              integer  NOT NULL ,
	[yCord]              integer  NOT NULL ,
	[idCity]             integer  NOT NULL ,
	[name]               varchar(100)  NOT NULL 
)
go

CREATE TABLE [Package]
( 
	[idPackage]          integer  IDENTITY(1,1) NOT NULL ,
	[packageType]        integer  NOT NULL ,
	[weight]             decimal(10,3)  NOT NULL ,
	[acceptanceTime]     datetime  NULL ,
	[price]              decimal(10,3)  NOT NULL ,
	[status]             integer  NOT NULL ,
	[districtFrom]       integer  NOT NULL ,
	[districtTo]         integer  NOT NULL ,
	[userName]           varchar(100)  NOT NULL 
)
go

CREATE TABLE [TransportOffer]
( 
	[idOffer]            integer  IDENTITY(1,1) NOT NULL ,
	[pricePercentage]    decimal(10,3)  NOT NULL ,
	[userName]           varchar(100)  NOT NULL ,
	[idPackage]          integer  NOT NULL 
)
go

CREATE TABLE [User]
( 
	[userName]           varchar(100)  NOT NULL ,
	[firstName]          varchar(100)  NOT NULL ,
	[lastName]           varchar(100)  NOT NULL ,
	[password]           varchar(100)  NOT NULL ,
	[numberOfSentPackages] integer  NOT NULL 
)
go

CREATE TABLE [Vehicle]
( 
	[licencePlateNumber] varchar(100)  NOT NULL ,
	[fuelType]           integer  NOT NULL ,
	[fuelConsumption]    decimal(10,3)  NOT NULL 
)
go

ALTER TABLE [AcceptedOffer]
	ADD CONSTRAINT [XPKAcceptedOffer] PRIMARY KEY  CLUSTERED ([idPackage] ASC)
go

ALTER TABLE [Admin]
	ADD CONSTRAINT [XPKAdmin] PRIMARY KEY  CLUSTERED ([userName] ASC)
go

ALTER TABLE [City]
	ADD CONSTRAINT [XPKCity] PRIMARY KEY  CLUSTERED ([idCity] ASC)
go

ALTER TABLE [Courier]
	ADD CONSTRAINT [XPKCourier] PRIMARY KEY  CLUSTERED ([userName] ASC)
go

ALTER TABLE [CourierRequest]
	ADD CONSTRAINT [XPKCourierRequest] PRIMARY KEY  CLUSTERED ([userName] ASC)
go

ALTER TABLE [District]
	ADD CONSTRAINT [XPKDistrict] PRIMARY KEY  CLUSTERED ([idDistrict] ASC)
go

ALTER TABLE [Package]
	ADD CONSTRAINT [XPKPackage] PRIMARY KEY  CLUSTERED ([idPackage] ASC)
go

ALTER TABLE [TransportOffer]
	ADD CONSTRAINT [XPKTransportOffer] PRIMARY KEY  CLUSTERED ([idOffer] ASC)
go

ALTER TABLE [User]
	ADD CONSTRAINT [XPKUser] PRIMARY KEY  CLUSTERED ([userName] ASC)
go

ALTER TABLE [Vehicle]
	ADD CONSTRAINT [XPKVehicle] PRIMARY KEY  CLUSTERED ([licencePlateNumber] ASC)
go


ALTER TABLE [AcceptedOffer]
	ADD CONSTRAINT [R_15] FOREIGN KEY ([idPackage]) REFERENCES [Package]([idPackage])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

ALTER TABLE [AcceptedOffer]
	ADD CONSTRAINT [R_16] FOREIGN KEY ([userName]) REFERENCES [Courier]([userName])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Admin]
	ADD CONSTRAINT [R_2] FOREIGN KEY ([userName]) REFERENCES [User]([userName])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Courier]
	ADD CONSTRAINT [R_4] FOREIGN KEY ([userName]) REFERENCES [User]([userName])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

ALTER TABLE [Courier]
	ADD CONSTRAINT [R_6] FOREIGN KEY ([licencePlateNumber]) REFERENCES [Vehicle]([licencePlateNumber])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [CourierRequest]
	ADD CONSTRAINT [R_7] FOREIGN KEY ([userName]) REFERENCES [User]([userName])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [CourierRequest]
	ADD CONSTRAINT [R_9] FOREIGN KEY ([licencePlateNumber]) REFERENCES [Vehicle]([licencePlateNumber])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [District]
	ADD CONSTRAINT [R_1] FOREIGN KEY ([idCity]) REFERENCES [City]([idCity])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [Package]
	ADD CONSTRAINT [R_10] FOREIGN KEY ([districtFrom]) REFERENCES [District]([idDistrict])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Package]
	ADD CONSTRAINT [R_11] FOREIGN KEY ([districtTo]) REFERENCES [District]([idDistrict])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Package]
	ADD CONSTRAINT [R_12] FOREIGN KEY ([userName]) REFERENCES [User]([userName])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [TransportOffer]
	ADD CONSTRAINT [R_13] FOREIGN KEY ([userName]) REFERENCES [Courier]([userName])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

ALTER TABLE [TransportOffer]
	ADD CONSTRAINT [R_14] FOREIGN KEY ([idPackage]) REFERENCES [Package]([idPackage])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

ALTER TABLE City ADD CONSTRAINT UQ_City_PostalCode UNIQUE (postalCode)
go
ALTER TABLE City ADD CONSTRAINT UQ_City_Name UNIQUE (name)
go
ALTER TABLE Vehicle
ADD CONSTRAINT CK_Vehicle_FuelType CHECK (fuelType IN (0, 1, 2));
go
ALTER TABLE Package
ADD CONSTRAINT CK_Package_PackageType CHECK (packageType IN (0, 1, 2));
go
ALTER TABLE Courier
ADD CONSTRAINT CK_Courier_Status CHECK ([status] IN (0, 1));
go
ALTER TABLE Courier
ADD CONSTRAINT UQ_Courier_LicencePlateNumber UNIQUE (licencePlateNumber);
go