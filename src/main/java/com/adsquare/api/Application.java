package com.adsquare.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import com.adsquare.processor.Processor;

public class Application {

	public static void main(String[] args) {

		List<Integer> millionList = new ArrayList<>();

		// generate random a million integers
		int[] randomList = IntStream.generate(() -> new Random().nextInt(100)).limit(1000000).toArray();

		for (int n : randomList) {
			millionList.add(n);
		}

		System.out.println("Max is : " + Collections.max(millionList));

		//Using simple for loop. Total time taken around 30 miliSeconds
		int secondHighest = new Processor().findSecondSimpleFor(millionList);

		System.out.println("SecondHighest element is : " + secondHighest);

		//Executing multiple threads. Multiple threads are created to find 1st and 2nd from each thread.
		//Once each thread is executed collecting input in an array and finding 2nd highest.
		//Total time taken is : 35 ms. This can be further optimized.

		secondHighest = new Application().executeParallel(millionList);
		System.out.println("SecondHighest element using executeParallel is : " + secondHighest);
	}

	public int executeParallel(List<Integer> millionList) {

		Long startTime = System.currentTimeMillis();

		int n = millionList.size();

		//This can be split into multiple subLists for tuning the performance
		int k = 2;

		List<List<Integer>> listOfSubLists = new ArrayList<>();

		for (int i = 0; i < k; i++) {
			int start = k * i;
			int end = k * (i + 1) > n ? n : n / k * (i + 1);
			listOfSubLists.add(millionList.subList(start, end));
		}

		List<Integer> firstSecondList = new ArrayList<>();

		ExecutorService executorService = Executors.newFixedThreadPool(k);

		for (int i = 0; i < listOfSubLists.size(); i++) {
			executorService.execute(new Processor(listOfSubLists.get(i), firstSecondList));
		}

		executorService.shutdown();

		//Wait till all tasks are complete
		while (!executorService.isTerminated()) {
		}

		int secondHighest = 0;

		if (executorService.isTerminated()) {
			secondHighest = new Processor().findSecondSimpleFor(firstSecondList);
		}

		Long endTime = System.currentTimeMillis();

		System.out.println("Total time taken by executeParallel  : " + (endTime - startTime));

		return secondHighest;
	}
}
