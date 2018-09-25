package com.sunnsoft.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.SecondLevelCacheStatistics;
import org.hibernate.stat.Statistics;

public class HibernateStatistic {

	private Log logger = LogFactory.getLog(HibernateStatistic.class);

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void execute() {
		if (logger.isInfoEnabled()) {
			Statistics stats = sessionFactory.getStatistics();

			double queryCacheHitCount = stats.getQueryCacheHitCount();
			double queryCacheMissCount = stats.getQueryCacheMissCount();
			double queryCacheHitRatio = (queryCacheHitCount / (queryCacheHitCount + queryCacheMissCount)) * 100.0;
			logger.info("Query Cache Hit ratio:" + queryCacheHitRatio + "%");
			// System.out.println("Query Cache Hit ratio:" +
			// queryCacheHitRatio);

			long maxQueryTime = stats.getQueryExecutionMaxTime();
			String maxTimeQueryString = stats
					.getQueryExecutionMaxTimeQueryString();
			logger.info("Max Time Query:" + maxTimeQueryString + " [ "
					+ maxQueryTime + " ms ]");
			// System.out.println("Max Time Query:" + maxTimeQueryString + " [ "
			// + maxQueryTime + " ms ]");
			String[] classNames = stats.getEntityNames();
			for (int i = 0; i < classNames.length; i++) {
				String className = classNames[i];
				logClass(className, stats);
			}
			double secondLevelCacheHitCount = stats
					.getSecondLevelCacheHitCount();
			double secondLevelCacheMissCount = stats
					.getSecondLevelCacheMissCount();
			double secondLevelCacheHitRatio = (secondLevelCacheHitCount / (secondLevelCacheHitCount + secondLevelCacheMissCount)) * 100.0;
			logger.info("Second Level Cache Hit ratio:"
					+ secondLevelCacheHitRatio + "%");
			// System.out.println("Second Level Cache Hit ratio:"
			// + secondLevelCacheHitRatio);
			String[] regionNames = stats.getSecondLevelCacheRegionNames();
			for (int i = 0; i < regionNames.length; i++) {
				String region = regionNames[i];
				logSecondLevelCache(region, stats);
			}
		}
	}

	public void logClass(String voClassName, Statistics stats) {
		EntityStatistics entityStats = stats.getEntityStatistics(voClassName);
		long changes = entityStats.getInsertCount()
				+ entityStats.getUpdateCount() + entityStats.getDeleteCount();
		long fetch = entityStats.getFetchCount();
		long load = entityStats.getLoadCount();

		logger.info(voClassName + " changed [" + changes + "] times , fetch ["
				+ fetch + "] times , load [" + load + "] times");
		// System.out.println(voClassName + " changed [" + changes
		// + "] times , fetch [" + fetch + "] times , load [" + load
		// + "] times");
	}

	public void logSecondLevelCache(String regionName, Statistics stats) {
		SecondLevelCacheStatistics secondLevelStats = stats
				.getSecondLevelCacheStatistics(regionName);
		long countInMem = secondLevelStats.getElementCountInMemory();
		long countOnDisk = secondLevelStats.getElementCountOnDisk();
		double hit = secondLevelStats.getHitCount();
		double miss = secondLevelStats.getMissCount();
		long sizeInMem = secondLevelStats.getSizeInMemory();
		double ratio = (hit / (hit + miss)) * 100.0;
		logger.info(regionName + " use memory [" + sizeInMem
				+ "] bytes , in mem [" + countInMem + "] entrys,on disk ["
				+ countOnDisk + "] entrys,hit ratio [" + ratio + "%]");
		// System.out.println(regionName + " use memory [" + sizeInMem
		// + "] bytes , in mem [" + countInMem + "] entrys,on disk ["
		// + countOnDisk + "] entrys,hit ratio [" + ratio + "]");
	}
}
