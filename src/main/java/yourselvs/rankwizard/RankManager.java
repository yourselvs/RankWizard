package yourselvs.rankwizard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RankManager {
	RankWizard instance;
	private List<RankClass> classes;
	private String defaultClass;
	
	public RankManager(RankWizard instance) {
		this.instance = instance;
		classes = new ArrayList<RankClass>();
	}
	
	public void addClass(RankClass newClass) {
		classes.add(newClass);
	}
	
	public void setDefaultClass(String defaultClass) {
		this.defaultClass = defaultClass;
	}
	
	public RankClass removeClass(String classStr) {
		for(int i = 0; i < classes.size(); i++) {
			if(classes.get(i).getName().equals(classStr)) {
				return classes.remove(i);
			}
		}
		return null;
	}
	
	public boolean containsClass(String classStr) {
		for(RankClass classObj : classes) {
			if(classObj.getName().equals(classStr)) {
				return true;
			}
		}
		return false;
	}
	
	public Set<RankClass> getClassesWithRank(String str) {
		Set<RankClass> classes = new HashSet<RankClass>();
		
		for(RankClass classObj : this.classes) {
			if(classObj.containsRank(str)) {
				classes.add(classObj);
			}
		}
		
		return classes;
	}
	
	public RankClass getDefaultClass() {
		for(RankClass classObj : classes) {
			if(classObj.getName().equals(defaultClass)) {
				return classObj;
			}
		}
		
		return null;
	}
	
	public RankClass getClass(String classStr) {
		for(RankClass classObj : classes) {
			if(classObj.getName().equals(classStr)) {
				return classObj;
			}
		}
		
		return null;
	}
}
