/*
Complete formatted project files for your Library Management app (JWT + GridFS + JdbcTemplate)

Instructions:
- These files are ADDITIVE. I will not delete any of your existing files.
- Copy each class into the exact path shown in the header comments.
- After copying, run `mvn -DskipTests=false test` (or start the app) and paste any stack traces if failures occur.

Files included below (each header is the target file path):
  - pom.xml (dependencies snippet)
  - src/main/resources/application.properties
  - Entities: User, Book, RefreshToken
  - Config: MongoConfig, SecurityConfig
  - Security: JwtUtil, JwtTokenFilter, JwtAuthenticationEntryPoint
  - Service: UserDetailsServiceImpl, AuthService, BookService, FileStorageService
  - Repositories: UserRepositoryJdbc, BookRepositoryJdbc, RefreshTokenRepositoryJdbc
  - Controllers: AuthController, BookController, HelloController
  - Tests: AuthControllerTest, BookControllerTest, HelloControllerTest, AuthServiceTest, BookServiceTest
*/

// ---------- pom.xml dependencies (snippet) ----------
/*
Add these dependencies to your pom.xml (inside <dependencies>...</dependencies>):

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>

<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt</artifactId>
  <version>0.9.1</version>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-test</artifactId>
  <scope>test</scope>
</dependency>

<dependency>
  <groupId>org.mockito</groupId>
  <artifactId>mockito-core</artifactId>
  <scope>test</scope>
</dependency>
*/


// ---------- src/main/resources/application.properties ----------
/*
# H2 in-memory datasource for quick development/tests
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver

# MongoDB GridFS
spring.data.mongodb.uri=mongodb://localhost:27017/library

# JWT
app.jwt.secret=ReplaceWithAStrongSecretKey
app.jwt.expiration-ms=3600000

# Active profile (change to 'test' during automated unit tests if desired)
spring.profiles.active=default
*/


// ---------- src/main/java/com/example/library/entity/User.java ----------
package com.example.library.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Set<String> roles = new HashSet<>();

    public User() { }

    public User(Long id, String username, String password, String email, Set<String> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles == null ? new HashSet<>() : new HashSet<>(roles);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles == null ? new HashSet<>() : new HashSet<>(roles); }

    public void addRole(String role) { if (role != null) this.roles.add(role); }
    public void removeRole(String role) { if (role != null) this.roles.remove(role); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}


// ---------- src/main/java/com/example/library/entity/Book.java ----------
package com.example.library.entity;

import java.time.LocalDate;

public class Book {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private LocalDate publishedDate;
    private String fileId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public LocalDate getPublishedDate() { return publishedDate; }
    public void setPublishedDate(LocalDate publishedDate) { this.publishedDate = publishedDate; }

    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }
}


// ---------- src/main/java/com/example/library/entity/RefreshToken.java ----------
package com.example.library.entity;

import java.time.Instant;

public class RefreshToken {
    private Long id;
    private Long userId;
    private String token;
    private Instant expiryDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Instant getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Instant expiryDate) { this.expiryDate = expiryDate; }
}


// ---------- src/main/java/com/example/library/config/MongoConfig.java ----------
package com.example.library.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.MongoDatabaseFactory;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri:mongodb://localhost:27017/library}")
    private String mongoUri;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUri);
    }

    @Bean
    public MongoDatabaseFactory mongoDbFactory(MongoClient mongoClient) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, extractDatabaseName(mongoUri));
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory factory) {
        return new MongoTemplate(factory);
    }

    @Bean
    public GridFsTemplate gridFsTemplate(MongoDatabaseFactory factory, MongoTemplate mongoTemplate) {
        return new GridFsTemplate(factory, mongoTemplate.getConverter());
    }

    private String extractDatabaseName(String uri) {
        try {
            String afterSlash = uri.substring(uri.lastIndexOf('/') + 1);
            if (afterSlash == null || afterSlash.isEmpty()) return "library";
            if (afterSlash.contains("?")) return afterSlash.substring(0, afterSlash.indexOf('?'));
            return afterSlash;
        } catch (Exception e) {
            return "library";
        }
    }
}


// ---------- src/main/java/com/example/library/security/JwtUtil.java ----------
package com.example.library.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret:ChangeMeInProperties}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms:3600000}")
    private long jwtExpirationMs;

    public String generateTokenForUserId(Long userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }
}


// ---------- src/main/java/com/example/library/security/JwtTokenFilter.java ----------
package com.example.library.security;

import com.example.library.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtTokenFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtil.validateToken(jwt)) {
                Long userId = jwtUtil.getUserIdFromToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserById(userId);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // logging omitted for brevity
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}


// ---------- src/main/java/com/example/library/security/JwtAuthenticationEntryPoint.java ----------
package com.example.library.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}


// ---------- src/main/java/com/example/library/config/SecurityConfig.java ----------
package com.example.library.config;

import com.example.library.security.JwtAuthenticationEntryPoint;
import com.example.library.security.JwtTokenFilter;
import com.example.library.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final JwtTokenFilter jwtTokenFilter;
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(JwtAuthenticationEntryPoint unauthorizedHandler,
                          JwtTokenFilter jwtTokenFilter,
                          UserDetailsServiceImpl userDetailsService) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtTokenFilter = jwtTokenFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeHttpRequests()
                .requestMatchers("/api/auth/**", "/auth/**", "/login", "/register", "/hello", "/", "/css/**", "/js/**", "/images/**").permitAll()
                .anyRequest().authenticated().and()
            .formLogin().loginPage("/auth/login").permitAll().and()
            .logout().logoutUrl("/auth/logout").permitAll();

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}


// ---------- src/main/java/com/example/library/service/UserDetailsServiceImpl.java ----------
package com.example.library.service;

import com.example.library.entity.User;
import com.example.library.repository.UserRepositoryJdbc;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepositoryJdbc userRepository;

    public UserDetailsServiceImpl(UserRepositoryJdbc userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findByUsername(username);
        if (u == null) throw new UsernameNotFoundException("User not found: " + username);
        return toUserDetails(u);
    }

    public UserDetails loadUserById(Long id) {
        User u = userRepository.findById(id);
        if (u == null) throw new UsernameNotFoundException("User not found id: " + id);
        return toUserDetails(u);
    }

    private UserDetails toUserDetails(User u) {
        Collection<GrantedAuthority> authorities = u.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return org.springframework.security.core.userdetails.User.withUsername(u.getUsername())
                .password(u.getPassword())
                .authorities(authorities)
                .accountExpired(false).accountLocked(false).credentialsExpired(false).disabled(false)
                .build();
    }
}


// ---------- src/main/java/com/example/library/repository/UserRepositoryJdbc.java ----------
package com.example.library.repository;

import java.util.HashSet;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.library.entity.User;

@Repository
public class UserRepositoryJdbc {

    private final JdbcTemplate jdbc;

    private final RowMapper<User> userMapper = (rs, rowNum) -> {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setEmail(rs.getString("email"));
        return u;
    };

    public UserRepositoryJdbc(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public User findByUsername(String username) {
        try {
            User u = jdbc.queryForObject("SELECT id,username,password,email FROM users WHERE username = ?", userMapper, username);
            List<String> roles = jdbc.queryForList("SELECT role FROM user_roles WHERE user_id = ?", String.class, u.getId());
            u.setRoles(new HashSet<>(roles));
            return u;
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public User findById(Long id) {
        try {
            User u = jdbc.queryForObject("SELECT id, username, password, email FROM users WHERE id = ?", userMapper, id);
            List<String> roles = jdbc.queryForList("SELECT role FROM user_roles WHERE user_id = ?", String.class, u.getId());
            u.setRoles(new HashSet<>(roles));
            return u;
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public void save(User user) {
        jdbc.update("INSERT INTO users(username,password,email) VALUES(?,?,?)", user.getUsername(), user.getPassword(), user.getEmail());
    }
}


// ---------- src/main/java/com/example/library/repository/BookRepositoryJdbc.java ----------
package com.example.library.repository;

import com.example.library.entity.Book;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepositoryJdbc {

    private final JdbcTemplate jdbc;

    private final RowMapper<Book> mapper = (rs, rowNum) -> {
        Book b = new Book();
        b.setId(rs.getLong("id"));
        b.setTitle(rs.getString("title"));
        b.setAuthor(rs.getString("author"));
        b.setIsbn(rs.getString("isbn"));
        java.sql.Date pd = rs.getDate("published_date");
        if (pd != null) b.setPublishedDate(pd.toLocalDate());
        b.setFileId(rs.getString("file_id"));
        return b;
    };

    public BookRepositoryJdbc(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public List<Book> findAll() {
        return jdbc.query("SELECT id,title,author,isbn,published_date,file_id FROM books ORDER BY title", mapper);
    }

    public Book findById(Long id) {
        try {
            return jdbc.queryForObject("SELECT id,title,author,isbn,published_date,file_id FROM books WHERE id = ?", mapper, id);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public Book save(Book book) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO books(title,author,isbn,published_date,file_id) VALUES(?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            if (book.getPublishedDate() != null) ps.setDate(4, Date.valueOf(book.getPublishedDate())); else ps.setNull(4, Types.DATE);
            ps.setString(5, book.getFileId());
            return ps;
        }, kh);

        Number key = kh.getKey(); if (key != null) book.setId(key.longValue());
        return book;
    }

    public Book update(Book book) {
        jdbc.update("UPDATE books SET title=?,author=?,isbn=?,published_date=?,file_id=? WHERE id=?",
                book.getTitle(), book.getAuthor(), book.getIsbn(),
                book.getPublishedDate() != null ? Date.valueOf(book.getPublishedDate()) : null,
                book.getFileId(), book.getId());
        return book;
    }

    public void deleteById(Long id) {
        jdbc.update("DELETE FROM books WHERE id = ?", id);
    }

    public boolean existsById(Long id) {
        Integer cnt = jdbc.queryForObject("SELECT count(*) FROM books WHERE id = ?", Integer.class, id);
        return cnt != null && cnt > 0;
    }

    public List<Book> findByTitleLike(String q) {
        if (q == null) return new ArrayList<>();
        String pattern = "%" + q + "%";
        return jdbc.query("SELECT id,title,author,isbn,published_date,file_id FROM books WHERE title LIKE ? ORDER BY title", mapper, pattern);
    }
}


// ---------- src/main/java/com/example/library/repository/RefreshTokenRepositoryJdbc.java ----------
package com.example.library.repository;

import com.example.library.entity.RefreshToken;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public class RefreshTokenRepositoryJdbc {

    private final JdbcTemplate jdbc;

    private final RowMapper<RefreshToken> mapper = (rs, rowNum) -> {
        RefreshToken rt = new RefreshToken();
        rt.setId(rs.getLong("id"));
        rt.setUserId(rs.getLong("user_id"));
        rt.setToken(rs.getString("token"));
        rt.setExpiryDate(Instant.ofEpochMilli(rs.getLong("expiry_millis")));
        return rt;
    };

    public RefreshTokenRepositoryJdbc(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public RefreshToken findByToken(String token) {
        try {
            return jdbc.queryForObject("SELECT id,user_id,token,expiry_millis FROM refresh_tokens WHERE token = ?", mapper, token);
        } catch (Exception ex) {
            return null;
        }
    }

    public void save(RefreshToken rt) {
        jdbc.update("INSERT INTO refresh_tokens(user_id,token,expiry_millis) VALUES(?,?,?)", rt.getUserId(), rt.getToken(), rt.getExpiryDate().toEpochMilli());
    }

    public void deleteByToken(String token) {
        jdbc.update("DELETE FROM refresh_tokens WHERE token = ?", token);
    }

    public int deleteByUserId(Long userId) {
        return jdbc.update("DELETE FROM refresh_tokens WHERE user_id = ?", userId);
    }
}


// ---------- src/main/java/com/example/library/service/AuthService.java ----------
package com.example.library.service;

import com.example.library.entity.RefreshToken;
import com.example.library.entity.User;
import com.example.library.repository.RefreshTokenRepositoryJdbc;
import com.example.library.repository.UserRepositoryJdbc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    public final RefreshTokenRepositoryJdbc refreshRepo;
    private final UserRepositoryJdbc userRepo;

    @Autowired
    public AuthService(RefreshTokenRepositoryJdbc refreshRepo, UserRepositoryJdbc userRepo) {
        this.refreshRepo = refreshRepo;
        this.userRepo = userRepo;
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken rt = new RefreshToken();
        rt.setUserId(userId);
        rt.setToken(UUID.randomUUID().toString());
        rt.setExpiryDate(Instant.now().plusSeconds(60L * 60L * 24L * 30L));
        refreshRepo.save(rt);
        return rt;
    }

    public RefreshToken getRefreshTokenByToken(String token) { return refreshRepo.findByToken(token); }

    public void verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
    }

    public void deleteRefreshToken(String token) { refreshRepo.deleteByToken(token); }

    public int deleteByUser(User user) { return refreshRepo.deleteByUserId(user.getId()); }
}


// ---------- src/main/java/com/example/library/service/BookService.java ----------
package com.example.library.service;

import com.example.library.entity.Book;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.repository.BookRepositoryJdbc;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookService {

    private final BookRepositoryJdbc repo;

    public BookService(BookRepositoryJdbc repo) { this.repo = repo; }

    public List<Book> listAll() { return repo.findAll(); }

    public Book findById(Long id) {
        Book b = repo.findById(id);
        if (b == null) throw new ResourceNotFoundException("Book not found: " + id);
        return b;
    }

    public void save(Book book) { repo.save(book); }

    public void update(Book book) { repo.update(book); }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Book not found: " + id);
        repo.deleteById(id);
    }

    public List<Book> search(String q) {
        if (q == null || q.trim().isEmpty()) return listAll();
        return repo.findByTitleLike(q);
    }
}


// ---------- src/main/java/com/example/library/service/FileStorageService.java ----------
package com.example.library.service;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.io.IOException;
import java.io.InputStream;

import org.bson.types.ObjectId;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.gridfs.model.GridFSFile;

@Service
@Profile("!test")
public class FileStorageService {

    private final GridFsTemplate gridFsTemplate;

    public FileStorageService(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    public String store(MultipartFile file) {
        try (InputStream in = file.getInputStream()) {
            ObjectId id = gridFsTemplate.store(in, file.getOriginalFilename(), file.getContentType());
            return id.toHexString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GridFsResource getFileResource(String id) {
        ObjectId objId;
        try {
            objId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            return null;
        }

        GridFSFile gridfsFile = gridFsTemplate.findOne(query(where("_id").is(objId)));
        if (gridfsFile == null) return null;
        return gridFsTemplate.getResource(gridfsFile);
    }
}


// ---------- src/main/java/com/example/library/controller/AuthController.java ----------
package com.example.library.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.entity.RefreshToken;
import com.example.library.entity.User;
import com.example.library.repository.UserRepositoryJdbc;
import com.example.library.security.JwtUtil;
import com.example.library.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepositoryJdbc userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, UserRepositoryJdbc userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.authService = authService;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public static class LoginRequest { public String username; public String password; }
    public static class RefreshRequest { public String refreshToken; }

    public static class TokenResponse {
        public String accessToken;
        public String refreshToken;
        public long expiresIn;

        public TokenResponse(String a, String r, long e) { this.accessToken = a; this.refreshToken = r; this.expiresIn = e; }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User existing = userRepo.findByUsername(user.getUsername());
        if (existing != null) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "username exists");
            return ResponseEntity.badRequest().body(err);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest lr) {
        User u = userRepo.findByUsername(lr.username);
        if (u == null) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "invalid creds");
            return ResponseEntity.status(401).body(err);
        }

        if (!passwordEncoder.matches(lr.password, u.getPassword())) {
            Map<String, String> err = new HashMap<>();
            err.put("error", "invalid creds");
            return ResponseEntity.status(401).body(err);
        }

        RefreshToken rt = authService.createRefreshToken(u.getId());
        String access = jwtUtil.generateTokenForUserId(u.getId());
        long expiresIn = 3600L;

        return ResponseEntity.ok(new TokenResponse(access, rt.getToken(), expiresIn));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest rr) {
        RefreshToken token = authService.refreshRepo.findByToken(rr.refreshToken);
        if (token == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid refresh token");
            return ResponseEntity.status(401).body(error);
        }

        authService.verifyExpiration(token);
        User user = userRepo.findById(token.getUserId());
        String access = jwtUtil.generateTokenForUserId(user.getId());
        return ResponseEntity.ok(new TokenResponse(access, token.getToken(), 3600L));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshRequest rr) {
        RefreshToken token = authService.refreshRepo.findByToken(rr.refreshToken);
        if (token == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Refresh token not found");
            return ResponseEntity.badRequest().body(error);
        }

        authService.refreshRepo.deleteByToken(rr.refreshToken);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out");
        return ResponseEntity.ok(response);
    }
}


// ---------- src/main/java/com/example/library/controller/BookController.java ----------
package com.example.library.controller;

import com.example.library.entity.Book;
import com.example.library.service.BookService;
import com.example.library.service.FileStorageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final FileStorageService fileStorageService;

    public BookController(BookService bookService, FileStorageService fileStorageService) {
        this.bookService = bookService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("books", bookService.listAll());
        return "books/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("bookForm", new Book());
        return "books/add";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("bookForm") Book bookForm,
                      BindingResult bindingResult,
                      @RequestParam(value = "file", required = false) MultipartFile file,
                      RedirectAttributes ra) {
        if (bindingResult.hasErrors()) return "books/add";
        if (file != null && !file.isEmpty()) {
            String fileId = fileStorageService.store(file);
            bookForm.setFileId(fileId);
        }
        bookService.save(bookForm);
        ra.addFlashAttribute("success", "Book added");
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        return "books/view";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        bookService.delete(id);
        ra.addFlashAttribute("success", "Book deleted");
        return "redirect:/books";
    }
}


// ---------- src/main/java/com/example/library/controller/HelloController.java ----------
package com.example.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("message", "Hello, Spring MVC + Thymeleaf!");
        return "hello";
    }
}


// ---------- src/test/java/com/example/library/controller/AuthControllerTest.java ----------
package com.example.library.controller;

import com.example.library.entity.User;
import com.example.library.repository.UserRepositoryJdbc;
import com.example.library.security.JwtUtil;
import com.example.library.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private UserRepositoryJdbc userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testRegisterUserSuccess() throws Exception {
        when(userRepository.findByUsername("alice")).thenReturn(null);
        when(passwordEncoder.encode("password123")).thenReturn("ENCODED");

        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content("{\"username\":\"alice\",\"password\":\"password123\",\"email\":\"alice@example.com\"}"))
                .andExpect(status().isOk());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUserAlreadyExists() throws Exception {
        User existing = new User();
        existing.setUsername("bob");
        when(userRepository.findByUsername("bob")).thenReturn(existing);

        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content("{\"username\":\"bob\",\"password\":\"pwd\",\"email\":\"bob@example.com\"}"))
                .andExpect(status().isBadRequest());

        verify(userRepository, never()).save(any(User.class));
    }
}


// ---------- src/test/java/com/example/library/controller/BookControllerTest.java ----------
package com.example.library.controller;

import com.example.library.entity.Book;
import com.example.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void testListBooks() throws Exception {
        when(bookService.listAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/list"))
                .andExpect(model().attributeExists("books"));
    }

    @Test
    public void testAddBook() throws Exception {
        mockMvc.perform(post("/books/add")
                .param("title", "Clean Code")
                .param("author", "Robert C. Martin")
                .param("isbn", "9780132350884"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        verify(bookService, times(1)).save(any(Book.class));
    }
}


// ---------- src/test/java/com/example/library/controller/HelloControllerTest.java ----------
package com.example.library.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class HelloControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new HelloController()).build();
    }

    @Test
    public void testHello() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(view().name("hello"))
                .andExpect(model().attributeExists("message"));
    }
}


// ---------- src/test/java/com/example/library/service/AuthServiceTest.java ----------
package com.example.library.service;

import com.example.library.entity.RefreshToken;
import com.example.library.entity.User;
import com.example.library.repository.RefreshTokenRepositoryJdbc;
import com.example.library.repository.UserRepositoryJdbc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private RefreshTokenRepositoryJdbc refreshRepo;

    @Mock
    private UserRepositoryJdbc userRepo;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testVerifyExpirationThrowsWhenExpired() {
        RefreshToken rt = new RefreshToken();
        rt.setId(1L);
        rt.setToken("t");
        rt.setExpiryDate(Instant.now().minusSeconds(60));

        when(refreshRepo.findByToken("t")).thenReturn(rt);

        Exception ex = assertThrows(RuntimeException.class, () -> authService.verifyExpiration(rt));
        assertTrue(ex.getMessage().toLowerCase().contains("expired"));
    }

    @Test
    public void testDeleteByUserCallsRepo() {
        User u = new User();
        u.setId(5L);
        when(refreshRepo.deleteByUserId(5L)).thenReturn(2);
        int deleted = authService.deleteByUser(u);
        assertEquals(2, deleted);
    }
}


// ---------- src/test/java/com/example/library/service/BookServiceTest.java ----------
package com.example.library.service;

import com.example.library.entity.Book;
import com.example.library.repository.BookRepositoryJdbc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Mock
    private BookRepositoryJdbc bookRepo;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        when(bookRepo.findAll()).thenReturn(Arrays.asList(new Book(), new Book()));
        List<Book> list = bookService.listAll();
        assertEquals(2, list.size());
    }

    @Test
    public void testSaveAndDelete() {
        Book b = new Book();
        b.setTitle("T");
        bookService.save(b);
        verify(bookRepo, times(1)).save(b);

        bookService.delete(1L);
        verify(bookRepo, times(1)).deleteById(1L);
    }
}


/*
Notes:
- These files are formatted and ready to copy into your project.
- They are intentionally additive and non-destructive.
- If you'd like, I can generate a git-format patch (add-only) containing these files.
- If you prefer a zip with all files ready-to-drop, I can produce one (but I cannot modify your uploaded archive). 

Next steps (pick one):
1) I generate a git patch (add-only) you can apply.
2) I generate the files as a downloadable zip for you to extract and merge.
3) You paste these into your repo yourself.

Which do you prefer? Paste any failing stack traces here and I will patch the specific file non-destructively.
*/
