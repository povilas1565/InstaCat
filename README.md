# InstaCat
Бекэнд(серверная часть cоциального медиа приложения ), созданный на стеке: Java 1.8 + Spring Boot + Maven + MySQl80. Для запуска данного кода необходимо:

1. Установите программу Intelij Idea Ultimate от компании JetBrains (чтобы скачать эту программу быстро по ссылке: https://www.jetbrains.com/idea ) (я создал этот код в Intelij Idea Ultimate 2021.3.1).
2. После установки Intelij Idea Ultimate создайте свой аккаунт на Jetbrains: https://account.jetbrains.com .
3. Установить весь пакет MySQl Community. Ссылка на установщик: https://drive.google.com/drive/folders/1zLKnYN_Bj48Oz-rpnqmSdTNDmQUrb5d2?usp=sharing
4. Копируйте данный репозиторий: https://github.com/povilas1565/facebook/tree/master . (Кнопка Код/Загрузить Zip)
5.Переменные окружения для связи Backend с Бд MySQl размещены в файле application.properties: 
spring.jpa.hibernate.ddl-auto=update 
spring.profiles.active=@spring.profiles.active@ 
spring.jpa.open-in-view=false spring.servlet.multipart.max-file-size=300MB 
spring.servlet.multipart.max-request-size=300MB spring.servlet.multipart.enabled=true 
#MySQl spring.datasource.url=jdbc:mysql://localhost :3306/facebook1 
spring.datasource.username=root spring.datasource.password=povilas1565
6. Данные переменные окружения установлены по умолчанию, либо создайте вариант создания новой схемы в MySQL Workbench c логином и паролем, полученными при установке MySQl Community.
7. Запустить программу в Intelij Idea Ultimate.
8. Наслаждайтесь!
