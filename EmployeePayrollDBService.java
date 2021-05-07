import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Driver;
import java.sql.SQLException;
import java.time.LocalDate;

public class EmployeePayrollDBService {

	private static EmployeePayrollDBService employeePayrollDBService;
	private PreparedStatement employeePayrollDataStatement;
	
	public EmployeePayrollDBService()
	{
		
	}
	
	public static EmployeePayrollDBService getInstance()
	{
		if(employeePayrollDBService == null)
			employeePayrollDBService = new EmployeePayrollDBService();
		return employeePayrollDBService;
	}
	
	//USE CASE 1
	private Connection getConnection() throws SQLException
	{
		String url = "jdbc:mysql://localhost:3306/payroll_service?characterEncoding=utf8&useSSL=false&useUnicode=true" ;
		String username = "root";
		String password = "Ak@Dd14a";
		Connection connection;
		System.out.println("Connecting to : "+url);
		connection = DriverManager.getConnection(url, username, password);
		System.out.println("Connection success"+connection);
		return connection;
	}
	
	//USE CASE 2
	public List<EmployeePayrollData> readData()
	{
		String sql = "select * from employee_payroll2";
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = this.getConnection();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
		    employeePayrollList = this.getEmployeePayrollData(result);
		}	
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(ClassNotFoundException e) 
		{
			
		}
		return employeePayrollList;
	}
	
	public int updateEmployeeData(String name, double salary)
	{
		return this.updateEmployeeDataUsingStatement(name, salary);
	}
	
	private int updateEmployeeDataUsingStatement(String name,double salary)
	{
		String sql = String.format("update employee_payroll set salary = %.2f where name = '%s';", salary, name);
		try(Connection connection = this.getConnection())
		{
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sql);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<EmployeePayrollData> getEmployeePayrollData(String name)
	{
		List<EmployeePayrollData> employeePayrollList = null;
		if(this.employeePayrollDataStatement == null)
			this.prepareStatementForEmployeeData();
		try
		{
			employeePayrollDataStatement.setString(1, name);
			ResultSet resultSet = employeePayrollDataStatement.executeQuery();
			employeePayrollList = this.getEmployeePayrollData(resultSet);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return employeePayrollList;
	}
	
	public List<EmployeePayrollData> getEmployeePayrollData(ResultSet result)
	{
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try
		{
			while(result.next())
			{
				int id = result.getInt("id");
				String name = result.getString("name");
				double salary = result.getInt("salary");
				LocalDate startDate = result.getDate("start_date").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return employeePayrollList;
	}
	
	private void prepareStatementForEmployeeData()
	{
		try
		{
			Connection connection = this.getConnection();
			String sql = "select * from employee_payroll where name = ?";
			employeePayrollDataStatement = connection.prepareStatement(sql);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	private List<EmployeePayrollData> getEmployeePayrollDataUsingDB(String sql)
	{
		ResultSet resultSet;
		List<EmployeePayrollData> employeePayrollList = null;
		try(Connection connection = this.getConnection())
		{
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			employeePayrollList = this.getEmployeePayrollData(result);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return employeePayrollList;
	}
	private int updateEmployeeDataUsingPreparedStatement(String name,double salary)
	{
		
		try(Connection connection = this.getConnection())
		{
			String sql = "update employee_payroll set salary = ? where name = ?;";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1,salary);
			preparedStatement.setString(2,name);
			return preparedStatement.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}
}
