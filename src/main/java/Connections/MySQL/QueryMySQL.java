package Connections.MySQL;

public class QueryMySQL {
    public static final String ALLSHOPS_QUERY = "SELECT * FROM FlowerShops";
    public static final String ALLSTOCK_QUERY = "SELECT * FROM (GardenElements LEFT JOIN Stock ON GardenElements.idGardenElements = Stock.GardenElementsId AND Stock.FlowerShopId = ?) inner join Type on GardenElements.TypesId = Type.idType";
    public static final String NEWSHOP_QUERY = "INSERT INTO FlowerShops (Name) VALUES (?)";
    public static final String ADDSTOCK_QUERY = "INSERT INTO Stock (FlowerShopId,GardenElementsId,Quantity,Price) VALUES (?,?,?,?)";
    public static final String UPDATESTOCK_QUERY = "UPDATE Stock SET Quantity = ?, Price = ? WHERE GardenElementsId = ? and FlowerShopId = ?";
    public static final String REMOVESTOCK_QUERY = "UPDATE Stock SET Quantity = ? WHERE GardenElementsId = ? and FlowerShopId = ?";
    public static final String SHOWALLTICKETS_QUERY = "SELECT * FROM Ticket WHERE FlowerShopId = ?";
    public static final String ADDNEWTICKET_QUERY = "INSERT INTO Ticket (FlowerShopId) VALUES (?)";
    public static final String ADDPRODTOTICKET_QUERY = "Insert into TicketGardenElements(TicketID, GardenElementsId, Quantity) values (?,?,?)";
    public static final String REMOVESTORE_QUERY = "DELETE FROM FlowerShops WHERE IdFlowerShop = ?";
    public static final String TOTALPRICE_QUERY = "SELECT SUM(TotalPrice) AS TotalMoneyEarned FROM Ticket where FlowerShopId = ?";


}
