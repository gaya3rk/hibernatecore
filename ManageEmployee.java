import java.util.*;
import org.hibernate.*;
import org.hibernate.cfg.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class ManageEmployee {
	private static SessionFactory factory;
	public static void main(String args[]) {
		try 
		{
			factory=new Configuration().configure().buildSessionFactory();
		}
		catch(Throwable ex)
		{
			System.err.println("Failed to create sessionFactory object" +ex);
			throw new ExceptionInInitializerError(ex);
		}
		ManageEmployee ME=new ManageEmployee();
		Integer empID1=ME.addEmployee("Zara","Ali",2000);
		Integer empID2=ME.addEmployee("Daisy","Das",5000);
		Integer empID3=ME.addEmployee("John","Paul",5000);
		Integer empID4=ME.addEmployee("Mohd","Yasee",3000);
		ME.listEmployees();
		ME.countEmployee();
		ME.totalSalary();
	}
	public Integer addEmployee(String fname,String lname,int Salary) 
	{
		Session session=factory.openSession();
		Transaction tx=null;
		Integer employeeID=null;
		try 
		{
			tx=session.beginTransaction();
			Employee employee=new Employee(fname,lname,Salary);
			employeeID=(Integer)session.save(employee);
			tx.commit();
		}
		catch(HibernateException e) 
		{
			if(tx!=null)tx.rollback();
			e.printStackTrace();
		}
		finally
		{
			session.close();
		}
		return employeeID;
	}
	public void listEmployees() {
		Session session=factory.openSession();
		Transaction tx=null;
		 try
		 {
			 tx=session.beginTransaction();
			 Criteria cr=session.createCriteria(Employee.class);
			 cr.add(Restrictions.gt("Salary", 2000));
			 List employees=cr.list();
			 for(Iterator iterator=employees.iterator();iterator.hasNext();) 
			 {
				 Employee employee=(Employee)iterator.next();
				 System.out.println("First Name:"+employee.getFirstName());
				 System.out.println("Last Name:"+employee.getLastName());
				 System.out.println("Salary:"+employee.getSalary());
			 }
			 tx.commit();
		 }
		 catch(HibernateException e)
		 {
			 if(tx!=null)tx.rollback();
			 e.printStackTrace();
		 }
		 finally 
		 {
			 session.close();
		 }
	}
	public void countEmployee() {
		Session session=factory.openSession();
		Transaction tx=null;
		try
		{
			tx=session.beginTransaction();
			Criteria cr=session.createCriteria(Employee.class);
			cr.setProjection(Projections.rowCount());
			List rowCount=cr.list();
			System.out.println("Total count:"+rowCount.get(0));
			tx.commit();
		}
		catch(HibernateException e)
		{
			if(tx!=null)tx.rollback();
			e.printStackTrace();
		}
		finally
		{
			session.close();
		}
	}
	public void totalSalary()
	{
		Session session=factory.openSession();
		Transaction tx=null;
		try
		{
			tx=session.beginTransaction();
			Criteria cr=session.createCriteria(Employee.class);
			cr.setProjection(Projections.sum("Salary"));
			List totalSalary=cr.list();
			System.out.println("Total Salary:"+totalSalary.get(0));
			tx.commit();
	}
		catch(HibernateException e) {
			if(tx!=null)tx.rollback();
			e.printStackTrace();
		}
		finally {
			session.close();
		}

	}

}
