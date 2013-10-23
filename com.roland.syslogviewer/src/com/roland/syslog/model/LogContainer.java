package com.roland.syslog.model;

import java.util.*;

public class LogContainer {
	public LogContainer(){
		itemList = new LinkedList<ILogItem>();
		finder = new Finder();
	}
	
	public boolean isEmpty(){
		return !(itemList.size() > 0);
	}
	
	public void clear(){
		itemList.clear();
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
		return finder.findNext(Comparator.FIND, str);
	}
	 
	public ILogItem findPrev(String str){
		return finder.findPrev(Comparator.FIND, str);
	}
	
	public ILogItem navNext(){
		return finder.findNext(Comparator.SELECT, null);
	}
	
	public ILogItem navPrev(){
		return finder.findPrev(Comparator.SELECT, null);
	}

	public void selectAll(){
		for(ILogItem item : itemList){
			item.select();
		}
	}
	
	public void setPosition(ILogItem pos){
		finder.setPosition(pos);
	}
	
	public void unselectAll(){
		for(ILogItem item : itemList){
			item.unselect();
		}	
	}
	
	public int getSelectCount(){
		int count = 0;
		for(ILogItem item : itemList){
			if(item.isSelected()){
				count++; 
			}
		}
		return count;
	}
	
	public void setSelected(LogContainer logs){
		for(ILogItem item: logs.getLogItemList()){
			item.select();
		}
	}
	
	private enum Direction{
		PREV, NEXT;
	}
	
	private enum Comparator{
		FIND {
			@Override
			boolean isRightOne(Object obj, Object condit) {
				return obj.toString().contains((String)condit);
			}},
		SELECT{
			@Override
			boolean isRightOne(Object obj, Object condit) {
				return ((ILogItem)obj).isSelected();
			}};
		abstract boolean isRightOne(Object obj, Object condit);
	}
	
	private class Finder{
		private ILogItem pos = null;

		public void setPosition(ILogItem pos){
			this.pos = pos;
		}

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

		public ILogItem findNext(Comparator cmptor, String str){
			ILogItem result = null;
			ListIterator<ILogItem> iter = getIterator(Direction.NEXT);
			while(iter.hasNext()){
				result = iter.next();
				if(cmptor.isRightOne(result, str)){
					pos = result;
					return result;
				}
			}
			if(pos != null){
				iter = itemList.listIterator();
				while(iter.hasNext()){
					result = iter.next();
					if(cmptor.isRightOne(result, str)){
						pos = result;
						return result;
					}
				}
			}
			return null;
		}
		
		public ILogItem findPrev(Comparator cmptor, String str){
			ILogItem result = null;
			ListIterator<ILogItem> iter = getIterator(Direction.PREV);
			while(iter.hasPrevious()){
				result = iter.previous();
				if(cmptor.isRightOne(result, str)){
					pos = result;
					return result;
				}
			}
			if(pos != null){
				iter = itemList.listIterator(itemList.size());
				while(iter.hasPrevious()){
					result = iter.previous();
					if(cmptor.isRightOne(result, str)){
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
