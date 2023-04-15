package com.demobank.account.query.api.query;

import com.demobank.account.query.api.dto.EqualityType;
import com.demobank.cqrs.core.query.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class FindAccountByBalanceQuery extends BaseQuery {

    private EqualityType equalityType;

    private double balance;

}
