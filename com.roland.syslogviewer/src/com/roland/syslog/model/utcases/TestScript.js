list = LogContainer.getLogItemList();
size = list.size();
for (i = 0; i < size; i++){
    println(list.get(i).getLogText());
    result.add(list.get(size - i - 1));
}
