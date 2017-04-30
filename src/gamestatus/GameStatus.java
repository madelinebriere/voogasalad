package gamestatus;

import java.util.HashMap;	

import ui.player.users.InitialGameStatus;
import ui.player.users.WriteableUser;
import util.observerobservable.VoogaObservableMap;

/**
 * An Observable map of Game Status indicators
 * @author sarahzhou
 *
 */
public class GameStatus extends VoogaObservableMap<String,String> implements WriteableGameStatus, ReadableGameStatus{

	private final String EXPERIENCE = "Experience";
	private final String MONEY = "Money";
	private final String LEVEL = "Level";
	private final String LIVES = "Lives";
	
	private final static int INIT_LIVES = 3;
	
	private WriteableUser myWriteableUser;
	private InitialGameStatus myInitialGameStatus;
	
	public GameStatus(WriteableUser writeableUser,InitialGameStatus initialGameStatus) {
		myWriteableUser =writeableUser;
		myInitialGameStatus = initialGameStatus;
		setInitialValues();
	}
	
	public void setInitialValues() {
		setMyMoney(myInitialGameStatus.getInitMoney());
		setMyLevel(myInitialGameStatus.getInitLevel());
		setMyExperience(myInitialGameStatus.getInitExp());
		setMyLives(INIT_LIVES);
	}

	public void addExperience(double exp){
		setMyExperience(getExp()+exp);
	}

	public void addMoney(double mon){
		setMyMoney(getMoney()+mon);
	}

	public void spendMoney(double mon){
		setMyMoney(getMoney()-mon);
	}

	public void levelUp() {
		int newLevel = getLevel()+1;
		setMyLevel(newLevel);
	}

	public void setMyExperience(double myExperience) {
		myWriteableUser.setExperience(myExperience);
		myMap.put(EXPERIENCE, Double.toString(myExperience));
		notifyObservers();
	}

	public void setMyMoney(double myMoney) {
		myWriteableUser.setMoney(myMoney);
		myMap.put(MONEY, Double.toString(myMoney));
		notifyObservers();
	}

	public void setMyLevel(int myLevel) {
		myWriteableUser.setLevel(myLevel);
		myMap.put(LEVEL, Integer.toString(myLevel));
		notifyObservers();
	}
	
	@Override
	public void loseLife() {
		setMyLives(getLives()-1);
	}

	@Override
	public void gainLife() {
		setMyLives(getLives()+1);
	}
	
	public void setMyLives(int lives) {
		myMap.put(LIVES, Integer.toString(lives));
		notifyObservers();
	}

	@Override
	public double getExp() {
		return Double.parseDouble(myMap.get(EXPERIENCE));
	}

	@Override
	public double getMoney() {
		return Double.parseDouble(myMap.get(MONEY));
	}

	@Override
	public int getLevel() {
		return Integer.parseInt(myMap.get(LEVEL));
	}

	@Override
	public int getLives() {
		return Integer.parseInt(myMap.get(LIVES));
	}
	
}
