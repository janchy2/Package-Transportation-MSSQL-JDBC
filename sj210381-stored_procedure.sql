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