package at.majcen.functions;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FunctionsTest {

	@Test
	public void testMap1() {
		List<String> stringList = new ArrayList<>(5);
		stringList.add("Hello");
		stringList.add("darkness");
		stringList.add("my");
		stringList.add("old");
		stringList.add("friend");

		List<Integer> result = Functions.map(stringList, String::length);
		List<Integer> expected = new ArrayList<>(5);
		expected.add(5);
		expected.add(8);
		expected.add(2);
		expected.add(3);
		expected.add(6);

		Assert.assertEquals(expected, result);
	}

	@Test
	public void testMap2() {
		List<Integer> stringList = new ArrayList<>(5);
		stringList.add(1);
		stringList.add(2);
		stringList.add(3);
		stringList.add(4);
		stringList.add(5);

		List<Double> result = Functions.map(stringList, Integer::doubleValue);
		List<Double> expected = new ArrayList<>(5);
		expected.add(1.);
		expected.add(2.);
		expected.add(3.);
		expected.add(4.);
		expected.add(5.);

		Assert.assertEquals(expected, result);
	}

	@Test
	public void testMap3() {
		List<String> stringList = new ArrayList<>(5);
		stringList.add("12");
		stringList.add("23");
		stringList.add("42");
		stringList.add("89");
		stringList.add("14");

		List<Integer> result = Functions.map(stringList, Integer::parseInt);
		List<Integer> expected = new ArrayList<>(5);
		expected.add(12);
		expected.add(23);
		expected.add(42);
		expected.add(89);
		expected.add(14);

		Assert.assertEquals(expected, result);
	}

	@Test
	public void testFold1() {
		List<String> stringList = new ArrayList<>();
		stringList.add("Hello ");
		stringList.add("darkness ");
		stringList.add("my ");
		stringList.add("old ");
		stringList.add("friend");
		String result = Functions.fold(stringList, "", (string1, string2) -> string1 + string2);
		Assert.assertEquals("Hello darkness my old friend", result);
	}

	@Test
	public void testFold2() {
		List<Integer> integerList = new ArrayList<>();
		integerList.add(1);
		integerList.add(2);
		integerList.add(3);
		integerList.add(4);
		integerList.add(5);
		int result = Functions.fold(integerList, 0, (int1, int2) -> int1 + int2);
		Assert.assertEquals(15, result);
	}

	@Test
	public void testFold3() {
		List<Double> doubleList = new ArrayList<>();
		doubleList.add(1.);
		doubleList.add(2.);
		doubleList.add(3.);
		doubleList.add(4.);
		doubleList.add(5.);

		double result = Functions.fold(doubleList, 1., (double1, double2) -> double1 * double2);
		Assert.assertEquals(120., result, 0.0001);
	}

	@Test
	public void testDivideAndConquer_QuickSort() {
		final Function<List<Integer>, Boolean> trivial = integers -> integers.size() <= 1;
		final Function<List<Integer>, List<Integer>> solve = integer -> integer;
		final Function<List<Integer>, Functions.Pair<List<Integer>>> divide = integers -> Functions.divideByPivot((integer) -> integer < integers.get(0), integers);
		final Function<Functions.Pair<List<Integer>>, List<Integer>> combine = listPair -> {
			List<Integer> integers = new ArrayList<>(listPair.first);
			integers.addAll(listPair.second);
			return integers;
		};

		final List<Integer> integers = List.of(5, 7, 3, 9, 11, 1, 8, 15, 3, 17);

		List<Integer> actual = Functions.divideAndConquer(trivial, solve, divide, combine, integers);
		final List<Integer> expected = List.of(1, 3, 3, 5, 7, 8, 9, 11, 15, 17);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testDivideAndConquer_Power() {
		final Function<Functions.Pair<Integer>, Boolean> trivial = integerPair -> integerPair.second == 1;
		final Function<Functions.Pair<Integer>, Integer> solve = integerPair -> integerPair.first;
		final Function<Functions.Pair<Integer>, Functions.Pair<Functions.Pair<Integer>>> divide = integerPair -> {
			final Integer base = integerPair.first;
			final Integer pow = integerPair.second;
			final Integer powDiv = pow / 2;
			return new Functions.Pair<>(new Functions.Pair<>(base, powDiv), new Functions.Pair<>(base, pow - powDiv));
		};
		final Function<Functions.Pair<Integer>, Integer> combine = integerPair -> integerPair.first * integerPair.second;

		final Functions.Pair<Integer> input = new Functions.Pair<>(3, 16);

		final Integer actual = Functions.divideAndConquer(trivial,solve,divide,combine,input);
		final Integer expected = 43046721;

		Assert.assertEquals(expected,actual);

	}

}
