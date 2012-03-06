package LUDOSimulator;

public interface GameEventListener {
	public enum EventType {pieceMoved,gameEnded,nextPlayer;}
	public void gameEvent(LUDOBoard board, EventType eventType, Object object);
}
