package com.adsquare.processor;

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
			} else if (number > second && number != first) {
				second = number;
			}
		}

		firstSecondList.add(first);
		firstSecondList.add(second);
	}

	public Integer findSecondSimpleFor(List<Integer> millionList) {
		int first = 0;
		int second = 0;

		Long start = System.currentTimeMillis();

		for (int i = 0; i < millionList.size(); i++) {
			if (millionList.get(i) > first) {
				second = first;
				first = millionList.get(i);
			} else if (millionList.get(i) > second && millionList.get(i) != first) {
				second = millionList.get(i);
			}
		}

		Long end = System.currentTimeMillis();

		System.out.println("Total time taken findSecondSimpleFor : " + (end - start));

		return second;
	}
}
