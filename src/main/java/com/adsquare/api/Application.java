package com.adsquare.api;

import java.util.ArrayList;
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
		IntStream.generate(() -> new Random().nextInt(100) + 100).limit(1000001).forEach((n) -> {
			millionList.add(n);
		});

		//Using simple for loop. Total time taken around 30 miliSeconds
		int secondHighest = new Processor().findSecondSimpleFor(millionList);

		System.out.println("SecondHighest element is : " + secondHighest);

		//Using stream sorted. Total time taken around 450 miliSeconds
		secondHighest = new Processor().findSecondUsingStream(millionList);

		System.out.println("SecondHighest element using findSecondCollectionsSort is : " + secondHighest);

		//Using Parallel stream sorted. . Total time taken around 300 miliSeconds
		secondHighest = new Processor().findSecondUsingParallelStream(millionList);

		System.out.println("SecondHighest element using findSecondCollectionsSort is : " + secondHighest);

		//Executing multiple threads. Number of threads created is square root of number of elements and finding 1st
		// and 2nd from each thread.
		//Once each thread is executed collecting input in an array and finding 2nd highest.
		//This is costlier operation. Total time taken is : 348 ms

		secondHighest = new Application().executeParallel(millionList);

		System.out.println("SecondHighest element using findSecondCollectionsSort is : " + secondHighest);

	}

	public int executeParallel(List<Integer> millionList) {

		Long startTime = System.currentTimeMillis();

		int n = millionList.size();
		int k = (int) Math.sqrt(millionList.size());

		List<List<Integer>> listOfSubLists = new ArrayList<>();

		for (int i = 0; i < k; i++) {
			int start = k * i;
			int end = k * (i + 1) > n ? n : k * (i + 1);

			listOfSubLists.add(millionList.subList(start, end));
		}

		List<Integer> firstSecondList = new ArrayList<>();

		//System.out.println("Second is : "+second);
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
