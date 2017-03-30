package gameengine.actors;

public interface SmartActor {

	public void move(Path path);
	
	public void attack(IGameObject gameObject);
}
