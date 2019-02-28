package rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.infraestructure.adapters.output.asb;
import lombok.extern.slf4j.Slf4j;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.Event;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.EventBuilder;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.EventType;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.event.provider.EventPublisher;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.arq.infra.exception.InvalidParameterException;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.infraestructure.adapters.output.asb.domain.GcpDomain;
import rtl.tot.corp.mrex.pctm.product.catalog.msgmover.mrexpctmproductcatalogmsgmover.infraestructure.adapters.output.asb.domain.Messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class EventPublisherService {

    
    private final RestTemplate restTemplate;
    //private final String apiGCPUrl;
    
    @Value("${api.pubsub.gcp.private.key}")
	private String privateKeyContent;
    
    @Value("${api.pubsub.gcp.public.key}")
    private String publicKeyContent;
    
    @Value("${api.pubsub.gcp.alg}")
    private String alg;
    
    @Value("${api.pubsub.gcp.typ}")
    private String typ;
    
    @Value("${api.pubsub.gcp.kid}")
    private String kid;
    
    @Value("${api.pubsub.gcp.iss}")
    private String iss;
    
    @Value("${api.pubsub.gcp.sub}")
    private String sub;
    
    @Value("${api.pubsub.gcp.aud}")
    private String aud;
    
    @Autowired
    private Environment env;


    @Autowired
    public EventPublisherService(final RestTemplate restTemplate) {
        
        this.restTemplate = restTemplate;
    }
    
    
  
    public boolean publish(final Event event) {
     
    	GcpDomain gcpBody;
    	Messages gcpMessage;
    	List<Messages> listGcpMessage;
    	
        try {
        	        	        	
        	//Set data in GCP Standard Body.
        	
        	gcpMessage = new Messages();
        	gcpBody = new GcpDomain();
        	listGcpMessage = new ArrayList<Messages>();
        	
        	gcpMessage.setAttributes(null);
        	gcpMessage.setData(Base64.getEncoder().encodeToString(event.toString().getBytes()));
        	
        	listGcpMessage.add(gcpMessage);
        	
        	gcpBody.setMessages(listGcpMessage);        	
        	
        	
        	HttpHeaders headers = new HttpHeaders();        	
        	headers.set("Authorization", "Bearer " + generateToken());
        	
        	HttpEntity<GcpDomain> entity = new HttpEntity<GcpDomain>(gcpBody, headers);
        	
        	ResponseEntity<String> response = restTemplate
        			.exchange(urlGCP(event.getEventType()), HttpMethod.POST, entity, String.class);
        	
        	log.info("Response GCP: " + response.getBody());
        	log.info("Status Response: " + response.getStatusCodeValue());
        	
            log.info("Event published: " + event);
            
        } catch (Exception e) {
        	
            log.error("Event could not be send. Cause: " + e.getMessage());
            return false;
        }
        return true;
    }
    
    
    /**
     * Generate JWS Token for Authentication Pub/Sub GCP.
     * @return token
     */    
    private String generateToken() {
    	
    	RSAPublicKey publicKey = null;
    	RSAPrivateKey privateKey = null;
    	Map<String,Object> header;
    	String token = null;    	    	
    	long now = System.currentTimeMillis();
    	    	
    	try {    		
    		
    		// Set Header JSON
    		header = new HashMap<String, Object>();
    		header.put("alg", alg);
    		header.put("typ", typ);
    		header.put("kid", kid);    		   		    	
   		     		    		
    		KeyFactory kf = KeyFactory.getInstance("RSA");    	

    		// Generate RSAPrivateKey from PrivateKey
    		PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));   		    		
    		privateKey = (RSAPrivateKey) kf.generatePrivate(keySpecPKCS8);
    		
    		// Generate RSAPublicKey from PublicKey		    		    		
    		CertificateFactory fact = CertificateFactory.getInstance("X.509");
    		InputStream stream = new ByteArrayInputStream(Base64.getDecoder().decode(publicKeyContent));    		    		
    		X509Certificate cer = (X509Certificate)fact.generateCertificate(stream);
    		publicKey = (RSAPublicKey) cer.getPublicKey();	    	  		    	   		   	
  		   		    		
    		//Generate Token with Header, Payload and Algorithm RSA256
    		Algorithm algorithm = Algorithm.RSA256(null, privateKey);
    		token = JWT.create()
    				.withHeader(header)
    				.withClaim("iss", iss)
    				.withClaim("sub", sub)
    				.withClaim("aud", aud)
    				.withClaim("iat", new Date(now))
    				.withClaim("exp", new Date(now + 3600 * 1000L))
    		        .sign(algorithm);   
    		
    		
    	}catch (JWTCreationException | NoSuchAlgorithmException | InvalidKeySpecException | CertificateException exception) {
    		
    		log.error("Error generating token. Cause: " + exception.getMessage().toString());    		
    	}
    	
    	return token;    	    		    	    	
    }
    
    
    private String urlGCP (String eventType) throws Exception {
    	
    	String endpoint = null;
    	
    	
    	switch(eventType)
    	{
    		case "productCreated":
    			
    			endpoint =  env.getProperty("api.pubsub.gcp.endpoint.product.create");    			
    			break;
    			
    		/*case "productUpdated":
    			    			
    			endpoint = env.getProperty("api.pubsub.gcp.endpoint.product.update");    		
    			break;
    		*/	
    		default:
    			log.error("Event type undefined: " + eventType);
    			throw new Exception("Event type undefined");
    	}
    	
    	
    	return endpoint;
    }

}
