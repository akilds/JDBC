import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollService {

	public enum IOService {CONSOLE_IO, FILE_IO, DB_IO, REST_IO}
	private List<EmployeePayrollData> employeePayrollList;
	private EmployeePayrollDBService employeePayrollDBService;
	
	public EmployeePayrollService() {
		employeePayrollDBService = EmployeePayrollDBService.getInstance();
	}
	
	public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList){
		this();
		this.employeePayrollList = employeePayrollList;
	}
	
	public static void main(String[] args) {
		ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
		Scanner consoleInputReader = new Scanner(System.in);
		employeePayrollService.readEmployeePayrollData(consoleInputReader);
		employeePayrollService.writeEmployeePayrollData(IOService.CONSOLE_IO);
	}
	
	public void readEmployeePayrollData(Scanner consoleInputReader)
	{
		System.out.println("Enter Employee ID : ");
		int id = consoleInputReader.nextInt();
		System.out.println("Enter Employee Name : ");
		String name = consoleInputReader.next();
		System.out.println("Enter Employee salary : ");
		double salary = consoleInputReader.nextDouble();
		employeePayrollList.add(new EmployeePayrollData(id, name, salary));
	}

	public void writeEmployeePayrollData(IOService ioService)
	{
		if(ioService.equals(EmployeePayrollService.IOService.CONSOLE_IO))
			System.out.println("\nWriting Employee payroll Roaster to Console\n" + employeePayrollList);
		else if(ioService.equals(EmployeePayrollService.IOService.FILE_IO))
			new EmployeePayrollFileIOService().writeData(employeePayrollList);
	}
	
	public void printData(IOService ioService)
	{
		if(ioService.equals(IOService.FILE_IO))
			new EmployeePayrollFileIOService().printData();
	}
	
	public long countEntries(IOService ioService)
	{
		if(ioService.equals(IOService.FILE_IO))
			return new EmployeePayrollFileIOService().countEntries();
		return 0;
	}

	public long readEmployeePayrollData(IOService ioService)
	{
		if(ioService.equals(IOService.FILE_IO))
			this.employeePayrollList = new EmployeePayrollFileIOService().readData();
		return employeePayrollList.size();
	}
	
	public List<EmployeePayrollData> readEmployeePayrollData1(IOService ioService)
	{
		if(ioService.equals(IOService.DB_IO))
			this.employeePayrollList = employeePayrollDBService.readData();
		return this.employeePayrollList;
	}
	
	public void updateEmployeeSalary(String name, double salary)
	{
		EmployeePayrollDBService employeePayrollDBService = new EmployeePayrollDBService();
		int result = employeePayrollDBService.updateEmployeeData(name,salary);
		if(result==0)	return;
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if(employeePayrollData != null)	employeePayrollData.salary = salary;
	}
	
	private EmployeePayrollData getEmployeePayrollData(String name)
	{
		EmployeePayrollData employeePayrollData;
		employeePayrollData = this.employeePayrollList.stream()
								  .filter(employeePayrollDataItem -> employeePayrollDataItem.name.equals(name))
								  .findFirst()
								  .orElse(null);
		return employeePayrollData;
	}
	
	public boolean checkEmployeePayrollInSyncWithDB(String name)
	{
		List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
		return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
	}
}
