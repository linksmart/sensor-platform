package de.fhg.fit.biomos.sensorplatform.deprecated;

import de.fhg.fit.biomos.sensorplatform.web.TeLiProUploader;

public class Telipro {

  public static void main(String[] args) {
    TeLiProUploader u = new TeLiProUploader("SYML-TST-003-XXX", "a-secret", "Sensorplatform", "YYYY-MM-dd'T'HH:mm:ss.SSS'Z'",
        "https://ditg.fit.fraunhofer.de/api/v1/deviceLogin", "https://ditg.fit.fraunhofer.de/api/v1/user/SYML-TST-003-XXX/samples",
        "https://ditg.fit.fraunhofer.de/api/v1/user/SYML-TST-003-XXX/samples/HeartRate/date/2016-07-27/1d.json");

    u.login();

    u.downloadData();

  }

  // public static void main(final String[] args) throws Exception {
  // CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
  // try {
  // httpclient.start();
  //
  // final HttpGet request1 = new HttpGet("http://www.apache.org/");
  // Future<HttpResponse> future = httpclient.execute(request1, null);
  // HttpResponse response1 = future.get();
  // System.out.println(request1.getRequestLine() + "->" + response1.getStatusLine());
  //
  // final CountDownLatch latch1 = new CountDownLatch(1);
  // final HttpGet request2 = new HttpGet("http://www.apache.org/");
  // httpclient.execute(request2, new FutureCallback<HttpResponse>() {
  //
  // @Override
  // public void completed(final HttpResponse response2) {
  // latch1.countDown();
  // System.out.println(request2.getRequestLine() + "->" + response2.getStatusLine());
  // }
  //
  // @Override
  // public void failed(final Exception ex) {
  // latch1.countDown();
  // System.out.println(request2.getRequestLine() + "->" + ex);
  // }
  //
  // @Override
  // public void cancelled() {
  // latch1.countDown();
  // System.out.println(request2.getRequestLine() + " cancelled");
  // }
  //
  // });
  // latch1.await();
  //
  // final CountDownLatch latch2 = new CountDownLatch(1);
  // final HttpGet request3 = new HttpGet("http://www.apache.org/");
  // HttpAsyncRequestProducer producer3 = HttpAsyncMethods.create(request3);
  // AsyncCharConsumer<HttpResponse> consumer3 = new AsyncCharConsumer<HttpResponse>() {
  //
  // HttpResponse response;
  //
  // @Override
  // protected void onResponseReceived(final HttpResponse response) {
  // this.response = response;
  // }
  //
  // @Override
  // protected void onCharReceived(final CharBuffer buf, final IOControl ioctrl) throws IOException {
  // // Do something useful
  // }
  //
  // @Override
  // protected void releaseResources() {
  // }
  //
  // @Override
  // protected HttpResponse buildResult(final HttpContext context) {
  // return this.response;
  // }
  //
  // };
  // httpclient.execute(producer3, consumer3, new FutureCallback<HttpResponse>() {
  // @Override
  // public void completed(final HttpResponse response3) {
  // latch2.countDown();
  // System.out.println(request2.getRequestLine() + "->" + response3.getStatusLine());
  // }
  //
  // @Override
  // public void failed(final Exception ex) {
  // latch2.countDown();
  // System.out.println(request2.getRequestLine() + "->" + ex);
  // }
  //
  // @Override
  // public void cancelled() {
  // latch2.countDown();
  // System.out.println(request2.getRequestLine() + " cancelled");
  // }
  // });
  // latch2.await();
  //
  // } finally {
  // httpclient.close();
  // }
  // }

}
