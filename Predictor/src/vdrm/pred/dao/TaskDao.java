package vdrm.pred.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;


import vdrm.base.data.ITask;
import vdrm.base.impl.Task;
import vdrm.pred.dbaccess.DBManager;

public class TaskDao implements ITaskDao {

	private DBManager db;

	public TaskDao(){
		this.setDb(DBManager.getInstance());
	}
	
	public void setDb(DBManager db) {
		this.db = db;
	}
	
	public ArrayList<ITask> getAllTasks()
	{
		ArrayList<ITask> tasks = new ArrayList<ITask>();
		ResultSet rs=null;
		
		String querry = "select * from tasks;";
		System.out.println(querry);
		Statement statement=db.prepareSqlStatement();
		
		try {
			rs = statement.executeQuery(querry);
			int cpu,mem,hdd;
			UUID id;
			String aux;
			ITask temp;
			while ( rs.next())
			{
				cpu = rs.getInt("cpu");
				mem = rs.getInt("mem");
				hdd = rs.getInt("hdd");
				aux = rs.getString("uuid");
				id = UUID.fromString(aux);
				temp = new Task(cpu,mem,hdd);
				temp.setTaskHandle(id);
				tasks.add(temp);
			}
		}
		catch (SQLException e) 
		{
			System.out.println("SQL Exception: execute query select all tasks ");
			e.printStackTrace();
		}	
		return tasks;
	}
	
	public void addTask(ITask task)
	{
		String querry;
		
		querry = "insert into tasks (uuid,cpu,mem,hdd) " +
					"values ('"+task.getTaskHandle()+"',"+task.getCpu()+
					","+task.getMem()+","+task.getHdd()+");";

		Statement statement=db.prepareSqlStatement();
		
		try {
			statement.execute(querry);
		} catch (SQLException e) {
			System.out.println("SQL Exception: execute query insert into tasks");
			e.printStackTrace();
		}
	}
	
	public ArrayList<ITask> getTaskHistory() {
		ArrayList<ITask> tasks = new ArrayList<ITask>();
		ResultSet rs=null;
		
		String querry = "select * from history;";
		System.out.println(querry);
		Statement statement=db.prepareSqlStatement();
		
		try {
			rs = statement.executeQuery(querry);
			int cpu,mem,hdd;
			UUID id;
			String aux;
			ITask temp;
			while ( rs.next())
			{
				cpu = rs.getInt("cpu");
				mem = rs.getInt("mem");
				hdd = rs.getInt("hdd");
				aux = rs.getString("uuid");
				id = UUID.fromString(aux);
				temp = new Task(cpu,mem,hdd);
				temp.setTaskHandle(id);
				tasks.add(temp);
			}
		}
		catch (SQLException e) 
		{
			System.out.println("SQL Exception: execute query select all history ");
			e.printStackTrace();
		}	
		return tasks;
	}
	
	public void addTaskToHistory(ITask task) {
		String querry;
		
		querry = "insert into history (uuid,cpu,mem,hdd) " +
					"values ('"+task.getTaskHandle()+"',"+task.getCpu()+
					","+task.getMem()+","+task.getHdd()+");";

		Statement statement=db.prepareSqlStatement();
		
		try {
			statement.execute(querry);
		} catch (SQLException e) {
			System.out.println("SQL Exception: execute query insert into history");
			e.printStackTrace();
		}
	}
}

