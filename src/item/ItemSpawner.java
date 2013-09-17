package item;
import game.BallGameStatic;


public class ItemSpawner
{	
	long lastSpawn;
	long nextSpawn;
	int minDelay;
	int maxDelay;


	public enum ItemType
	{					
		HEALTH		{public void addItem(){
            BallGameStatic.items.add(new HealthItem());}},
		SHIELD		{public void addItem(){
            BallGameStatic.items.add(new ShieldItem());}},
		ORBITAL		{public void addItem(){
            BallGameStatic.items.add(new OrbitalItem());}},
		TRIPLESHOT	{public void addItem(){
            BallGameStatic.items.add(new TripleShotItem());}},
		FASTSHOT	{public void addItem(){
            BallGameStatic.items.add(new FastShotItem());}};
		
		public void addItem()
		{
			BallGameStatic.items.add(new Item());
		}
	}
	
	public ItemSpawner(int min, int max)
	{
		minDelay = min;
		maxDelay = max;
		
		lastSpawn = System.currentTimeMillis();
	}

	public void attemptSpawn()
	{
		if(System.currentTimeMillis() >= nextSpawn)
		{
			addRandomItem();
			int delay = (int)(Math.random()*(maxDelay - minDelay))+minDelay;
			lastSpawn = System.currentTimeMillis();
			nextSpawn = lastSpawn + delay*1000;
		}
	}

	void addRandomItem()
	{				
		ItemType item = ItemType.values()[(int)(Math.random()*ItemType.values().length)];
		item.addItem();
	}
}
