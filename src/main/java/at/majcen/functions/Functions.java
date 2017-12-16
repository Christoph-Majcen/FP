package at.majcen.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class Functions {

	public static <T, R> List<R> map(List<T> list, Function<T, R> func) {
		if (list.size() == 0) {
			return Collections.emptyList();
		}
		final List<R> returnList = new ArrayList<>(list.size());
		returnList.add(func.apply(list.get(0)));
		returnList.addAll(map(list.subList(1, list.size()), func));
		return returnList;
	}

	public static <T> T fold(List<T> list, T initial, BinaryOperator<T> func) {
		if (list.size() == 0) {
			return initial;
		}
		if (list.size() == 1) {
			return func.apply(initial, list.get(0));
		}
		return fold(list.subList(1, list.size()), func.apply(initial, list.get(0)), func);
	}

	public static <T, R> List<R> scan(List<T> list, R element, BiFunction<R, T, R> func) {
		if (list.size() == 0) {
			return Collections.singletonList(element);
		}
		if (list.size() == 1) {
			return Collections.singletonList(func.apply(element, list.get(0)));
		}
		final List<R> returnList = new ArrayList<>(list.size());
		final R newElement = func.apply(element, list.get(0));
		returnList.add(newElement);
		returnList.addAll(scan(list.subList(1, list.size()), newElement, func));
		return returnList;
	}

	public static <T,R> R divideAndConquer(Function<T, Boolean> trivial, Function<T,R> solve, Function<T,Pair<T>> divide, Function<Pair<R>,R> combine, T input) {
		if(trivial.apply(input)) {
			return solve.apply(input);
		}
		Pair<T> divided = divide.apply(input);

		R first = divideAndConquer(trivial,solve,divide,combine,divided.first);
		R second = divideAndConquer(trivial,solve,divide,combine,divided.second);
		return combine.apply(new Pair<>(first, second));
	}

	public static <T> Pair<List<T>> divideByPivot(Function<T,Boolean> criteria,List<T> input) {
		if(input.isEmpty()) {
			return new Pair<>(new ArrayList<>(), new ArrayList<>());
		}
		T pivot = input.get(0);
		Pair<List<T>> divided = divideInternal(criteria, input.subList(1,input.size()));
		if(divided.second.isEmpty()) {
			divided.second.add(pivot);
		} else {
			divided.first.add(pivot);
		}
		return divided;
	}

	private static <T> Pair<List<T>> divideInternal(Function<T,Boolean> criteria, List<T> input){
		if(input.isEmpty()) {
			return new Pair<>(new ArrayList<>(), new ArrayList<>());
		}

		if(input.size() == 1) {
			return partitionFirstElement(criteria, input.get(0));
		}
		Pair<List<T>> pairFirst = partitionFirstElement(criteria,input.get(0));
		Pair<List<T>> pair = divideInternal(criteria, input.subList(1,input.size()));
		pair.first.addAll(pairFirst.first);
		pair.second.addAll(pairFirst.second);
		return pair;
	}

	private static <T> Pair<List<T>> partitionFirstElement(Function<T, Boolean> criteria, T element) {
		List<T> returnList = new ArrayList<>();
		returnList.add(element);
		if(criteria.apply(element)) {
			return new Pair<>(returnList, new ArrayList<>());
		}
		return new Pair<>(new ArrayList<>(), returnList);
	}

	public static class Pair<T> {
		public final T first;
		public final T second;

		@SuppressWarnings("WeakerAccess")
		public Pair(T first, T second) {
			this.first = first;
			this.second = second;
		}
	}

}
