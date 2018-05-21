package NLP;

import static NLP.AuthService.getAuth;

public class Address {
    public static String ACCESS_TOKEN=getAuth();
    public static String URL_lexer="https://aip.baidubce.com/rpc/2.0/nlp/v1/lexer?access_token="+ACCESS_TOKEN;

}
