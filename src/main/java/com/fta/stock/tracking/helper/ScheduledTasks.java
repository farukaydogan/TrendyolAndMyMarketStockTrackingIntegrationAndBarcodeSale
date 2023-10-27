package com.fta.stock.tracking.helper;

import com.fta.stock.tracking.controller.api.TrendyolApiController;
import com.fta.stock.tracking.model.*;
import com.fta.stock.tracking.repository.UserRepository;
import com.fta.stock.tracking.service.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

//@EnableScheduling
@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final TrendyolOrderService trendyolOrderService;
    private final NotificationService notificationService;
    private final TrendyolApiService trendyolApiService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ProductService productService;
    private final SettingsService settingsService;
    private final PasswordEncoder passwordEncoder;

    private User adminUser;

    @Autowired
    public ScheduledTasks(UserService userService,TrendyolApiService trendyolApiService,TrendyolOrderService trendyolOrderService,UserRepository userRepository,ProductService productService,SettingsService settingsService,PasswordEncoder passwordEncoder,NotificationService notificationService) {
        this.trendyolOrderService = trendyolOrderService;
        this.userService = userService;
        this.trendyolApiService = trendyolApiService;
        this.userRepository = userRepository;
        this.productService = productService;
        this.settingsService = settingsService;
        this.passwordEncoder =passwordEncoder;
        this.notificationService =notificationService;
        initializeAdminUser();
    }

    private void initializeAdminUser() {
        // Admin kullanıcısını ve e-posta adresini arayarak bul
        String adminEmail = "faruk034595@gmail.com"; // Admin e-posta adresini buraya yazın

        if (userRepository.count() == 0) {
            // Kullanıcı ve ayarları oluştur
            User newUser = new User();
            newUser.setEmail("faruk034595@gmail.com");
            newUser.setPassword(passwordEncoder.encode("hppavilon614"));  // Gerçek uygulamada şifreyi şifrelemeyi unutmayın!
            newUser.setRole(Role.ADMIN);
            newUser.setFirstname("Admin");
            // Diğer kullanıcı alanlarını set edin...

            Settings newSettings = new Settings();
            newSettings.setTrendyolApiKey("elFJY2o1b0lPdXB3RVVWakJTYWs6N2M3M0pxZ0xXZzJ2akRzZ0NuVU0=");
            newUser.setSettings(newSettings);
            newSettings.setUser(newUser);
            newSettings.setEnableScheeduling(false);

            userService.createUserWithSettings(newUser, newSettings);
            addProductsFromCSV(newUser);
        }
        this.adminUser = userService.findUserByEmailAndRole(adminEmail, Role.ADMIN);
    }
    @Scheduled(fixedRate = 86400000) // 24 saat
    public void getTrendyolAllProductAndSaveDb() throws Exception {
        if (settingsService.isScheduleEnabled(String.valueOf(adminUser.getId()))) {
            System.out.println("All trendyol products save to db or update is running...");
            trendyolApiService.getAllProductFromTrendyolAndUpdateMyDb(adminUser);
        } else {
            System.out.println("Scheduled task for updating products is disabled.");
        }
    }
    @Scheduled(fixedRate = 8640000) //
    public void checkStockQuantityAndRunEmailNotification() throws Exception {
        if (settingsService.isScheduleEnabled(String.valueOf(adminUser.getId()))) {
            System.out.println("All trendyol products save to db or update is running...");
           List<TrendyolProducts>trendyolProducts= trendyolApiService.getAllProductFromTrendyolAndUpdateMyDb(adminUser);
           List<Product> productList=productService.convertTrendyolProductsToProducts(trendyolProducts);
            notificationService.checkStockQuantity(productList);

        } else {
            System.out.println("Scheduled task for updating products is disabled.");
        }
    }

    @Scheduled(fixedRate = 60000) // 1 dakika
    public void getTrendyolOrders() throws IOException {
        if (settingsService.isScheduleEnabled(String.valueOf(adminUser.getId()))) {
            System.out.println("Get trendyol order and update db is running...");
            trendyolApiService.getTrendyolOrderAndUpdateDecreaseStock(adminUser);
        } else {
            System.out.println("Scheduled task for getting orders is disabled.");
        }
    }

    @Scheduled(fixedRate = 900000) // 15 dakika
    public void stockUpdateToTrendyol() throws IOException {
        if (settingsService.isScheduleEnabled(String.valueOf(adminUser.getId()))) {
            System.out.println("Product quantity update to trendyol is running...");
            trendyolApiService.getTrendyolOrderAndUpdateDecreaseStock(adminUser);
            trendyolApiService.updateToTrendyolProductQuantity(adminUser);
        } else {
            System.out.println("Scheduled task for updating stock is disabled.");
        }
    }
    private void addProductsFromCSV(User user) {
        try {
            // resources klasöründeki CSV dosyasını oku
            InputStream inputStream = getClass().getResourceAsStream("/product_22_10_2023.csv");
            if (inputStream == null) {
                throw new FileNotFoundException("products.csv dosyası bulunamadı");
            }
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            CSVReader csvReader = new CSVReaderBuilder(inputStreamReader)
                    .withSkipLines(1)   // Başlık satırını atla
                    .build();
            List<String[]> allData = csvReader.readAll();
            csvReader.close(); // CSVReader'ı kapat

            // Her satır için bir ürün oluştur
            for (String[] row : allData) {
                Product product = new Product();
                product.setBarcode(row[1]);
                product.setColorCode(row[2]);
                product.setImagePath(row[3]);
                product.setName(row[5]);
                product.setPrice(Double.parseDouble(row[6]));
                product.setStockQuantity(Integer.parseInt(row[7]));
                product.setUser(user);
                if (row.length > 8 && row[8] != null && !row[8].isEmpty()) {
                    product.setTrendyolPrice(Double.parseDouble(row[8]));
                }

                // Ürünü veritabanına ekle
                productService.saveProduct(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}