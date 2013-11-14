package com.roland.syslogviewer.parts;

import java.util.List;

public interface IPersistServiceProvider {
	public List<String> getSearchHistory();
	public void addSearchText(String text);
	public void setScriptPath(String text);
	public String getScriptPath();
}
