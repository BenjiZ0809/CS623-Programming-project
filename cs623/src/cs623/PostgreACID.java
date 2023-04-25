package cs623;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgreACID {

	public static void main(String[] args) throws SQLException {
		
		// Create connection to postgreSQL
		Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/cs623project", "postgres", "Zx199189@");
		
		// For atomicity
		conn.setAutoCommit(false);
		
		// Isolation
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		
		Statement stmt1 = null;
		
		try {
			// Create statement 
			stmt1 = conn.createStatement();
			
			stmt1.execute("ALTER TABLE stock DROP CONSTRAINT fk_stock_prodid");
			
			stmt1.execute("ALTER TABLE stock ADD CONSTRAINT fk_stock_prodid FOREIGN KEY(prodid) "
					+ "REFERENCES product(prodid) ON DELETE CASCADE ON UPDATE CASCADE");
			
			stmt1.executeUpdate("UPDATE product SET prodid = 'pp1' WHERE prodid = 'p1'");
			
		}
		catch(SQLException exc) {
			System.out.println("An exception was thrown");
			exc.printStackTrace();
			
			// atomicity
			conn.rollback();
			stmt1.close();
			conn.close();
			return;
		}
		
		System.out.println("Query executed successfully");
		conn.commit();
		stmt1.close();
		conn.close();
	}

}
