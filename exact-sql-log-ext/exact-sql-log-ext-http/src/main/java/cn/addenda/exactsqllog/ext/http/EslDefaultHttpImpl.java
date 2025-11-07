package cn.addenda.exactsqllog.ext.http;

import cn.addenda.exactsqllog.ext.facade.HttpFacade;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

public class EslDefaultHttpImpl implements HttpFacade, AutoCloseable {

  private final Properties agentProperties;
  private final Properties httpProperties;
  private final CloseableHttpClient closeableHttpClient;

  public EslDefaultHttpImpl(Properties agentProperties, Properties httpProperties) {
    this.agentProperties = agentProperties;
    this.httpProperties = httpProperties;
    this.closeableHttpClient = HttpClients.createDefault();
  }

  public void sendRequest(String uri, String jsonParam) {
    try {
      doSendRequest(uri, jsonParam);
    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void doSendRequest(String uri, String jsonParam) throws URISyntaxException, IOException {
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
  public void close() throws Exception {
    closeableHttpClient.close();
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
