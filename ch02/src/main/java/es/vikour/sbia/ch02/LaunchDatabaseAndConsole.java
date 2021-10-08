package es.vikour.sbia.ch02;

import java.sql.SQLException;

import org.h2.tools.Console;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LaunchDatabaseAndConsole {
	
	public static void main(String [] args) throws SQLException {

		new ClassPathXmlApplicationContext(
				"/batch-infrastructure.xml",
				"/root-datasource-context.xml");
		
		Console.main(args);
	}

}
