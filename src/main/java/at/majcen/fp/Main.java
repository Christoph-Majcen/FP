package at.majcen.fp;


import at.majcen.functions.Functions;

import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		List<Integer> test2 = new ArrayList<>(3);
		test2.add(1);
		test2.add(2);
		test2.add(3);
		test2.add(4);
		test2.add(5);
		List<String> returnList = Functions.scan(test2, "", (string, integer) -> string + integer);

		returnList.forEach(System.out::println);
	}
}
