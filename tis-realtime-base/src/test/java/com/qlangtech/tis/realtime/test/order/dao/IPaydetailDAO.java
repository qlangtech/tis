/**
 * Copyright (c) 2020 QingLang, Inc. <baisui@qlangtech.com>
 *
 * This program is free software: you can use, redistribute, and/or modify
 * it under the terms of the GNU Affero General Public License, version 3
 * or later ("AGPL"), as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.qlangtech.tis.realtime.test.order.dao;

import com.qlangtech.tis.ibatis.RowMap;
import com.qlangtech.tis.realtime.test.order.pojo.Paydetail;
import com.qlangtech.tis.realtime.test.order.pojo.PaydetailCriteria;
import java.util.List;

/**
 * @author 百岁（baisui@qlangtech.com）
 * @date 2020/04/13
 */
public interface IPaydetailDAO {

    int countByExample(PaydetailCriteria example);

    int countFromWriteDB(PaydetailCriteria example);

    int deleteByExample(PaydetailCriteria criteria);

    int deleteByPrimaryKey(String paydetailId);

    void insert(Paydetail record);

    void insertSelective(Paydetail record);

    List<Paydetail> selectByExample(PaydetailCriteria criteria);

    @SuppressWarnings("all")
    List<RowMap> selectColsByExample(PaydetailCriteria example, int page, int pageSize);

    List<Paydetail> selectByExample(PaydetailCriteria example, int page, int pageSize);

    Paydetail selectByPrimaryKey(String paydetailId);

    int updateByExampleSelective(Paydetail record, PaydetailCriteria example);

    int updateByExample(Paydetail record, PaydetailCriteria example);

    Paydetail loadFromWriteDB(String paydetailId);
}
