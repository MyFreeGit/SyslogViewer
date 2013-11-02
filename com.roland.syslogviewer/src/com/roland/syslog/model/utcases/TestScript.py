from com.roland.syslog.model import *

list = SYSLOG.getLogItemList();
size = list.size();
print "Hello World!"
for i in range(size):
	RESULT.add(list.get(size - i - 1));
