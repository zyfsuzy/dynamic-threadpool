package cn.hippo4j.starter.remote;

import cn.hippo4j.common.config.ApplicationContextHolder;
import cn.hippo4j.common.web.base.Result;
import cn.hippo4j.starter.config.BootstrapProperties;
import cn.hippo4j.starter.toolkit.HttpClientUtil;

import java.util.Map;

/**
 * Server http agent.
 *
 * @author chen.ma
 * @date 2021/6/23 20:50
 */
public class ServerHttpAgent implements HttpAgent {

    private final BootstrapProperties dynamicThreadPoolProperties;

    private final ServerListManager serverListManager;

    private final HttpClientUtil httpClientUtil;

    private ServerHealthCheck serverHealthCheck;

    public ServerHttpAgent(BootstrapProperties properties, HttpClientUtil httpClientUtil) {
        this.dynamicThreadPoolProperties = properties;
        this.httpClientUtil = httpClientUtil;
        this.serverListManager = new ServerListManager(dynamicThreadPoolProperties);
    }

    @Override
    public void start() {

    }

    @Override
    public String getTenantId() {
        return dynamicThreadPoolProperties.getNamespace();
    }

    @Override
    public String getEncode() {
        return null;
    }

    @Override
    public Result httpGetSimple(String path) {
        return httpClientUtil.restApiGetHealth(buildUrl(path), Result.class);
    }

    @Override
    public Result httpPost(String path, Object body) {
        isHealthStatus();
        return httpClientUtil.restApiPost(buildUrl(path), body, Result.class);
    }

    @Override
    public Result httpPostByDiscovery(String path, Object body) {
        isHealthStatus();
        return httpClientUtil.restApiPost(buildUrl(path), body, Result.class);
    }

    @Override
    public Result httpGetByConfig(String path, Map<String, String> headers, Map<String, String> paramValues, long readTimeoutMs) {
        isHealthStatus();
        return httpClientUtil.restApiGetByThreadPool(buildUrl(path), headers, paramValues, readTimeoutMs, Result.class);
    }

    @Override
    public Result httpPostByConfig(String path, Map<String, String> headers, Map<String, String> paramValues, long readTimeoutMs) {
        isHealthStatus();
        return httpClientUtil.restApiPostByThreadPool(buildUrl(path), headers, paramValues, readTimeoutMs, Result.class);
    }

    @Override
    public Result httpDeleteByConfig(String path, Map<String, String> headers, Map<String, String> paramValues, long readTimeoutMs) {
        return null;
    }

    private String buildUrl(String path) {
        return serverListManager.getCurrentServerAddr() + path;
    }

    private void isHealthStatus() {
        if (serverHealthCheck == null) {
            serverHealthCheck = ApplicationContextHolder.getBean(ServerHealthCheck.class);
        }

        serverHealthCheck.isHealthStatus();
    }

}
