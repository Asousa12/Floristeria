use FlowerShopsBBDD;
SELECT * FROM GardenElements WHERE IdGardenElements = 1;
SELECT * FROM GardenElements WHERE IdGardenElements = 1;
SELECT * FROM GardenElements;
INSERT INTO FlowerShops (name) VALUES ("FlowerShopRosita");
UPDATE Stock SET Quantity = Quantity + 2 WHERE GardenElementsid = 1;
UPDATE Stock SET idStock = 1 WHERE GardenElementsid = 1;
UPDATE Stock SET Quantity = Quantity - 2 WHERE GardenElementsId = 1;
SELECT IdTicket, Date FROM Ticket WHERE FlowerShopId = 1;


