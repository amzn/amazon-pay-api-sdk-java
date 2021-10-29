[
    {
      "name" : "get-vanilla",
      "uri" : "/",
      "method" : "GET",
      "parameters" : {},
      "payload" : "",
      "canonicalRequest" : "GET\n/\n\naccept:application/json\ncontent-type:application/json\nx-amz-pay-date:20180524T223710Z\nx-amz-pay-host:pay-api.amazon.eu\nx-amz-pay-region:EU\n\naccept;content-type;x-amz-pay-date;x-amz-pay-host;x-amz-pay-region\ne3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
      "stringToSign" : "AMZN-PAY-RSASSA-PSS\nbb49552975c396f1e49d5510e1555b8b0627dc491bc61aaefdb561f202ad8020"
    },

    {
      "name" : "get-slashes",
      "uri" : "//foo//",
      "method" : "GET",
      "parameters" : {},
      "payload" : "",
      "canonicalRequest" : "GET\n/\n\naccept:application/json\ncontent-type:application/json\nx-amz-pay-date:20180524T223710Z\nx-amz-pay-host:pay-api.amazon.eu\nx-amz-pay-region:EU\n\naccept;content-type;x-amz-pay-date;x-amz-pay-host;x-amz-pay-region\ne3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
      "stringToSign" : "AMZN-PAY-RSASSA-PSS\nbb49552975c396f1e49d5510e1555b8b0627dc491bc61aaefdb561f202ad8020"
    },

    {
      "name" : "get-empty",
      "uri" : "",
      "method" : "GET",
      "parameters" : {},
      "payload" : "",
      "canonicalRequest" : "GET\n/\n\naccept:application/json\ncontent-type:application/json\nx-amz-pay-date:20180524T223710Z\nx-amz-pay-host:pay-api.amazon.eu\nx-amz-pay-region:EU\n\naccept;content-type;x-amz-pay-date;x-amz-pay-host;x-amz-pay-region\ne3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
      "stringToSign" : "AMZN-PAY-RSASSA-PSS\nbb49552975c396f1e49d5510e1555b8b0627dc491bc61aaefdb561f202ad8020"
    },

    {
      "name" : "get-unreserved",
      "uri" : "/-._~0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz",
      "method" : "GET",
      "parameters" : {},
      "payload" : "",
      "canonicalRequest" : "GET\n/-._~0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz\n\naccept:application/json\ncontent-type:application/json\nx-amz-pay-date:20180524T223710Z\nx-amz-pay-host:pay-api.amazon.eu\nx-amz-pay-region:EU\n\naccept;content-type;x-amz-pay-date;x-amz-pay-host;x-amz-pay-region\ne3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
      "stringToSign" : "AMZN-PAY-RSASSA-PSS\na0e5dd63e175587f29d51c6b08be5c746a568209ac66ab1f967e0ba515aa80bc"
    },

    {
      "name" : "get-vanilla-query",
      "uri" : "/",
      "method" : "GET",
      "parameters" : {},
      "algorithm" : "AMZN-PAY-RSASSA-PSS",
      "payload" : "",
      "canonicalRequest" : "GET\n/\n\naccept:application/json\ncontent-type:application/json\nx-amz-pay-date:20180524T223710Z\nx-amz-pay-host:pay-api.amazon.eu\nx-amz-pay-region:EU\n\naccept;content-type;x-amz-pay-date;x-amz-pay-host;x-amz-pay-region\ne3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
      "stringToSign" : "AMZN-PAY-RSASSA-PSS\nbb49552975c396f1e49d5510e1555b8b0627dc491bc61aaefdb561f202ad8020"
    },

    {
      "name" : "get-utf8",
      "uri" : "/\u1234",
      "method" : "GET",
      "parameters" : {},
      "payload" : "",
      "canonicalRequest" : "GET\n/%E1%88%B4\n\naccept:application/json\ncontent-type:application/json\nx-amz-pay-date:20180524T223710Z\nx-amz-pay-host:pay-api.amazon.eu\nx-amz-pay-region:EU\n\naccept;content-type;x-amz-pay-date;x-amz-pay-host;x-amz-pay-region\ne3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
      "stringToSign" : "AMZN-PAY-RSASSA-PSS\n77f6544c8fb336d430895659da1e30415a1558b4b013f4d7926bb3a6275f701d"

    },

    {
      "name" : "get-vanilla-utf8-query",
      "uri" : "/",
      "method" : "GET",
      "parameters" : {
        "\u1234" : [ "bar" ]
      },
      "payload" : "",
      "canonicalRequest" : "GET\n/\n%E1%88%B4=bar\naccept:application/json\ncontent-type:application/json\nx-amz-pay-date:20180524T223710Z\nx-amz-pay-host:pay-api.amazon.eu\nx-amz-pay-region:EU\n\naccept;content-type;x-amz-pay-date;x-amz-pay-host;x-amz-pay-region\ne3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
      "stringToSign" : "AMZN-PAY-RSASSA-PSS\nef1007307504f2ca9f0b6310b04056f5d3d6d18e7bf3d90663133f19ec132ecc"
    },

    {
      "name" : "post-vanilla",
      "uri" : "/",
      "method" : "POST",
      "parameters" : {},
      "payload" : "",
      "canonicalRequest" : "POST\n/\n\naccept:application/json\ncontent-type:application/json\nx-amz-pay-date:20180524T223710Z\nx-amz-pay-host:pay-api.amazon.eu\nx-amz-pay-region:EU\n\naccept;content-type;x-amz-pay-date;x-amz-pay-host;x-amz-pay-region\ne3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
      "stringToSign" : "AMZN-PAY-RSASSA-PSS\n54156f5badd6da7fa630b2c334c808ad376530001d334ae9960457215aa626d3"
    },

    {
      "name" : "post-vanilla-query",
      "uri" : "/",
      "method" : "POST",
      "parameters" : {
          "foo" : [ "bar" ]
      },
      "payload" : "",
      "canonicalRequest" : "POST\n/\nfoo=bar\naccept:application/json\ncontent-type:application/json\nx-amz-pay-date:20180524T223710Z\nx-amz-pay-host:pay-api.amazon.eu\nx-amz-pay-region:EU\n\naccept;content-type;x-amz-pay-date;x-amz-pay-host;x-amz-pay-region\ne3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
      "stringToSign" : "AMZN-PAY-RSASSA-PSS\ncc82525870b2d03666c287fd8b2c0d2fc6fa1693d9f357d27f7cac8cbcc12832"
    },

    {
      "name" : "get-vanilla-empty-query-key",
      "uri" : "/",
      "method" : "GET",
      "parameters" : {
          "foo" : [ "bar" ]
      },
      "payload" : "",
      "canonicalRequest" : "GET\n/\nfoo=bar\naccept:application/json\ncontent-type:application/json\nx-amz-pay-date:20180524T223710Z\nx-amz-pay-host:pay-api.amazon.eu\nx-amz-pay-region:EU\n\naccept;content-type;x-amz-pay-date;x-amz-pay-host;x-amz-pay-region\ne3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
      "stringToSign" : "AMZN-PAY-RSASSA-PSS\n6837f80baf1801c75db8119cffbadc26a89d20adb9cf3f3de4ce0fac31b4ae52"
    },

    {
      "name" : "get-vanilla-query-order-value",
      "uri" : "/",
      "method" : "GET",
      "parameters" : {
          "foo" : [ "b", "a" ]
      },
      "payload" : "",
      "canonicalRequest" : "GET\n/\nfoo=a&foo=b\naccept:application/json\ncontent-type:application/json\nx-amz-pay-date:20180524T223710Z\nx-amz-pay-host:pay-api.amazon.eu\nx-amz-pay-region:EU\n\naccept;content-type;x-amz-pay-date;x-amz-pay-host;x-amz-pay-region\ne3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
      "stringToSign" : "AMZN-PAY-RSASSA-PSS\na33266ec31c7f0ca8a0738eb49fb8c81319f14374a0b86d52e6911a9748a6d42"
    },

    {
      "name" : "get-vanilla-query-order-key",
      "uri" : "/",
      "method" : "GET",
      "parameters" : {
          "a" : [ "foo" ],
          "b" : [ "foo" ]
      },
      "payload" : "",
      "canonicalRequest" : "GET\n/\na=foo&b=foo\naccept:application/json\ncontent-type:application/json\nx-amz-pay-date:20180524T223710Z\nx-amz-pay-host:pay-api.amazon.eu\nx-amz-pay-region:EU\n\naccept;content-type;x-amz-pay-date;x-amz-pay-host;x-amz-pay-region\ne3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
      "stringToSign" : "AMZN-PAY-RSASSA-PSS\n6615ba7ca3734e97b35013f77c74d111ce2fa63ab52be161ad41bcd4b010120f"
    },

    {
      "name" : "get-vanilla-query-order-sort-by-key",
      "uri" : "/",
      "method" : "GET",
      "parameters" : {
          "A.1" : [ "foo" ],
          "A.2" : [ "foo" ],
          "A.10" : [ "foo" ]
      },
      "payload" : "",
      "canonicalRequest" : "GET\n/\nA.1=foo&A.10=foo&A.2=foo\naccept:application/json\ncontent-type:application/json\nx-amz-pay-date:20180524T223710Z\nx-amz-pay-host:pay-api.amazon.eu\nx-amz-pay-region:EU\n\naccept;content-type;x-amz-pay-date;x-amz-pay-host;x-amz-pay-region\ne3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
      "stringToSign" : "AMZN-PAY-RSASSA-PSS\n29d908d70c4bb50d62272976787245b52ed52e569fdae56ef3aa6d8979978ee7"
    },

    {
      "name" : "get-vanilla-query-order-key-case",
      "uri" : "/",
      "method" : "GET",
      "parameters" : {
          "foo" : [ "Zoo", "aha" ]
      },
      "payload" : "",
      "canonicalRequest" : "GET\n/\nfoo=Zoo&foo=aha\naccept:application/json\ncontent-type:application/json\nx-amz-pay-date:20180524T223710Z\nx-amz-pay-host:pay-api.amazon.eu\nx-amz-pay-region:EU\n\naccept;content-type;x-amz-pay-date;x-amz-pay-host;x-amz-pay-region\ne3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
      "stringToSign" : "AMZN-PAY-RSASSA-PSS\nf379cbd2e2fc9d5d50667a07ec22996335f731b474bb8bd366647d05d958c9d5"
    },

    {
      "name" : "get-vanilla-query-unreserved",
      "uri" : "/",
      "method" : "GET",
      "parameters" : {
          "-._~0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" : [ "-._~0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" ]
      },
      "payload" : "",
      "canonicalRequest" : "GET\n/\n-._~0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz=-._~0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz\naccept:application/json\ncontent-type:application/json\nx-amz-pay-date:20180524T223710Z\nx-amz-pay-host:pay-api.amazon.eu\nx-amz-pay-region:EU\n\naccept;content-type;x-amz-pay-date;x-amz-pay-host;x-amz-pay-region\ne3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
      "stringToSign" : "AMZN-PAY-RSASSA-PSS\n892abddd6e79db3feb5695b318af639c3e863494f6b4df32ec24fff4c1562a73"
    },

    {
      "name" : "post-vanilla-query-nonunreserved",
      "uri" : "/",
      "method" : "POST",
      "parameters" : {
          "@#$%^&+=/,?><`\";:\\|][{} " : [ "@#$%^&+=/,?><`\";:\\|][{} " ]
      },
      "algorithm" : "AMZN-PAY-RSASSA-PSS",
      "payload" : "",
      "canonicalRequest" : "POST\n/\n%40%23%24%25%5E%26%2B%3D%2F%2C%3F%3E%3C%60%22%3B%3A%5C%7C%5D%5B%7B%7D%20=%40%23%24%25%5E%26%2B%3D%2F%2C%3F%3E%3C%60%22%3B%3A%5C%7C%5D%5B%7B%7D%20\naccept:application/json\ncontent-type:application/json\nx-amz-pay-date:20180524T223710Z\nx-amz-pay-host:pay-api.amazon.eu\nx-amz-pay-region:EU\n\naccept;content-type;x-amz-pay-date;x-amz-pay-host;x-amz-pay-region\ne3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
      "stringToSign" : "AMZN-PAY-RSASSA-PSS\n66bbbf2b5d6d297a9393abbc0b4f9d62e30666bb0ec0dea8f1d4e45551440e4c"
    },

    {
      "name" : "post-vanilla-query-space",
      "uri" : "/",
      "method" : "POST",
      "parameters" : {
          "f oo" : [ "b ar" ]
      },
      "payload" : "",
      "canonicalRequest" : "POST\n/\nf%20oo=b%20ar\naccept:application/json\ncontent-type:application/json\nx-amz-pay-date:20180524T223710Z\nx-amz-pay-host:pay-api.amazon.eu\nx-amz-pay-region:EU\n\naccept;content-type;x-amz-pay-date;x-amz-pay-host;x-amz-pay-region\ne3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
      "stringToSign" : "AMZN-PAY-RSASSA-PSS\n99be3aeb19b5a1aebafc807d59f3740d3e604ae31cf928592fa70739ea64bb32"
    },
 ]