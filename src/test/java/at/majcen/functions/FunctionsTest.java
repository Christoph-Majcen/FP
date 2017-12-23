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

	private Function<List<Integer>, Boolean> getQuickSortTrivial() {
		return integers -> integers.size() <= 1;
	}

	private Function<List<Integer>, List<Integer>> getQuickSortSolve() {
		return integer -> integer;
	}

	private Function<List<Integer>, Functions.Pair<List<Integer>>> getQuickSortDivide() {
		return integers -> Functions.divideByPivot((integer) -> integer < integers.get(0), integers);
	}

	private Function<Functions.Pair<List<Integer>>, List<Integer>> getQuickSortCombine() {
		return listPair -> {
			List<Integer> integers = new ArrayList<>(listPair.first);
			integers.addAll(listPair.second);
			return integers;
		};
	}

	private List<Integer> executeQuickSort(List<Integer> integers) {
		final Function<List<Integer>, Boolean> trivial = getQuickSortTrivial();
		final Function<List<Integer>, List<Integer>> solve = getQuickSortSolve();
		final Function<List<Integer>, Functions.Pair<List<Integer>>> divide = getQuickSortDivide();
		final Function<Functions.Pair<List<Integer>>, List<Integer>> combine = getQuickSortCombine();

		return Functions.divideAndConquer(trivial, solve, divide, combine, integers);
	}

	@Test
	public void testDivideAndConquer_QuickSort1() {
		final List<Integer> integers = List.of(5, 7, 3, 9, 11, 1, 8, 15, 3, 17);
		final List<Integer> actual = executeQuickSort(integers);
		assertThat(actual).containsExactly(1, 3, 3, 5, 7, 8, 9, 11, 15, 17);
	}

	@Test
	public void testDivideAndConquer_QuickSort2() {
		final List<Integer> integers = List.of(6, 6, 6, 6, 6, 6, 6, 6, 6, 6);
		final List<Integer> actual = executeQuickSort(integers);
		assertThat(actual).containsExactly(6, 6, 6, 6, 6, 6, 6, 6, 6, 6);
	}

	@Test
	public void testDivideAndConquer_QuickSort3() {
		final List<Integer> integers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		List<Integer> actual = executeQuickSort(integers);
		assertThat(actual).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
	}

	private Function<Functions.Pair<Integer>, Boolean> getPowerTrivial() {
		return integerPair -> integerPair.second == 1 || integerPair.second == 0;
	}

	private Function<Functions.Pair<Integer>, Integer> getPowerSolve() {
		return integerPair -> {
			if(integerPair.second == 0) {
				return 1;
			}
			return integerPair.first;
		};
	}

	private Function<Functions.Pair<Integer>, Functions.Pair<Functions.Pair<Integer>>> getPowerDivide() {
		return integerPair -> {
			final Integer base = integerPair.first;
			final Integer pow = integerPair.second;
			final Integer powDiv = pow / 2;
			return new Functions.Pair<>(new Functions.Pair<>(base, powDiv), new Functions.Pair<>(base, pow - powDiv));
		};
	}

	private Function<Functions.Pair<Integer>, Integer> getPowerCombine() {
		return integerPair -> integerPair.first * integerPair.second;
	}

	// Note: Power only works for positive Integers
	private Integer executePower(Functions.Pair<Integer> input) {
		final Function<Functions.Pair<Integer>, Boolean> trivial = getPowerTrivial();
		final Function<Functions.Pair<Integer>, Integer> solve = getPowerSolve();
		final Function<Functions.Pair<Integer>, Functions.Pair<Functions.Pair<Integer>>> divide = getPowerDivide();
		final Function<Functions.Pair<Integer>, Integer> combine = getPowerCombine();

		return Functions.divideAndConquer(trivial, solve, divide, combine, input);
	}

	@Test
	public void testDivideAndConquer_Power1() {
		final Functions.Pair<Integer> input = new Functions.Pair<>(3, 16);
		final Integer actual = executePower(input);
		assertThat(actual).isEqualTo(43046721);
	}

	@Test
	public void testDivideAndConquer_Power2() {
		final Functions.Pair<Integer> input = new Functions.Pair<>(4, 3);
		final Integer actual = executePower(input);
		assertThat(actual).isEqualTo(64);
	}

	@Test
	public void testDivideAndConquer_Power3() {
		final Functions.Pair<Integer> input = new Functions.Pair<>(3, 0);
		final Integer actual = executePower(input);
		assertThat(actual).isEqualTo(1);
	}

}
