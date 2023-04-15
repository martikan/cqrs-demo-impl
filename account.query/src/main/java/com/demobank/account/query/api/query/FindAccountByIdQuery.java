package com.demobank.account.query.api.query;

import com.demobank.cqrs.core.query.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class FindAccountByIdQuery extends BaseQuery {

    private String id;

}
