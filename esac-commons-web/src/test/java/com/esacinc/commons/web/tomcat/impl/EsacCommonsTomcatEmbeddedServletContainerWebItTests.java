package com.esacinc.commons.web.tomcat.impl;

import com.esacinc.commons.io.EsacFileNameExtensions;
import com.esacinc.commons.net.http.client.impl.EsacRestTemplate;
import com.esacinc.commons.web.test.impl.AbstractEsacCommonsWebItTests;
import java.net.URI;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.Test;

@Test(groups = { "esac-commons.test.it.web.tomcat" })
public class EsacCommonsTomcatEmbeddedServletContainerWebItTests extends AbstractEsacCommonsWebItTests {
    @Value(("${esac-commons.tomcat.server.context.url}${esac-commons.static.images.dir.url.path}/#{ app.groupName }-logo-32x32." + EsacFileNameExtensions.PNG))
    private URI testStaticImgUrl;

    @Value("${esac-commons.tomcat.server.context.url}${esac-commons.static.images.favicon.file.url.path}")
    private URI testFaviconUrl;

    @Resource(name = "restTemplateTomcatClient")
    private EsacRestTemplate restTemplate;

    @Test
    public void testGetStaticFiles() throws Exception {
        Assertions.assertThat(this.restTemplate.getForObject(this.testStaticImgUrl, byte[].class)).isNotEmpty();

        Assertions.assertThat(this.restTemplate.getForObject(this.testFaviconUrl, byte[].class)).isNotEmpty();
    }
}
