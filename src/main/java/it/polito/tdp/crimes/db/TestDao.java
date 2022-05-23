package it.polito.tdp.crimes.db;

public class TestDao {

	public static void main(String[] args) {
		EventsDao dao = new EventsDao();
		for(String e : dao.getVertici("traffic-accident", 2))
			System.out.println(e);
	}

}
