package net.swa.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {
	private long onlineCount;

	public void sessionCreated(HttpSessionEvent event) {
		onlineCount++;
		event.getSession().setAttribute("_onlineCount", onlineCount);
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		if (onlineCount > 0) {
			onlineCount--;
		}
		event.getSession().setAttribute("_onlineCount", onlineCount);
	}

}
