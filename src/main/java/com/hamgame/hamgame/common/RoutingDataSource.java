package com.hamgame.hamgame.common;

import static org.springframework.transaction.support.TransactionSynchronizationManager.*;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {
	@Override
	protected Object determineCurrentLookupKey() {
		return isCurrentTransactionReadOnly() ? "slave" : "master";
	}
}
