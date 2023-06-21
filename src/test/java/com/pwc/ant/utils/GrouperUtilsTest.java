package com.erebos.ant.utils;

import com.erebos.ant.utils.pojo.AccountTransaction;
import com.erebos.ant.utils.pojo.DataTypeX;
import com.erebos.ant.utils.pojo.Type;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.erebos.ant.utils.GrouperUtils.groupByEnumMember;
import static com.erebos.ant.utils.GrouperUtils.groupByStringMember;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class GrouperUtilsTest {

    @Test
    void testGroupByString() {
        final List<AccountTransaction> list = List.of(new AccountTransaction("PC02", "Tran 1"), new AccountTransaction("PC01", "Tran 1"), new AccountTransaction("PC02", "Tran 2"));

        final Map<String, List<AccountTransaction>> grouped = groupByStringMember(list, AccountTransaction::id);

        assertThat(grouped.size(), is(2));
        assertThat(grouped.get("PC01").size(), is(1));
        assertThat(grouped.get("PC02").size(), is(2));
        assertThat(grouped.get("PC01").get(0).name(), is("Tran 1"));
        assertThat(grouped.get("PC02").get(0).name(), is("Tran 1"));
        assertThat(grouped.get("PC02").get(1).name(), is("Tran 2"));
    }

    @Test
    void testGroupByEnumMember() {
        final List<DataTypeX> list = List.of(
                new DataTypeX("name01", Type.TYP1),
                new DataTypeX("name02", Type.TYP2),
                new DataTypeX("name03", Type.TYP1)
        );
        final Map<Enum, List<DataTypeX>> grouped = groupByEnumMember(list, DataTypeX::getType);

        assertThat(grouped.size(), is(2));
        assertThat(grouped.get(Type.TYP1).size(), is(2));
        List<DataTypeX> type1Elements = grouped.get(Type.TYP1);
        assertThat(type1Elements.get(0).name, is("name01"));
        assertThat(type1Elements.get(1).name, is("name03"));

        assertThat(grouped.get(Type.TYP2).size(), is(1));
        List<DataTypeX> type2Elements = grouped.get(Type.TYP2);
        assertThat(type2Elements.get(0).name, is("name02"));
    }
}
