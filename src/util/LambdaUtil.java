package util;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LambdaUtil {
	
	public static void main(String[] args){
		
	}
	
	
	/**
	 * @param items the collection that will be filtered
	 * @param predicate the predicate to base the filtering on
	 * @return a collection of items filtered based on the predicate
	 */
	static public <T> Collection<T> filter(Collection<T> items, Predicate<T> predicate){
		return items.stream()
				.filter(t -> predicate.test(t))
				.collect(Collectors.toList());
	}
	
	/**
	 * @param items the collection that will be mapped
	 * @param function the function to map the input to the output
	 * @return A Collection of items mapped to their output based on the function passed
	 */
	static public <I,O> Collection<O> map(Collection<I> items, Function<I,O> function){
		return items.stream()
				.map(i -> function.apply(i))
				.collect(Collectors.toList());
	}

}
