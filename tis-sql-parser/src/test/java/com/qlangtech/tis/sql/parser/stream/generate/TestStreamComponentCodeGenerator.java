/**
 * Copyright (c) 2020 QingLang, Inc. <baisui@qlangtech.com>
 * <p>
 * This program is free software: you can use, redistribute, and/or modify
 * it under the terms of the GNU Affero General Public License, version 3
 * or later ("AGPL"), as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.qlangtech.tis.sql.parser.stream.generate;

import com.google.common.collect.Lists;
import com.qlangtech.tis.manage.common.Config;
import com.qlangtech.tis.manage.common.TisUTF8;
import com.qlangtech.tis.sql.parser.BasicTestCase;
import com.qlangtech.tis.sql.parser.SqlTaskNodeMeta;
import com.qlangtech.tis.sql.parser.er.ERRules;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * @author 百岁（baisui@qlangtech.com）
 * @date 2020/04/13
 */
public class TestStreamComponentCodeGenerator extends BasicTestCase {

    /**
     * 测试单表增量脚本生成
     *
     * @throws Exception
     */
    public void testSingleTableCodeGenerator() throws Exception {

        //  CoreAction.create
        String topologyName = "employees4local";
        Optional<ERRules> erRule = ERRules.getErRule(topologyName);

        // 测试针对单表的的topology增量脚本生成
        long timestamp = 20191111115959l;
        SqlTaskNodeMeta.SqlDataFlowTopology topology = SqlTaskNodeMeta.getSqlDataFlowTopology(topologyName);
        assertNotNull(topology);
        if (!erRule.isPresent()) {
            ERRules.createDefaultErRule(topology);
        }
        String collectionName = "search4employee4local";
        List<FacadeContext> facadeList = Lists.newArrayList();
        StreamComponentCodeGenerator streamCodeGenerator
                = new StreamComponentCodeGenerator("search4employee4local", timestamp, facadeList, topology, true);
        streamCodeGenerator.build();

        assertGenerateContentEqual(timestamp, collectionName, "S4employee4localListener.scala");
    }


    public void testGeneratorCode() throws Exception {
        long timestamp = 20191111115959l;
        String collectionName = "search4totalpay";
        SqlTaskNodeMeta.SqlDataFlowTopology topology = SqlTaskNodeMeta.getSqlDataFlowTopology("totalpay");
        FacadeContext fc = new FacadeContext();
        fc.setFacadeInstanceName("order2DAOFacade");
        fc.setFullFacadeClassName("com.qlangtech.tis.realtime.order.dao.IOrder2DAOFacade");
        fc.setFacadeInterfaceName("IOrder2DAOFacade");
        List<FacadeContext> facadeList = Lists.newArrayList();
        facadeList.add(fc);
        StreamComponentCodeGenerator streamCodeGenerator = new StreamComponentCodeGenerator("search4totalpay", timestamp, facadeList, topology);
        streamCodeGenerator.build();

        assertGenerateContentEqual(timestamp, collectionName, "S4totalpayListener.scala");
    }

    public void testGeneratorSearch4totalpay5Code() throws Exception {
        long timestamp = 20200928183209l;
        String collectionName = "search4totalpay5";
        SqlTaskNodeMeta.SqlDataFlowTopology topology = SqlTaskNodeMeta.getSqlDataFlowTopology("totalpay2");
        FacadeContext fc = new FacadeContext();
        fc.setFacadeInstanceName("order2DAOFacade");
        fc.setFullFacadeClassName("com.qlangtech.tis.realtime.order.dao.IOrder2DAOFacade");
        fc.setFacadeInterfaceName("IOrder2DAOFacade");
        List<FacadeContext> facadeList = Lists.newArrayList();
        facadeList.add(fc);
        StreamComponentCodeGenerator streamCodeGenerator = new StreamComponentCodeGenerator(collectionName, timestamp, facadeList, topology);
        streamCodeGenerator.build();
        assertGenerateContentEqual(timestamp, collectionName, "S4totalpay5Listener.scala");

    }

    public static void assertGenerateContentEqual(long timestamp, String collectionName, String generateScalaFileName) throws IOException {
        File generateFile = new File(Config.getDataDir(), "cfg_repo/streamscript/" + collectionName + "/"
                + timestamp + "/src/main/scala/com/qlangtech/tis/realtime/transfer/" + collectionName + "/" + generateScalaFileName);
        // 校验生成的文件和assert文件内容相等
        try (InputStream assertFile = TestStreamComponentCodeGenerator.class.getResourceAsStream(generateScalaFileName)) {
            assertNotNull("generateScalaFileName can not be null:" + generateScalaFileName, assertFile);
           // FileUtils.write(new File(generateScalaFileName), FileUtils.readFileToString(generateFile, TisUTF8.get()), TisUTF8.get(), false);
            assertTrue(generateFile.getAbsolutePath(), generateFile.exists());
            assertEquals(IOUtils.toString(assertFile, TisUTF8.get()), FileUtils.readFileToString(generateFile, TisUTF8.get()));
        }
    }
}
