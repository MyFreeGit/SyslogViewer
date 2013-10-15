package com.roland.syslog.model;

import java.util.*;

public class LogContainer {
	public LogContainer(){
		itemList = new LinkedList<ILogItem>();
		finder = new Finder();
	}
	
	public boolean add(ILogItem item){
		return itemList.add(item);
	}
	
	public List<ILogItem> getLogItemList(){
		return itemList;
	}

	private List<ILogItem> itemList;
	
	public LogContainer findAll(String str){
		return finder.findAll(str);
	}
	
	public ILogItem findNext(String str){
		return finder.findNext(str);
	}
	 
	public ILogItem findPrev(String str){
		return finder.findPrev(str);
	}
		
	private enum Direction{
		PREV, NEXT;
	}
	
	private class Finder{
		private ILogItem pos = null;


		public LogContainer findAll(String str){
			LogContainer result = new LogContainer();
			pos = null;
			for(ILogItem item : itemList){
				if(item.toString().contains(str)){
					result.add(item);
				}
			}
			return result;
		}
		
		private ListIterator<ILogItem> getIterator(Direction dir){
			ListIterator<ILogItem> iter = null;
			if(pos == null){
				if(dir == Direction.NEXT){
					iter = itemList.listIterator();
				}else if(dir == Direction.PREV){
					iter = itemList.listIterator(itemList.size());
				}
			}else{
				iter = itemList.listIterator(itemList.indexOf(pos));
				if(dir == Direction.NEXT){
					iter.next();
				}
			}
			return iter;
		}

		public ILogItem findNext(String str){
			ILogItem result = null;
			ListIterator<ILogItem> iter = getIterator(Direction.NEXT);
			while(iter.hasNext()){
				result = iter.next();
				if(result.toString().contains(str)){
					pos = result;
					return result;
				}
			}
			if(pos != null){
				iter = itemList.listIterator();
				while(iter.hasNext()){
					result = iter.next();
					if(result.toString().contains(str)){
						pos = result;
						return result;
					}
				}
			}
			return null;
		}
		
		public ILogItem findPrev(String str){
			ILogItem result = null;
			ListIterator<ILogItem> iter = getIterator(Direction.PREV);
			while(iter.hasPrevious()){
				result = iter.previous();
				if(result.toString().contains(str)){
					pos = result;
					return result;
				}
			}
			if(pos != null){
				iter = itemList.listIterator(itemList.size());
				while(iter.hasPrevious()){
					result = iter.previous();
					if(result.toString().contains(str)){
						pos = result;
						return result;
					}
				}
			}
			return null;
		}
	} 
	private Finder finder;
}
