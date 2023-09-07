package com.hamgame.hamgame.common;

import static org.springframework.transaction.support.TransactionSynchronizationManager.*;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {
	/*
	 * @Transaction(readOnly = true)가 설정되어있으면 slave, 나머지는 master 호출
	 * 리턴되는 key값과 일치하는 dataSource명을 찾아 targetDataSource로 사용하도록 한다.
	 */
	@Override
	protected Object determineCurrentLookupKey() {
		return isCurrentTransactionReadOnly() ? "slave" : "master";
	}
}
