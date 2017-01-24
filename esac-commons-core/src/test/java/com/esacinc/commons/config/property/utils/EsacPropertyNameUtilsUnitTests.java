package com.esacinc.commons.config.property.utils;

import com.esacinc.commons.test.impl.AbstractEsacCommonsUnitTests;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "esac-commons.test.unit.io.utils" }, groups = { "esac-commons.test.unit.config", "esac-commons.test.unit.config.prop",
    "esac-commons.test.unit.config.prop.utils", "esac-commons.test.unit.config.prop.utils.name" })
public class EsacPropertyNameUtilsUnitTests extends AbstractEsacCommonsUnitTests {
    public final static String PROP_NAME_0 = "part0";
    public final static String PROP_NAME_1 = PROP_NAME_0 + ".part1";
    public final static String PROP_NAME_2 = PROP_NAME_0 + ".part2";
    public final static String PROP_NAME_3 = PROP_NAME_2 + ".part3";
    public final static String PROP_NAME_4 = PROP_NAME_0 + ".part4";

    @Test(dependsOnMethods = { "testIsParent" })
    public void testIsDescendant() {
        Assertions.assertThat(EsacPropertyNameUtils.isDescendant(PROP_NAME_0, PROP_NAME_3)).isTrue();

        Assertions.assertThat(EsacPropertyNameUtils.isDescendant(PROP_NAME_1, PROP_NAME_3)).isFalse();
    }

    @Test
    public void testIsParent() {
        Assertions.assertThat(EsacPropertyNameUtils.isParent(PROP_NAME_3, PROP_NAME_2)).isTrue();

        Assertions.assertThat(EsacPropertyNameUtils.isParent(PROP_NAME_3, PROP_NAME_0)).isFalse();
    }
}
