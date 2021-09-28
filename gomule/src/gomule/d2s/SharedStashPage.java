package gomule.item;

import java.util.ArrayList;

public class SharedStashPage {

	private final long MAX_STASHGOLD = 2500000;

	private ArrayList<D2Item> items;
	private long gold;
	
	public SharedStashPage() {
		items = new ArrayList<>();
		gold = 0;
	}

	public void addItem(D2Item d2i) {
		items.add(d2i);
	}

	public D2Item getItemAt(int i) {
		return items.get(i);
	}

	public ArrayList getItems() {
		return items;
	}

	public D2Item removeItemAt(int i) {
		return items.remove(i);
	}

	public long getGold(long x) {
		long tmp = x;

		if(x>gold) tmp = gold;
		gold -= tmp;

		return tmp;
	}


	public long putGold(long x) {
		long tmp = x;

		if(gold+x>MAX_STASHGOLD) tmp = (gold+x) - MAX_STASHGOLD;
		gold += tmp;
		
		return tmp;
	}
}
