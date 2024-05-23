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