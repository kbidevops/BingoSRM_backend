package kr.or.smplatform.itsm.resttemplate;

import org.springframework.web.client.RestTemplate;

public class RestClientTest {
	
	
	
	public static void main(String[] args) {
//		final String uri = "https://www.peoplemac.com/sangju/site/retrieveNotiListAjax.do";
		final String uri = "http://localhost:8080/itsm/itsm/user/mngr/retrieveAjax.do?userId=tipa1";
	     
	    RestTemplate restTemplate = new RestTemplate();
	    String result = restTemplate.getForObject(uri, String.class);
	     
	    System.out.println(result);
	}
}
