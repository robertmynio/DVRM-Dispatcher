package vdrm.pred.miner;

import java.util.ArrayList;

import vdrm.base.data.ITask;
import vdrm.pred.dao.TaskDao;
import vdrm.pred.pred.Predictor;


public class MinerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Predictor pred = new Predictor();
		TaskDao dao = new TaskDao();
		ArrayList<ITask> tasks = dao.getAllTasks();
		pred.initialize(tasks);
		
		ArrayList<ITask> history = dao.getTaskHistory();
		pred.setHistory(history);
		
		ArrayList<ITask> prediction = null;
		
		for(int i = 0 ; i<tasks.size(); i++) {
			prediction = pred.predict(tasks.get(i));
			if(prediction!=null)
				System.out.println("Prediction length for "+i+" = " + prediction.size());
		}
	}

}
