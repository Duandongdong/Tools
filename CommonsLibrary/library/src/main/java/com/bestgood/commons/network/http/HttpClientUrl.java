
package com.bestgood.commons.network.http;

import com.google.api.client.http.GenericUrl;

/**
 * 请求服务器的url地址
 *
 * @author ddc
 * @date: Apr 20, 2014 9:05:38 PM
 */
public class HttpClientUrl extends GenericUrl {
    public HttpClientUrl(String encodedUrl) {
        super(encodedUrl);
    }
}
