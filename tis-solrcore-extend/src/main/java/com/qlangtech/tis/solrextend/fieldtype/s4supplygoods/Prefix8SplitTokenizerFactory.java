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
package com.qlangtech.tis.solrextend.fieldtype.s4supplygoods;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 * 原始值为warehouseid_lastVer;[warehouseid_lastVer]
 * paser取内容中的warehouseid作为term
 * warehouseId 从第9位开始到最后一位
 * warehouseId 前8位也就是selfentityid
 *
 * @author 百岁（baisui@qlangtech.com）
 * @date 2019年1月17日
 */
public class Prefix8SplitTokenizerFactory extends TokenizerFactory {

    public Prefix8SplitTokenizerFactory(Map<String, String> args) {
        super(args);
    }

    @Override
    public Tokenizer create(AttributeFactory attributeFactory) {
        return new Prefix8SplitTokenizer(attributeFactory);
    }
}

class Prefix8SplitTokenizer extends Tokenizer {

    private final CharTermAttribute termAttr = (CharTermAttribute) addAttribute(CharTermAttribute.class);

    String[] stockInfoArray;

    private int termsIndex = 0;

    private int termsNum = 0;

    static final Pattern KV_PAIR_LIST_PATTERN = Pattern.compile("(\\d{8})(\\w+)_(\\d+)");

    Prefix8SplitTokenizer(AttributeFactory factory) {
        super(factory);
    }

    @Override
    public boolean incrementToken() throws IOException {
        this.clearAttributes();
        if (stockInfoArray == null) {
            Set<String> stockInfoTermSet = new HashSet<>();
            Matcher kvPairMatcher = KV_PAIR_LIST_PATTERN.matcher(IOUtils.toString(this.input));
            while (kvPairMatcher.find()) {
                assert (kvPairMatcher.groupCount() == 3);
                stockInfoTermSet.add(kvPairMatcher.group(1));
                stockInfoTermSet.add(kvPairMatcher.group(2));
            }
            termsIndex = 0;
            termsNum = stockInfoTermSet.size();
            stockInfoArray = stockInfoTermSet.toArray(new String[termsNum]);
        }
        if (termsIndex >= termsNum) {
            termsIndex = -1;
            termsNum = 0;
            stockInfoArray = null;
            return false;
        }
        termAttr.setEmpty().append(stockInfoArray[termsIndex++]);
        return true;
    }
}
