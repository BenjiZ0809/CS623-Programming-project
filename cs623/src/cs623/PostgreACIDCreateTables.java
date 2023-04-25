package cs623;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgreACIDCreateTables {

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

			stmt1.execute("CREATE TABLE Product(prodid VARCHAR(10), pname VARCHAR(30), price DECIMAL)");

			stmt1.execute("ALTER TABLE product ADD CONSTRAINT pk_product PRIMARY KEY(prodid)");
			
			stmt1.execute("ALTER TABLE product ADD CONSTRAINT ck_product_proice CHECK (price > 0)");
			
			stmt1.execute("CREATE TABLE depot(depid VARCHAR(10), address VARCHAR(30), volume INTEGER)");
			
			stmt1.execute("ALTER TABLE depot ADD CONSTRAINT pk_depot PRIMARY KEY(depid)");
			
			stmt1.execute("ALTER TABLE depot ADD CONSTRAINT ck_depot_volume CHECK(volume > 0)");
			
			stmt1.execute("CREATE TABLE stock(prodid VARCHAR(10), depid VARCHAR(10), quantity INTEGER)");
			
			stmt1.execute("ALTER TABLE stock ADD CONSTRAINT pk_stock PRIMARY KEY(prodid, depid)");
			
			stmt1.execute("ALTER TABLE stock ADD CONSTRAINT fk_stock_prodid FOREIGN KEY(prodid) REFERENCES product(prodid)");
			
			stmt1.execute("ALTER TABLE stock ADD CONSTRAINT fk_stock_depid FOREIGN KEY(depid) REFERENCES depot(depid)");
			
			stmt1.execute("INSERT INTO product(prodid, pname, price) VALUES ('p1', 'tape', 2.5)");
			
			stmt1.execute("INSERT INTO product(prodid, pname, price) VALUES ('p2', 'tv', 250)");
			
			stmt1.execute("INSERT INTO product(prodid, pname, price) VALUES ('p3', 'vcr', 80)");
			
			stmt1.execute("INSERT INTO depot(depid, address, volume) VALUES ('d1', 'New York', 9000)");
			
			stmt1.execute("INSERT INTO depot(depid, address, volume) VALUES ('d2', 'Syracuse', 6000)");
			
			stmt1.execute("INSERT INTO depot(depid, address, volume) VALUES ('d4', 'New York', 2000)");
			
			stmt1.execute("INSERT INTO stock(prodid, depid, quantity) VALUES ('p1', 'd1', 1000)");
			
			stmt1.execute("INSERT INTO stock(prodid, depid, quantity) VALUES ('p1', 'd2', -100)");
			
			stmt1.execute("INSERT INTO stock(prodid, depid, quantity) VALUES ('p1', 'd4', 1200)");
			
			stmt1.execute("INSERT INTO stock(prodid, depid, quantity) VALUES ('p3', 'd1', 3000)");
			
			stmt1.execute("INSERT INTO stock(prodid, depid, quantity) VALUES ('p3', 'd4', 2000)");
			
			stmt1.execute("INSERT INTO stock(prodid, depid, quantity) VALUES ('p2', 'd4', 1500)");
			
			stmt1.execute("INSERT INTO stock(prodid, depid, quantity) VALUES ('p2', 'd1', -400)");
			
			stmt1.execute("INSERT INTO stock(prodid, depid, quantity) VALUES ('p2', 'd2', 2000)");

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
