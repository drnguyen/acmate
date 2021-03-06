package org.svv.acmate.executor;

import java.util.ArrayList;
import java.util.List;

import org.svv.acmate.burpsuite.SiteMap;
import org.svv.acmate.exception.ExecutorException;
import org.svv.acmate.gui.IModelListener;
import org.svv.acmate.model.Request;
import org.svv.acmate.model.TargetAppModel;
import org.svv.acmate.model.config.User;

import burp.IBurpExtenderCallbacks;

public abstract class ACTestExecutor {
	
	protected IBurpExtenderCallbacks callbacks;
	protected SiteMap siteMap;
	protected TargetAppModel appModel;
	
	private List<IExecutorListener> listeners;
	
	/**
	 * Constructor using fields
	 * 
	 * @param userList
	 * @param callbacks
	 * @param siteMap
	 * @param appModel
	 */
	public ACTestExecutor(IBurpExtenderCallbacks callbacks, SiteMap siteMap,
			TargetAppModel appModel) {
		this.callbacks = callbacks;
		this.siteMap = siteMap;
		this.appModel = appModel;
		
		listeners = new ArrayList<IExecutorListener>();
	}
	
	
	public void registerListener(IExecutorListener listener){
		this.listeners.add(listener);
	}
	
	public void deregisterListener(IExecutorListener listener){
		this.listeners.remove(listener);
	}
	
	/**
	 * Inform the listeners about the starting of the job;
	 */
	protected void notifyStartExecution(int load){
		for (IExecutorListener listener : listeners){
			listener.start(load);
		}
	}
	
	
	protected void notifyStartExecution(List<Session> sessions){
		for (IExecutorListener listener : listeners){
			listener.start(sessions);
		}
	}

	protected void informProgress(int completed){
		for (IExecutorListener listener : listeners){
			listener.progress(completed);
		}
	}
	
	protected void notifyDoneExecution(){
		for (IExecutorListener listener : listeners){
			listener.done();
		}
		
		// also inform model listeners
		appModel.modelChanged(IModelListener.EVENT_TESTRESULT_UPDATED);
	}

	protected void notifyFailure(String message){
		for (IExecutorListener listener : listeners){
			listener.failed(message);
		}
	}
	
	/**
	 * Check if pages and users have been selected
	 * @return
	 * @throws ExecutorException 
	 */
	protected boolean isExecutionReady(List<String> selectedPaths, List<User> selectedUsers) throws ExecutorException{
		if (siteMap == null || appModel == null){
			throw new ExecutorException("Site map or target application cannot be null!");
		}
		
		for (String path : siteMap.getPaths()){
			Boolean val = siteMap.getSelectedPaths().get(path);
			
			if (val != null && val.booleanValue()){
				selectedPaths.add(path);
			}
		}
		
		if (selectedPaths.size() == 0){
			throw new ExecutorException("Please select target pages for AC testing!");
		}
		
		// check users
		for (User u : appModel.getActiveUsers().keySet()){
			Boolean val = appModel.getActiveUsers().get(u);
			if (val.booleanValue()){
				selectedUsers.add(u);
			}
		}
		
		if (selectedUsers.size() == 0){
			throw new ExecutorException("Select users for AC testing!");
		}
		
		return true;
	}
	
	/**
	 * Implement this method to perform concrete AC test execution method
	 */
	public abstract void execute() throws ExecutorException;

	/**
	 * create sessions for selected users and selected requests
	 * @param selectedUsers
	 * @param selectedRequests
	 * @return
	 */
	protected List<Session> prepareSessions(final List<User> selectedUsers, final List<Request> selectedRequests) {
		List<Session> sessions = new ArrayList<Session>();
		for (int i = 0; i < selectedUsers.size(); i++){
			User u = selectedUsers.get(i);
			
			sessions.add(new Session(u, selectedRequests, callbacks, appModel));
		}
		return sessions;
	}
	
	
	/**
	 * Start a thread to execute multiple sessions
	 * @param sessions
	 * @throws ExecutorException
	 */
	protected void startACTest(final List<Session> sessions) throws ExecutorException{
		Thread worker = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for (int i = 0; i < sessions.size(); i++){					
					Session s = sessions.get(i);
					try {
						s.call();
						informProgress(i+1);
					} catch (Exception e) {
//						e.printStackTrace();
						String message; 
						if (e.getMessage() == null || e.getMessage().isEmpty()){
							message = "Unknown exception during AC testing!";
						} else 
							message = e.getMessage();
						notifyFailure(message);
						return;
//							throw new RuntimeException(e.getLocalizedMessage());
					}
				}

				// end execution, store result and inform listener
//				List<Session> storedResult = appModel.getExecutionResults();
//				synchronized(storedResult){
//					for (Session s : sessions){
//						storedResult.add(s);
//					}
//				}
				notifyDoneExecution();
			}
		});
		
		try {
			worker.start();
//			worker.join();
		} catch (RuntimeException e){
			throw new ExecutorException(e.getLocalizedMessage());
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}

}
