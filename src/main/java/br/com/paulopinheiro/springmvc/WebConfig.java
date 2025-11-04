package br.com.paulopinheiro.springmvc;

import br.com.paulopinheiro.springmvc.interceptors.DemoHandlerInterceptor;
import br.com.paulopinheiro.springmvc.security.DefaultAuthenticationFailureHandler;
import br.com.paulopinheiro.springmvc.security.DefaultAuthenticationProvider;
import br.com.paulopinheiro.springmvc.security.DefaultSuccessLogoutHandler;
import br.com.paulopinheiro.springmvc.security.DefaultUserDetailsService;
import br.com.paulopinheiro.springmvc.security.SetupDataLoader;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled=true, securedEnabled=true)
@Configuration
@ComponentScan(basePackages = {"br.com.paulopinheiro.springmvc"})
@EnableTransactionManagement
@EnableJpaRepositories("br.com.paulopinheiro.springmvc.persistence.repositories.springdata")
@PropertySource("classpath:database.properties")
@PropertySources({
    @PropertySource("classpath:test.properties"),
    @PropertySource("classpath:test2.properties")
})
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver bean = new InternalResourceViewResolver();

        bean.setViewClass(JstlView.class);
        bean.setPrefix("/WEB-INF/view/");
        bean.setSuffix(".jsp");

        return bean;
    }

    @Bean
    public HandlerExceptionResolver errorHandler() {
        return (HttpServletRequest request, 
                HttpServletResponse response,
                Object handler,
                Exception ex) -> {
            ModelAndView model = new ModelAndView("error-page");
            model.addObject("exception", ex);
            model.addObject("handler", handler);
            
            return model;
        };
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new CookieLocaleResolver();
    }

    @Bean("messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("msg");
        messageSource.setDefaultEncoding("UTF-8");

        return messageSource;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DemoHandlerInterceptor())
                .addPathPatterns("/test/simple-model-demo");

        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**", "/js/**")
                .addResourceLocations("/css/", "/js/");
    }

    // Mapping of resources from the file system
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//	    registry
//	      .addResourceHandler("/images/**")
//	      .addResourceLocations("file:C:\\images\\");
//	 }
    // Mapping multiple locations
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//	    registry
//	      .addResourceHandler("/resources/**")
//	      .addResourceLocations("/resources/","classpath:/other-resources/");
//	}
    // PathResourceResolver Example
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/resources/**")
//				.addResourceLocations("/resources/", "/other-resources/")
//				.setCachePeriod(3600)
//				.resourceChain(true)
//				.addResolver(new PathResourceResolver());
//	}
    // EncodedResourceResolver Example
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/other-files/**")
//				.addResourceLocations("file:C:\\other-files\\")
//				.setCachePeriod(3600)
//				.resourceChain(true)
//				.addResolver(new EncodedResourceResolver());
//	}
    // Chaining Resources Example
//	@Override public void addResourceHandlers(ResourceHandlerRegistry registry) { 
//		registry.addResourceHandler("/js/**")
//				.addResourceLocations("/js/")
//				.setCachePeriod(3600)
//				.resourceChain(true)
//				.addResolver(new GzipResourceResolver())
//				.addResolver(new PathResourceResolver()); 
//	}

//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        UserDetails user1 = User.withUsername("user1").password(passwordEncoder().encode("test1")).roles("CUSTOMER").build();
//        UserDetails user2 = User.withUsername("user2").password(passwordEncoder().encode("test2")).roles("CUSTOMER").build();
//        UserDetails admin = User.withUsername("admin").password(passwordEncoder().encode("root")).roles("ADMIN").build();
//
//        return new InMemoryUserDetailsManager(user1, user2, admin);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                    .requestMatchers("/test/admin/**").hasRole("ADMIN")
//                    .requestMatchers("/test/anonymous").anonymous()
//                    .requestMatchers("/test/login_page").permitAll()
//                    .anyRequest().authenticated()
//                )
////                .formLogin(withDefaults()
//                .formLogin(form -> form
//                    .loginPage("/test/login_page")
//                    .loginProcessingUrl("/test/perform_login")
//                    .defaultSuccessUrl("/test/homepage")
//                    .failureUrl("/test/login_page?error=true")
//                    .failureHandler(authenticationFailureHandler())
//                )
//                .logout(logout -> logout
//                    .logoutUrl("/test/perform_logout")
//                    .logoutSuccessUrl("/test/login_page")
//                    .deleteCookies("JSESSIONID")
//                    .logoutSuccessHandler(logoutSuccessHandler())
//                )
//                .httpBasic(withDefaults());
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/test/admin/**").hasRole("ADMIN")
                    .requestMatchers("/test/manager/**").hasAuthority(SetupDataLoader.WRITE_PRIVILEGE)
                    .requestMatchers("/test/anonymous").anonymous()
                    .requestMatchers("/test/login_remember_me",
                                     "/test/login_page*", 
                                     "/test/user-registration-form-security-demo", 
                                     "/test/create-user-security-demo").permitAll()
                    .anyRequest().authenticated()
                )
//                .formLogin(withDefaults())
                .formLogin(form -> form
                    .loginPage("/test/login_remember_me")
                    .loginProcessingUrl("/test/perform_login")
                    .defaultSuccessUrl("/test/homepage",false)
                    .failureUrl("/test/login_page?error=true")
                    .failureHandler(authenticationFailureHandler())
                )
                .logout(logout -> logout
                    .logoutUrl("/test/perform_logout")
                    .deleteCookies("JSESSIONID")
//                    .logoutSuccessHandler(logoutSuccessHandler())
                    .logoutSuccessUrl("/test/login_remember_me")
                )
                .rememberMe(rememberMe -> rememberMe
                    .key("superSecretKey")
                    .rememberMeParameter("remember")
                    .rememberMeCookieName("rememberLogin")
                )
                .httpBasic(withDefaults()
                );

        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        System.out.println("Authentication Failure");
        return new DefaultAuthenticationFailureHandler();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new DefaultSuccessLogoutHandler();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new DefaultUserDetailsService();
    }

    @Bean
    public AuthenticationProvider authProvider() {
        return new DefaultAuthenticationProvider();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.authenticationProvider(authProvider());

        return authenticationManagerBuilder.build();
    }

    // ===== Spring Data JPA Configuration
    private static final String PROPERTY_DRIVER = "driver";
    private static final String PROPERTY_URL = "url";
    private static final String PROPERTY_USERNAME = "user";
    private static final String PROPERTY_PASSWORD = "password";

    @Autowired private Environment environment;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();

        ds.setUrl(environment.getProperty(PROPERTY_URL));
        ds.setUsername(environment.getProperty(PROPERTY_USERNAME));
        ds.setPassword(environment.getProperty(PROPERTY_PASSWORD));
        ds.setDriverClassName(environment.getProperty(PROPERTY_DRIVER));

        return ds;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean lfb = new LocalContainerEntityManagerFactoryBean();

        lfb.setDataSource(dataSource());
        lfb.setPackagesToScan("br.com.paulopinheiro.springmvc.persistence.entities");
        lfb.setJpaVendorAdapter(eclipseLinkAdapter());
        lfb.setJpaProperties(eclipseLinkProperties());

        return lfb;
    }

    private JpaVendorAdapter eclipseLinkAdapter() {
        EclipseLinkJpaVendorAdapter eclipseLinkJpaVendorAdapter = new EclipseLinkJpaVendorAdapter();

        eclipseLinkJpaVendorAdapter.setShowSql(true);

        return eclipseLinkJpaVendorAdapter;
    }

    private Properties eclipseLinkProperties() {
        Properties props = new Properties();

        props.put("eclipselink.weaving", "false");

        return props;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return transactionManager;
    }
}
