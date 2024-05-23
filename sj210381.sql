
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
	[profit]             integer  NOT NULL ,
	[status]             integer  NOT NULL ,
	[licencePlateNumber] varchar(100)  NOT NULL ,
	[currentProfit]      integer NOT NULL
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

CREATE PROCEDURE [spGrantRequest] @userName varchar(100)
AS
BEGIN
	DECLARE @licencePlateNumber varchar(100)
	SET @licencePlateNumber=(
		SELECT licencePlateNumber FROM [CourierRequest] WHERE userName=@userName
	)

	IF @licencePlateNumber IS NULL
	BEGIN
		RETURN 1
	END

	INSERT INTO [Courier](userName, licencePlateNumber, [status], profit, numberOfDeliveries, currentProfit) VALUES(@userName, @licencePlateNumber, 0, 0, 0, 0)
	DELETE FROM CourierRequest WHERE userName=@userName

	RETURN 0
END

go

CREATE PROCEDURE [spAcceptOffer] @idOffer int
AS
BEGIN
	DECLARE @pricePercentage decimal(10,3)
	DECLARE @userName varchar(100)
	DECLARE @idPackage int

	SET @pricePercentage=(
		SELECT pricePercentage FROM TransportOffer WHERE idOffer=@idOffer
	)
	
	IF @pricePercentage IS NULL
	BEGIN
		RETURN 1
	END

	SET @userName=(
		SELECT userName FROM TransportOffer WHERE idOffer=@idOffer
	)
	SET @idPackage=(
		SELECT idPackage FROM TransportOffer WHERE idOffer=@idOffer
	)

	INSERT INTO AcceptedOffer(idPackage, userName, pricePercentage) VALUES(@idPackage, @userName, @pricePercentage)
	UPDATE Package SET [status]=1 WHERE idPackage=@idPackage
	UPDATE Package SET acceptanceTime=GETDATE() WHERE idPackage=@idPackage
	RETURN 0
END

go

CREATE PROCEDURE [spGetDeliveryPrice] @result decimal(10,3) output, @idPackage int
AS
BEGIN
	DECLARE @pricePercentage decimal(10,3)
	SET @pricePercentage=(
		SELECT pricePercentage from AcceptedOffer WHERE idPackage=@idPackage
	)

	IF @pricePercentage IS NULL
	BEGIN
		SET @result=-1
	END

	DECLARE @price decimal(10,3)
	SET @price=(
		SELECT price FROM Package WHERE idPackage=@idPackage
	)

	SET @result=@price * @pricePercentage / 100.0
END

go

CREATE PROCEDURE [spNextDrive] @userName varchar(100), @idPackage int output, @nextPackage int output
AS
BEGIN
	DECLARE @driving int
	DECLARE @status int

	SET @driving=(
		SELECT [status] FROM [Courier] WHERE userName=@userName
	)
	IF @driving=0
	BEGIN
		SET @status=1
	END
	ELSE
	BEGIN
		SET @status=2
	END

	SET @idPackage=(
		SELECT TOP 1 Package.idPackage FROM [Package] JOIN [AcceptedOffer] ON(Package.idPackage=AcceptedOffer.idPackage) 
		WHERE AcceptedOffer.username=@userName and Package.[status]=@status ORDER BY Package.acceptanceTime
		)

	IF @idPackage IS NULL
	BEGIN
		SET @idPackage=-1
		SET @nextPackage=-1
		RETURN
	END

	UPDATE [Package] SET [status]=3 WHERE idPackage=@idPackage

	SET @nextPackage=(
		SELECT TOP 1 Package.idPackage FROM [Package] JOIN [AcceptedOffer] ON(Package.idPackage=AcceptedOffer.idPackage) 
		WHERE AcceptedOffer.username=@userName and Package.[status]=1 ORDER BY Package.acceptanceTime
		)

	IF @nextPackage IS NULL
	BEGIN
		SET @nextPackage=-1
	END
	ELSE
	BEGIN
		UPDATE Package SET [status]=2 WHERE idPackage=@nextPackage
	END
	
	UPDATE [Courier] SET numberOfDeliveries=numberOfDeliveries + 1 WHERE userName=@userName
END

go

CREATE PROCEDURE [spCalculateProfit] @distance decimal(10,3), @betweenDistance decimal(10,3), @idPackage int, @userName varchar(100)
AS
BEGIN
	DECLARE @pricePercentage decimal(10,3)
	DECLARE @price decimal(10,3)
	DECLARE @gain decimal(10,3)
	DECLARE @loss decimal(10,3)
	DECLARE @fuelType int
	DECLARE @fuelConsumption decimal(10,3)
	DECLARE @fuelPrice int

	SET @pricePercentage=(
		SELECT pricePercentage FROM AcceptedOffer WHERE idPackage=@idPackage
	)

	SET @price=(
		SELECT price FROM Package WHERE idPackage=@idPackage
	)

	SET @gain=@price + @price * @pricePercentage / 100

	SET @fuelType=(
		SELECT Vehicle.fuelType FROM [Courier] join [Vehicle] ON(Courier.licencePlateNumber=Vehicle.licencePlateNumber)
		WHERE Courier.userName=@userName
	)

	SET @fuelConsumption=(
		SELECT Vehicle.fuelConsumption FROM [Courier] join [Vehicle] ON(Courier.licencePlateNumber=Vehicle.licencePlateNumber)
		WHERE Courier.userName=@userName
	)

	IF @fuelType=0
	BEGIN
		SET @fuelPrice=15
	END
	IF @fuelType=1
	BEGIN
		SET @fuelPrice=36
	END
	IF @fuelType=2
	BEGIN
		SET @fuelPrice=32
	END

	SET @loss=@fuelConsumption * @fuelPrice * @distance

	IF @betweenDistance!= -1
	BEGIN
		SET @loss=@loss + @fuelConsumption * @fuelPrice * @betweenDistance
		UPDATE [Courier] SET currentProfit=currentProfit + @gain - @loss WHERE userName=@userName
	END
	ELSE
	BEGIN
		UPDATE [Courier] SET profit=currentProfit + @gain - @loss WHERE userName=@userName
		UPDATE [Courier] SET currentProfit=0 WHERE userName=@userName
		UPDATE [Courier] SET [status]=0 WHERE userName=@userName
	END
END

go

CREATE TRIGGER TR_TransportOffer_DeleteOffers
ON AcceptedOffer
AFTER INSERT
AS
BEGIN
	DELETE FROM TransportOffer WHERE idPackage IN (SELECT idPackage FROM INSERTED)
END

go

CREATE TRIGGER TR_SentPackage
ON Package
AFTER INSERT
AS
BEGIN
	UPDATE [User] SET numberOfSentPackages=numberOfSentPackages + 1
	WHERE userName IN (SELECT userName FROM INSERTED)
END

go

CREATE TRIGGER TR_DeletedDistricts ON District
AFTER DELETE
AS
BEGIN
	DELETE FROM Package WHERE districtFrom IN (SELECT idDistrict FROM DELETED)
	DELETE FROM Package WHERE districtTo IN (SELECT idDistrict FROM DELETED)
END

go