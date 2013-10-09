from com.roland.syslog.model import *
import re

print dir()
list = LogContainer.getLogItemList();
temp =  LogItemsContainer()
size = list.size();
for i in range(size):
    print(list.get(i).getLogText());
    result.add(list.get(size - i - 1));
