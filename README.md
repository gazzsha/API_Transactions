
# ***REST API(GET) Client And Bank Service***

## Описание проекта

*Bank Service предоставляет получать транзакции.
*Client Service отвечает на клиентские запросы.

## Запуск

Необходимо сначала запускать Сервис Клиента для создание БД.

Поднять базы данные Postgres & Casandra
```
docker compose up
```

Сборка .jar и запуск .jar file 
```
cd ClientTransaction
./gradlew clean build
cd ../BankTransaction
./gradlew clean build
```
Запуск .jar file
```
cd ClientTransaction
java -jar build/libs/ClientTransaction-0.0.1-SNAPSHOT.jar
cd ../
java -jar BankTransaction/build/libs/BankTransaction-0.0.1-SNAPSHOT.jar
```

## Описание 

BankService получается доступные валюты из внешнего API заносит им в БД Casandra / Ежедневно он обращается к серверу для обновление цен закрытия.
Из-за огранечений внешнего API, делается несколько запросов в минуту. В среднем занимает 8~10 минут.

ClientService отвечает за клиентские запросы. (Полуить транзакции, создать bank-account, обновить лимиты)


## Документация

ClientService Documentation находится в папке ClientTransaction





