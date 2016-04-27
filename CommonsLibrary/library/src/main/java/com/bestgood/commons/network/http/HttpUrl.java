
package com.bestgood.commons.network.http;


import android.net.Uri;
import android.text.TextUtils;

import java.util.Map;

/**
 * 请求服务器的url地址
 *
 * @author ddc
 * @date: Apr 20, 2014 9:05:38 PM
 */
public class HttpUrl {
    public String url;

    public HttpUrl(String url) {
        this.url = url;
    }

    public String build() {
        return build(null);
    }

    public String build(Map<? extends String, ?> params) {

        if (TextUtils.isEmpty(url) || params == null || params.isEmpty()) {
            return url;
        }

        Uri.Builder builder = Uri.parse(url).buildUpon();
        for (String key : params.keySet()) {
            builder.appendQueryParameter(key, params.get(key).toString());
        }

        return builder.build().toString();
    }
}