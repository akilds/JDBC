import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

public class EmployeePayrollServiceTest {
	@Test
	public void employeePayrollCheck()
	{
		EmployeePayrollData[] arrayOfEmps = {
				new EmployeePayrollData(1,"Jeff Bezos",10000.0),
				new EmployeePayrollData(2,"Bill Gates",20000.0),
				new EmployeePayrollData(3,"Mark Zuckerburg",30000.0)
		};
		EmployeePayrollService employeePayrollService;
		employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
		employeePayrollService.writeEmployeePayrollData(EmployeePayrollService.IOService.FILE_IO);
		employeePayrollService.printData(EmployeePayrollService.IOService.FILE_IO);
		long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO);
		Assert.assertEquals(3,entries);
	}
	
	@Test
	public void employeePayrollCheck2()
	{
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		long entries = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.FILE_IO);
		Assert.assertEquals(3,entries);
	}
	
	@Test
	public void matchCount()
	{
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData1(EmployeePayrollService.IOService.DB_IO);
		Assert.assertEquals(3, employeePayrollData.size());
	}
	
	@Test
	public void testUpdation()
	{
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData1(EmployeePayrollService.IOService.DB_IO);
		employeePayrollService.updateEmployeeSalary("Terisa",3000000.00);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
		Assert.assertTrue(result);
	}
}
