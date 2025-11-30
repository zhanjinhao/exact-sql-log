package cn.addenda.exactsqllog.ext.http;

import cn.addenda.exactsqllog.common.jvm.JVMShutdownCallback;
import cn.addenda.exactsqllog.facade.HttpFacade;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.util.Properties;

public class EslDefaultHttpImpl implements HttpFacade, JVMShutdownCallback {

  private final Properties agentProperties;
  private final Properties httpProperties;
  private final CloseableHttpClient closeableHttpClient;

  public EslDefaultHttpImpl(Properties agentProperties, Properties httpProperties) {
    this.agentProperties = agentProperties;
    this.httpProperties = httpProperties;
    this.closeableHttpClient = createHttpClient();
  }

  private CloseableHttpClient createHttpClient() {
    // Create a connection manager with custom configuration.
    final PoolingHttpClientConnectionManager connManager = PoolingHttpClientConnectionManagerBuilder.create()
            .setPoolConcurrencyPolicy(PoolConcurrencyPolicy.STRICT)
            .setConnPoolPolicy(PoolReusePolicy.FIFO)
            .build();

    // Configure the connection manager to use socket configuration either
    // by default or for a specific host.
    connManager.setDefaultSocketConfig(SocketConfig.custom()
            .setTcpNoDelay(true)
            .build());
    // Validate connections after 10 sec of inactivity
    connManager.setDefaultConnectionConfig(ConnectionConfig.custom()
            .setConnectTimeout(Timeout.ofMilliseconds(getHttpPropertyInteger("connection.connectTimeout")))
            .setSocketTimeout(Timeout.ofMilliseconds(getHttpPropertyInteger("connection.socketTimeout")))
            .setValidateAfterInactivity(TimeValue.ofMilliseconds(getHttpPropertyInteger("connection.validateAfterInactivity")))
            .setTimeToLive(TimeValue.ofMilliseconds(getHttpPropertyInteger("connection.timeToLive")))
            .build());

    // Create global request configuration
    final RequestConfig defaultRequestConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(Timeout.ofMilliseconds(getHttpPropertyInteger("connection.connectionRequestTimeout")))
            .build();

    // Configure total max or per route limits for persistent connections
    // that can be kept in the pool or leased by the connection manager.
    connManager.setMaxTotal(getHttpPropertyInteger("connection.maxTotal"));
    connManager.setDefaultMaxPerRoute(getHttpPropertyInteger("connection.defaultMaxPerRoute"));

    return HttpClients.custom()
            .setConnectionManager(connManager)
            .setDefaultRequestConfig(defaultRequestConfig)
            .build();
  }

  private Integer getHttpPropertyInteger(String propertyName) {
    return Integer.parseInt(httpProperties.getProperty(propertyName));
  }

  public void sendRequest(String uri, String jsonParam) {
    ExactSqlLogExtHttpException.runWithExactSqlLogExtException(
            () -> doSendRequest(uri, jsonParam)
    );
  }

  private void doSendRequest(String uri, String jsonParam) throws IOException {
    final HttpPost httpPost = new HttpPost(uri);

    // 设置请求头，指定内容类型为 JSON
    httpPost.setHeader("Content-Type", "application/json");

    // 设置请求体，将 JSON 参数放入请求体中
    StringEntity entity = new StringEntity(jsonParam, ContentType.APPLICATION_JSON);
    httpPost.setEntity(entity);

    final Result result = closeableHttpClient.execute(httpPost, response -> {
      // Process response message and convert it into a value object
      return new Result(response.getCode(), EntityUtils.toString(response.getEntity()));
    });
  }

  @Override
  public void shutdown() throws Throwable {
    if (closeableHttpClient != null) {
      closeableHttpClient.close();
    }
  }

  @Override
  public Integer getOrder() {
    return 100;
  }

  @Setter
  @Getter
  @ToString
  static class Result {

    final int status;
    final String content;

    Result(final int status, final String content) {
      this.status = status;
      this.content = content;
    }

  }

}
