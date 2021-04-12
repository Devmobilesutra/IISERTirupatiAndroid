package com.mobilesutra.iiser_tirupati.Error_Activity;

public interface AsyncResponse<T> {

	 void processFinish(T output);
	 
	 void processFinishLog(T output);
	 
}
