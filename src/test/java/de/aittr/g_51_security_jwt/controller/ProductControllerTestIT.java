package de.aittr.g_51_security_jwt.controller;

import de.aittr.g_51_security_jwt.domain.Product;
import de.aittr.g_51_security_jwt.domain.Role;
import de.aittr.g_51_security_jwt.domain.User;
import de.aittr.g_51_security_jwt.repository.ProductRepository;
import de.aittr.g_51_security_jwt.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import javax.crypto.SecretKey;
import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest при старте тестов запускает наше приложение
//полноценно на тестовом экземпляре tomcat
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//Порядок вкл. => @Order(1)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerTestIT {

    @LocalServerPort
    private int port;

    //TestRestTemplate отправляет запросы на тестированное приложение и получать http ответы
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Value("${key.access}")
    private String accessPhrase;

    private Product testProduct;
    private HttpHeaders headers;
    private User admin;
    private String adminAccessToken;
    private SecretKey accessKey;

    @BeforeEach
    public void setUp() {
        testProduct = createTestProduct();

        //пустой заголовок для http-запроса
        headers = new HttpHeaders();

        // create secret key
        accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessPhrase));

        admin = createAdmin();

        adminAccessToken = generateAdminAccessToken();
    }

    @Test
    @Order(1)
    public void chackRequestForAllProducts() {

        //создаём объект http-запроса
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Product[]> response = restTemplate.exchange("/products", HttpMethod.GET, request, Product[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Unexpected http status code");

        Product[] body = response.getBody();

        assertNotNull(body, "Response body should not be null");

        for (Product product : body) {
            assertNotNull(product.getId(), "Product id should not be null");
            assertNotNull(product.getTitle(), "Product title should not be null");
            assertNotNull(product.getPrice(), "Product price should not be null");
        }
    }

    @Test
    @Order(2)
    public void checkForbiddenStatusWhileSavingProductWithoutAuthorization() {
        HttpEntity<Product> request = new HttpEntity<>(testProduct, headers);

        ResponseEntity<Product> response = restTemplate.exchange("/products", HttpMethod.POST, request, Product.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Unexpected http status code");
        assertNull(response.getBody(), "Response body should be null");
    }

    @Test
    @Order(3)
    public void checkSuccessWhileSavingProductWithAdminAuthorization() {
        headers.add(HttpHeaders.COOKIE, "Access-Token=" + adminAccessToken);
        HttpEntity<Product> request = new HttpEntity<>(testProduct, headers);

        ResponseEntity<Product> response = restTemplate.exchange(
                "/products", HttpMethod.POST, request, Product.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Unexpected http status code");
        Product savedProduct = response.getBody();
        assertNotNull(savedProduct, "Saved product should not be null");
        assertNotNull(savedProduct.getId(), "Saved product id should not be null");

        assertEquals(testProduct.getTitle(), savedProduct.getTitle(), "Saved product has incorrect title");
        assertEquals(testProduct.getPrice(), savedProduct.getPrice(), "Saved product has incorrect price");

        userRepository.delete(admin);
        productRepository.delete(savedProduct);
    }

    private Product createTestProduct() {
        Product product = new Product();
        product.setTitle("Test product");
        product.setPrice(new BigDecimal(777));
        return product;
    }

    private String generateAdminAccessToken() {
        return Jwts.builder()
                .subject(admin.getEmail())
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .signWith(accessKey)
                .compact();
    }

    private User createAdmin() {
        String adminEmail = "admin@test.com";
        User admin = userRepository.findByEmail(adminEmail).orElse(null);

        if (admin == null) {
            admin = new User();
            admin.setEmail(adminEmail);
            admin.setName("Admin");
            admin.setRole(Role.ROLE_ADMIN);
            admin.setPassword("111");

            userRepository.save(admin);
        }
        return admin;
    }
}