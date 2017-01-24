package com.esacinc.commons.config.property.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import com.esacinc.commons.config.property.utils.EsacPropertyNameUtilsUnitTests;
import com.esacinc.commons.test.impl.AbstractEsacCommonsUnitTests;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "esac-commons.test.unit.io.utils", "esac-commons.test.unit.config.prop.utils" },
    groups = { "esac-commons.test.unit.config", "esac-commons.test.unit.config.prop", "esac-commons.test.unit.config.prop.trie" })
public class PropertyTrieUnitTests extends AbstractEsacCommonsUnitTests {
    private final static Boolean PROP_VALUE1 = true;
    private final static Double PROP_VALUE3 = -1.0D;
    private final static List<Integer> PROP_VALUE4 = Stream.of(1, 2, 3).collect(Collectors.toList());

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    @Test
    public void testTrie() throws Exception {
        PropertyTrieImpl<Object> trie = new PropertyTrieImpl<>();
        trie.put(EsacPropertyNameUtilsUnitTests.PROP_NAME_1, PROP_VALUE1);
        trie.put(EsacPropertyNameUtilsUnitTests.PROP_NAME_3, PROP_VALUE3);
        trie.put(EsacPropertyNameUtilsUnitTests.PROP_NAME_4, PROP_VALUE4);

        Assertions.assertThat(trie).containsOnlyKeys(EsacPropertyNameUtilsUnitTests.PROP_NAME_0, EsacPropertyNameUtilsUnitTests.PROP_NAME_1,
            EsacPropertyNameUtilsUnitTests.PROP_NAME_2, EsacPropertyNameUtilsUnitTests.PROP_NAME_3, EsacPropertyNameUtilsUnitTests.PROP_NAME_4);

        Assertions.assertThat(trie.containsLeafKey(EsacPropertyNameUtilsUnitTests.PROP_NAME_1)).isTrue();
        Assertions.assertThat(trie.get(EsacPropertyNameUtilsUnitTests.PROP_NAME_1)).isEqualTo(PROP_VALUE1);

        Assertions.assertThat(trie.containsBranchKey(EsacPropertyNameUtilsUnitTests.PROP_NAME_2)).isTrue();

        Assertions.assertThat(trie.containsLeafKey(EsacPropertyNameUtilsUnitTests.PROP_NAME_3)).isTrue();
        Assertions.assertThat(trie.get(EsacPropertyNameUtilsUnitTests.PROP_NAME_3)).isEqualTo(PROP_VALUE3);

        Assertions.assertThat(trie.containsLeafKey(EsacPropertyNameUtilsUnitTests.PROP_NAME_4)).isTrue();
        Assertions.assertThat(trie.get(EsacPropertyNameUtilsUnitTests.PROP_NAME_4)).isEqualTo(PROP_VALUE4);

        Assertions.assertThat(trie.prefixMap(EsacPropertyNameUtilsUnitTests.PROP_NAME_0)).containsOnlyKeys(EsacPropertyNameUtilsUnitTests.PROP_NAME_1,
            EsacPropertyNameUtilsUnitTests.PROP_NAME_2, EsacPropertyNameUtilsUnitTests.PROP_NAME_3, EsacPropertyNameUtilsUnitTests.PROP_NAME_4);

        trie.remove(EsacPropertyNameUtilsUnitTests.PROP_NAME_3);

        Assertions.assertThat(trie).containsOnlyKeys(EsacPropertyNameUtilsUnitTests.PROP_NAME_0, EsacPropertyNameUtilsUnitTests.PROP_NAME_1,
            EsacPropertyNameUtilsUnitTests.PROP_NAME_4);

        Assertions.assertThat(trie.prefixMap(EsacPropertyNameUtilsUnitTests.PROP_NAME_0)).containsOnlyKeys(EsacPropertyNameUtilsUnitTests.PROP_NAME_1,
            EsacPropertyNameUtilsUnitTests.PROP_NAME_4);
    }
}
