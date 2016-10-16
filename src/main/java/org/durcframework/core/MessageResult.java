package org.durcframework.core;

import java.util.List;

public interface MessageResult {

	void setSuccess(boolean success);

	void setMessage(String message);

	void setMessages(List<String> messages);
}
