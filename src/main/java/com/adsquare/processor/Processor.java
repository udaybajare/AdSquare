package com.adsquare.processor;

import java.util.Comparator;
import java.util.List;

public class Processor implements Runnable {

	List<Integer> subList;
	List<Integer> firstSecondList;

	public Processor() {

	}

	public Processor(List<Integer> millionList, List<Integer> firstSecondList) {
		this.subList = millionList;
		this.firstSecondList = firstSecondList;
	}

	@Override
	public void run() {
		int first = 0;
		int second = 0;

		for (int number : subList) {
			if (number > first) {
				second = first;
				first = number;
			} else if (number > second) {
				second = number;
			}

			//System.out.println("First and Second are : " + first + " : " + second);
		}

		//findSecondSimpleFor(subList);

		firstSecondList.add(first);
		firstSecondList.add(second);

	}

	public Integer findSecondSimpleFor(List<Integer> millionList) {
		int first = 0;
		int second = 0;

		Long start = System.currentTimeMillis();

		//1,3,6,2,5,78,54,1,36
		for (int number : millionList) {
			if (number > first) {
				second = first;
				first = number;
			} else if (number > second) {
				second = number;
			}
		}

		Long end = System.currentTimeMillis();

		//System.out.println("first and second are " + first + " and " + second);
		System.out.println("Total time taken findSecondSimpleFor : " + (end - start));

		return second;
	}

	public Integer findSecondUsingStream(List<Integer> millionList) {
		int first = 0;
		int second = 0;

		Long start = System.currentTimeMillis();
		second = millionList.stream().sorted(Comparator.reverseOrder()).limit(2).skip(1).findFirst().get();
		Long end = System.currentTimeMillis();

		System.out.println("Total time taken findSecondUsingStream : " + (end - start));

		return second;
	}

	public Integer findSecondUsingParallelStream(List<Integer> millionList) {
		int first = 0;
		int second = 0;

		Long start = System.currentTimeMillis();
		second = millionList.parallelStream().sorted(Comparator.reverseOrder()).limit(2).skip(1).findFirst().get();
		Long end = System.currentTimeMillis();

		System.out.println("Total time taken findSecondUsingParallelStream : " + (end - start));

		return second;
	}
}
