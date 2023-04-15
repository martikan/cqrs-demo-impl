package com.demobank.account.query.api.query;

import com.demobank.cqrs.core.query.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class FindAccountByHolderQuery extends BaseQuery {

    private String accountHolder;

}
