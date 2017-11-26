package fr.proneus.engine.timer;

import java.util.ArrayList;
import java.util.List;

public class Timer {

	private ArrayList<Task> taskList;
	private List<Integer> taskToRemove;

	private int currentTask;

	public Timer() {
		this.taskList = new ArrayList<>();
		this.taskToRemove = new ArrayList<>();
	}

	protected void update() {
		long currentTime = System.currentTimeMillis();
		ArrayList<Task> tasks = new ArrayList<>(taskList);
		for (Task task : tasks) {
			if (task != null) {
				if (!task.started) {
					if (currentTime - task.time >= task.getFirstRun()) {
						task.getRunnable().run();
						if (task.getScheduleTime() == 0) {
							taskToRemove.add(task.getID());
						} else {
							task.time = currentTime;
							task.started = true;
						}
					}
				} else {
					if (currentTime - task.time > task.getScheduleTime()) {
						task.getRunnable().run();
						task.time = currentTime;
					}
				}
			}
		}

		if (!taskToRemove.isEmpty()) {
			ArrayList<Integer> taskToRemoveCopy = new ArrayList<>(taskToRemove);
			taskToRemoveCopy.forEach(t -> removeTask(t));
			taskToRemove.clear();
		}
	}

	public int addTimerTask(Runnable runnable, int time) {
		int id = currentTask++;
		taskList.add(new Task(id, runnable, time, 0));

		return id;
	}

	public int addRepeatingTask(Runnable runnable, int firstRun, int schedule) {
		int id = currentTask++;
		taskList.add(new Task(id, runnable, firstRun, schedule));

		return id;
	}

	public Task getTask(int task) {
		for (Task t : taskList) {
			if (t.getID() == task)
				return t;
		}
		return null;
	}

	public void removeTask(int task) {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getID() == task) {
				taskList.remove(i);
				return;
			}
		}
	}

	public void reset() {
		for (Task task : taskList) {
			if (!this.taskToRemove.contains(task.getID()))
				this.taskToRemove.add(task.getID());
		}
	}

}
