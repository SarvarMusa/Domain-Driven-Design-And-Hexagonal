# Projem için Readme Dosyası (`README.md`)

## Genel Bakış
Bu proje, yazılım geliştirme sürecinde DDD (Domain-Driven Design) ve Hexagonal Architecture (Eşiksel Mimarlık) prensiplerini uygulayan bir örnektir. Bu mimarilerin yazılım sistemlerinin daha esnek, sürdürülebilir ve test edilebilir olmasını sağladığına inanırız.

## Teknolojiler
Proje, aşağıdaki teknolojileri kullanmaktadır:
- Java 17
- Spring Boot 2.5.4
- Maven 3.8.1
- PostgreSQL 13
- Docker 20.10.7

## Kurulum
### Gerekli Yazılım
- Java Development Kit (JDK) 17
- Docker

### Kurulum Adımları
1. **Docker Kurulumu:** Docker'ın bilgisayarınıza başarıyla kurulduğunu doğrulayın.
2. **Proje Klonlama:** Projenin kaynak kodlarını klonlayın veya indirin.
   ```
   git clone https://github.com/yourusername/yourproject.git
   cd yourproject
   ```
3. **Bağımlılıkların Yüklenmesi:** Maven kullanarak bağımlılıkları yükleyin.
   ```
   mvn install
   ```
4. **Veritabanı Ayarları:** `application.properties` dosyasını düzenleyerek veritabanı bağlantı ayarlarınızı yapılandırın.
5. **Docker ile Uygulama Çalıştırma:**
   ```
   docker-compose up
   ```

## Kullanım
Uygulamanın çalıştığından emin olduktan sonra, API endpoint'lerini kullanarak hizmetleri deneyebilirsiniz. Swagger UI üzerinden API dokümantasyonuna erişmek için `/swagger-ui.html` adresini ziyaret edin.

## DDD Nedir?
DDD, Domain-Driven Design yani Alan Yönelimli Tasarım, yazılım geliştirme sürecinde kullanılan bir yaklaşımdır. Bu yaklaşım, yazılımın iş mantığı üzerinde odaklanmayı ve bu mantığı en iyi şekilde temsil edecek şekilde modellemeyi amaçlar. DDD, karmaşık iş süreçlerini yönetmek ve yazılımın kullanıcı gereksinimlerine daha iyi uyum sağlamasını sağlamak için kullanılır.

## Hexagonal Architecture Nedir?
Hexagonal Architecture, ya da Eşiksel Mimarlık, yazılım sisteminin dış dünyayla etkileşimini sınırlayarak ve bu etkileşimi merkezi bir hub üzerinden yöneterek, yazılımın daha bağımsız, esnek ve test edilebilir olmasını sağlayan bir mimari yaklaşımdır. Bu yaklaşım, yazılımın işlevselliğini ve teknolojiyi ayrı tutarak, teknolojik değişikliklere karşı daha dayanıklı bir yazılım geliştirilmesini sağlar.