package gamedata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds the lineage of ActorData to allow
 * for upgrading.
 * 
 * @author maddiebriere
 *
 */

public class LineageData {
	private Map<Integer,ActorData> generations;
	private int current;
	
	public LineageData(ActorData progenitor){
		generations = new HashMap<Integer,ActorData>();
		current = 0;
		generations.put(current, progenitor);
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj == null){
			return false;
		}
		if(!(obj instanceof LineageData)){
			return false;
		}
		LineageData lin = (LineageData)obj;
		return lin.getProgenitor().equals(this.getProgenitor());
	}
	
	public void addGeneration(ActorData newbie){
		generations.put(++current, newbie);
	}
	
	public ActorData getProgenitor(){
		return generations.get(0);
	}
	
	public ActorData getCurrent(){
		return generations.get(current);
	}

	public Map<Integer, ActorData> getMap(){
		return this.generations;
	}
	
	public void upgrade(){
		if(generations.containsKey(current+1)){
			current++;
		}
	}
}

