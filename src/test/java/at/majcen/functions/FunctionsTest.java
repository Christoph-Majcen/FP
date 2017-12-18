package at.majcen.functions;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class FunctionsTest {

	@Test
	public void testMap1() {
		List<String> stringList = List.of("Hello", "darkness", "my", "old", "friend");

		List<Integer> result = Functions.map(stringList, String::length);

		assertThat(result).containsExactly(5, 8, 2, 3, 6);
	}

	@Test
	public void testMap2() {
		List<Integer> intList = List.of(1, 2, 3, 4, 5);

		List<Double> result = Functions.map(intList, Integer::doubleValue);

		assertThat(result).containsExactly(1., 2., 3., 4., 5.);
	}

	@Test
	public void testMap3() {
		final List<String> stringList = List.of("12", "23", "42", "89", "14");

		List<Integer> result = Functions.map(stringList, Integer::parseInt);

		assertThat(result).containsExactly(12, 23, 42, 89, 14);
	}

	@Test
	public void testFold1() {
		List<String> stringList = List.of("Hello ", "darkness ", "my ", "old ", "friend");

		String result = Functions.fold(stringList, "", (string1, string2) -> string1 + string2);

		assertThat(result).isEqualTo("Hello darkness my old friend");
	}

	@Test
	public void testFold2() {
		List<Integer> intList = List.of(1, 2, 3, 4, 5);

		int result = Functions.fold(intList, 0, (int1, int2) -> int1 + int2);

		assertThat(result).isEqualTo(15);
	}

	@Test
	public void testFold3() {
		List<Double> doubleList = List.of(1., 2., 3., 4., 5.);

		double result = Functions.fold(doubleList, 1., (double1, double2) -> double1 * double2);

		assertThat(result).isEqualTo(120.);
	}

	@Test
	public void testScan1() {
		List<Integer> intList = List.of(1,2,3,4,5);

		List<Integer> result = Functions.scan(intList, 0, (int1, int2) -> int1 + int2);

		assertThat(result).containsExactly(1, 3, 6, 10, 15);
	}

	@Test
	public void testScan2() {
		List<Double> doubleList = List.of(1., 2., 3., 4., 5.);

		List<Double> result = Functions.scan(doubleList, 1., (double1, double2) -> double1 * double2);

		assertThat(result).containsExactly(1.0, 2.0, 6.0, 24.0, 120.);
	}

	@Test
	public void testScan3() {
		List<String> stringList = List.of("Hello ", "darkness ", "my ", "old ", "friend");

		List<String> result = Functions.scan(stringList, "", (string1, string2) -> string1 + string2);

		assertThat(result).containsExactly("Hello ", "Hello darkness ", "Hello darkness my ", "Hello darkness my old ", "Hello darkness my old friend");
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

		assertThat(actual).containsExactly(1, 3, 3, 5, 7, 8, 9, 11, 15, 17);
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

		final Integer actual = Functions.divideAndConquer(trivial, solve, divide, combine, input);

		assertThat(actual).isEqualTo(43046721);

	}

}
