package com.esacinc.commons.build.maven.utils

import org.apache.maven.execution.MavenSession
import org.apache.maven.plugin.MojoExecution
import org.apache.maven.plugin.PluginParameterExpressionEvaluator
import org.apache.maven.repository.internal.MavenRepositorySystemUtils
import org.eclipse.aether.impl.DefaultServiceLocator
import org.eclipse.aether.spi.locator.ServiceLocator

final class EsacMavenUtils {
    final static String PLUGIN_PARAM_EXPR_EVALUATOR_SESSION_REQ_DATA_KEY = PluginParameterExpressionEvaluator.simpleName
    final static String SERVICE_LOCATOR_SESSION_REQ_DATA_KEY = ServiceLocator.simpleName

    private EsacMavenUtils() {
    }

    static DefaultServiceLocator getServiceLocator(MavenSession session) {
        return buildSessionComponent(session, SERVICE_LOCATOR_SESSION_REQ_DATA_KEY, { MavenRepositorySystemUtils.newServiceLocator() })
    }
    
    static PluginParameterExpressionEvaluator getPluginParameterExpressionEvaluator(MavenSession session, MojoExecution mojoExec) {
        return buildSessionComponent(session, PLUGIN_PARAM_EXPR_EVALUATOR_SESSION_REQ_DATA_KEY, {new PluginParameterExpressionEvaluator(session, mojoExec)})
    }

    static <T> T buildSessionComponent(MavenSession session, String key, Closure<T> valueBuilder) {
        return ((T) session.request.data.computeIfAbsent(key, {valueBuilder()}))
    }
}
