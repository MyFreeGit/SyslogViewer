package com.roland.syslog.model;

import java.util.*;

import com.roland.syslog.model.ILogItem.Field;


/**
 * LogContainer is only for contains the log that is read-only after being 
 * created. And it implements ILogNavigator interface, that allows the user GUI
 * to navigate Log position.
 */
public class LogContainer extends AbstractLogSet implements ILogNavigator {
	private final static String UNSUPPORTED_OPERATION_MSG = "Modification on SYSLOG isn't allowed!";
	public LogContainer(){
		super();
		finder = new Finder();
	}

	/*When user calls the routine that modified the inner data, just throw 
	 *runtime exception makes the execution failure!*/
	@Override public void clear(){throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);}

	@Override public boolean add(ILogItem item){throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);}
	
	@Override public boolean addAll(ILogSet logs){throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);}
	
	@Override public ILogSet sort(Field sortBy){throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MSG);}
	

	@Override protected List<ILogItem> createLogList(){
		return new LinkedList<ILogItem>();
	}
	
	@Override public ILogSet findAll(String str){
		return finder.findAll(str);
	}
	
	@Override  public ILogItem findNext(String str){
		return finder.findNext(Comparator.FIND, str);
	}
	 
	@Override public ILogItem findPrev(String str){
		return finder.findPrev(Comparator.FIND, str);
	}
	
	@Override public ILogItem navNext(){
		return finder.findNext(Comparator.SELECT, null);
	}
	
	@Override public ILogItem navPrev(){
		return finder.findPrev(Comparator.SELECT, null);
	}

	@Override public void setPosition(ILogItem pos){
		finder.setPosition(pos);
	}
	
	public boolean saveToFile(String fileName){
		
		return true;
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

		public ILogSet findAll(String str){
			ResultLogList result = new ResultLogList();
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
